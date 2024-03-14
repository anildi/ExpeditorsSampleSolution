package expeditors.backend.commonconfig.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Base64;

@Component
public class RestClientFactory {
    @Autowired
    private ApplicationContext context;

    @Value("${spring.profiles.active}")
    private String profiles;

    public RestClient get(String baseUrl, String user, String pw) {
        var restClient = profiles.contains("ssl") ? ssl(baseUrl, user, pw)
                : basicAuth(baseUrl, user, pw);

        return restClient;
    }

    public RestClient noAuth(String baseUrl) {
        var restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();

        return restClient;
    }

    public RestClient basicAuth(String baseUrl, String user, String pw) {
        String basicAuthHeader = STR."basic \{Base64.getEncoder()
                .encodeToString((user + ":" + pw).getBytes())}";

        var restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", basicAuthHeader)
                .build();

        return restClient;
    }

    public RestClient ssl(String baseUrl, String user, String pw) {
        baseUrl = baseUrl.replace("http:", "https:");
        String basicAuthHeader = STR."basic \{Base64.getEncoder()
                .encodeToString((user + ":" + pw).getBytes())}";
        var sslRestTemplate = context.getBean("fakessltemplate", RestTemplate.class);
        sslRestTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
        var restClient = RestClient.builder(sslRestTemplate)
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", basicAuthHeader)
                .build();

        return restClient;
    }
}