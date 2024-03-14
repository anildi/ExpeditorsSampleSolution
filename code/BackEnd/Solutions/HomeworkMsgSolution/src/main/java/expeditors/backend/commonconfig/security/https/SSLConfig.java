package expeditors.backend.commonconfig.security.https;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * To configure the server to use SSL:
 * 1) Make a self signed certificate.  Important - the CN field
 * where it asks you for first and last name, should be set to
 * the hostname, e.g. localhost.
 * keytool -genkeypair -alias larkuspring
 * -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore larkUKeyfile.p12
 * <p>
 * 2) Make sure you point to this file with the appropriate properties in
 * application.[properties|yml]
 * <p>
 * To configure RestTemplate to use SSL see one of the two factory methods below
 */
@Configuration
@Profile("ssl | ssltest")
public class SSLConfig {

    private SslBundles sslBundles;
    private SslBundle ourBundle;

    public SSLConfig(SslBundles sslBundles) {
        this.sslBundles = sslBundles;
        this.ourBundle = sslBundles.getBundle("web-server");
    }

//    @Bean
//    public RestTemplate sslRestTemplate() {
//        var rt = new RestTemplateBuilder()
//                .setSslBundle(sslBundles.getBundle("web-server"))
//                .build();
//
//        return rt;
//    }

    @Bean
    public RestTemplate sslRestTemplate() {
        SSLConnectionSocketFactory sslSocketFactory =
                SSLConnectionSocketFactoryBuilder.create()
                        .setSslContext(this.ourBundle.createSslContext())
                        .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();
        HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(sslSocketFactory).build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .evictExpiredConnections()
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }


    /**
     * This one allows self signed certificates
     */
    @Bean("fakessltemplate")
    public RestTemplate restTemplateFakeSSL() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        //We point it at the same keystore as the server
        clientStore.load(getClass().getResourceAsStream("larkUKeyfile.p12"), "password".toCharArray());

        TrustStrategy lam = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = SSLContexts.custom()
                //.loadTrustMaterial(null, acceptingTrustStrategy)
                .loadKeyMaterial(clientStore, "password".toCharArray())
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();

        SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        HttpClientConnectionManager cm =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(sslSocketFactory).build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectTimeout(10000); // 10 seconds
//        requestFactory.setReadTimeout(10000); // 10 seconds

        RestTemplate restTemplate = new RestTemplateBuilder()
                .requestFactory(() -> requestFactory)
//                .errorHandler(new TemplateErrorHandler())
                .build();
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
   }
//
//    /**
//     * This one checks the server's certificate, but accepts self signed certificates
//     *
//     * @return
//     * @throws IOException
//     * @throws KeyStoreException
//     * @throws CertificateException
//     * @throws NoSuchAlgorithmException
//     * @throws UnrecoverableKeyException
//     * @throws KeyManagementException
//     */
//    @Bean("realssltemplate")
//    public RestTemplate restTemplateRealSSL() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
//        KeyStore clientStore = KeyStore.getInstance("PKCS12");
//        //We point it at the same keystore as the server
//        clientStore.load(getClass().getResourceAsStream("larkUKeyfile.p12"), "password".toCharArray());
//
//        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
//        sslContextBuilder.setProtocol("TLS");
//        sslContextBuilder.loadKeyMaterial(clientStore, "password".toCharArray());
//        sslContextBuilder.loadTrustMaterial(new TrustSelfSignedStrategy());
//
//        SSLConnectionSocketFactory sslConnectionSocketFactory =
//                new SSLConnectionSocketFactory(sslContextBuilder.build());
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setSSLSocketFactory(sslConnectionSocketFactory)
//                .build();
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//        requestFactory.setConnectTimeout(10000); // 10 seconds
//        requestFactory.setReadTimeout(10000); // 10 seconds
//
//        RestTemplate restTemplate = new RestTemplateBuilder()
//                .requestFactory(() -> requestFactory)
//                .errorHandler(new TemplateErrorHandler()).build();
//        return restTemplate;
//    }
//
    TrustManager[] certs = new TrustManager[]{new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }};

    public static class TrustAllHostNameVerifier implements HostnameVerifier {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }
}