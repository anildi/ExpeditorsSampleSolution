package expeditors.backend.adoptapp.controller;

import expeditors.backend.adoptapp.domain.Adopter;
import expeditors.backend.adoptapp.domain.Pet;
import expeditors.backend.adoptapp.domain.PetType;
import expeditors.backend.commonconfig.client.RestClientFactory;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author whynot
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdopterControllerClientTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestClientFactory clientFactory;

    private RestClient restClient;

    @Value("${client.user}") String user;
    @Value("${client.password}") String password;

    @Autowired
    private ApplicationContext context;

    private String baseUrl;
    private String rootUrl;
    private String oneAdopterUrl;

    @PostConstruct
    public void init() {
        baseUrl = "http://localhost:" + port;
        rootUrl = "/petservice";
        oneAdopterUrl = rootUrl + "/{id}";

        this.restClient = clientFactory.get(baseUrl, user, password);

//        String basicAuthHeader = STR."basic \{Base64.getEncoder().encodeToString((user + ":" + password).getBytes())}";
//        this.restClient = RestClient.builder()
//                .baseUrl(baseUrl)
//                .defaultHeader("Accept", "application/json")
//                .defaultHeader("Content-Type", "application/json")
//                .defaultHeader("Authorization", basicAuthHeader)
//                .build();
    }


    @Test
    public void testGetAll() {
        //This is the Spring REST mechanism to create a paramterized type
        ParameterizedTypeReference<List<Adopter>> ptr =
                new ParameterizedTypeReference<List<Adopter>>() {};

        ResponseEntity<List<Adopter>> response = restClient.get()
                .uri(rootUrl)
                .retrieve()
                .toEntity(ptr);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetOneWithGoodId() {
        ResponseEntity<Adopter> response = restClient.get()
                .uri(oneAdopterUrl, 1)
                .retrieve()
                .toEntity(Adopter.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Adopter adopter = response.getBody();

        System.out.println(adopter);
    }

    @Test
    public void testGetOneWithBadId() {
        ResponseEntity<?> response = restClient.get()
                .uri(oneAdopterUrl, 1000)
                .retrieve()
                .onStatus(code -> code == HttpStatus.NOT_FOUND, (req, resp) -> {
                })
                .toBodilessEntity();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testPostAdopter() throws URISyntaxException {
        Adopter a = new Adopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                Pet.builder(PetType.DOG).name("woofie").breed("mixed").build());

        ResponseEntity<Void> result = restClient.post()
                .uri(rootUrl)
                .body(a)
                .retrieve()
                .toBodilessEntity();


        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        String locHdr = result.getHeaders().get("Location").get(0);
        assertNotNull(locHdr);

    }

    @Test
    public void testDeleteAdopterWithGoodId() {
        Adopter c = new Adopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                Pet.builder(PetType.DOG).name("woofie").breed("mixed").build());

        ResponseEntity<Void> result = restClient.post()
                .uri(rootUrl)
                .body(c)
                .retrieve()
                .toBodilessEntity();

        //Get the location header
        String locHdr = result.getHeaders().get("Location").get(0);
        System.out.println("locHdr: " + locHdr);

        ResponseEntity<Adopter> newResult = restClient.get()
                .uri(locHdr)
                .retrieve()
                .toEntity(Adopter.class);

        assertEquals(newResult.getStatusCode(), HttpStatus.OK);
        Adopter newAdopter = newResult.getBody();
        assertNotNull(newAdopter);

        result = restClient.delete()
                .uri(locHdr)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void testDeleteAdopterWithBadId() {
        ResponseEntity<?> result = restClient.delete()
                .uri(oneAdopterUrl, 1000)
                .retrieve()
                .onStatus(code -> code == HttpStatus.NOT_FOUND, (req, resp) -> {
                })
                .toBodilessEntity();


        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testUpdateAdopterWithGoodId() {
        Adopter adopter = restClient.get()
                .uri(oneAdopterUrl, 1)
                .retrieve()
                .body(Adopter.class);

        adopter.setName("Firlan");

        ResponseEntity<?> result = restClient.put()
                .uri(rootUrl)
                .body(adopter)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        adopter = restClient.get()
                .uri(oneAdopterUrl, 1)
                .retrieve()
                .body(Adopter.class);

        assertEquals("Firlan", adopter.getName());
    }

    @Test
    public void testUpdateAdopterWithBadId() {
        Adopter adopter = restClient.get()
                .uri(oneAdopterUrl, 1)
                .retrieve()
                .body(Adopter.class);

        adopter.setName("Firlan");
        adopter.setId(1000);

        ResponseEntity<?> result = restClient.put()
                .uri(rootUrl)
                .body(adopter)
                .retrieve()
                .onStatus(st -> st == HttpStatus.NOT_FOUND, (req, resp) -> {})
                .toBodilessEntity();

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    }
}
