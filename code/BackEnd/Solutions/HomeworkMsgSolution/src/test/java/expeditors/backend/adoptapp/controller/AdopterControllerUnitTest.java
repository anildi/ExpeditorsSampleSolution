package expeditors.backend.adoptapp.controller;

import expeditors.backend.adoptapp.domain.Adopter;
import expeditors.backend.adoptapp.domain.Pet;
import expeditors.backend.adoptapp.domain.PetType;
import expeditors.backend.adoptapp.service.AdopterService;
import expeditors.backend.utils.UriCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whynot
 */
@ExtendWith(MockitoExtension.class)
public class AdopterControllerUnitTest {

    @Mock
    private UriCreator uriCreator;

    @Mock
    private AdopterService adopterService;

    @InjectMocks
    private AdopterController adopterController;

    private List<Adopter> adopters = List.of(
            new Adopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                Pet.builder(PetType.DOG).name("woofie").breed("mixed").build()),

            new Adopter("Francine", "383 9339 9999 9393", LocalDate.of(2020, 5, 9),
                        Pet.builder(PetType.DOG).name("slinky").breed("dalmation").build()),

            new Adopter("Darlene", "4484 9339 77939", LocalDate.of(2020, 5, 9),
                        Pet.builder(PetType.TURTLE).name("swifty").build())
            );

    @Test
    public void testGetAll() {
        Mockito.when(adopterService.getAllAdopters()).thenReturn(adopters);

        ResponseEntity<?> result = adopterController.getAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());

        Mockito.verify(adopterService).getAllAdopters();
    }

    @Test
    public void testGetOneWithGoodId() {
        Mockito.when(adopterService.getAdopter(1)).thenReturn(adopters.get(0));

        ResponseEntity<?> result = adopterController.getAdopter(1);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        Mockito.verify(adopterService).getAdopter(1);
    }

    @Test
    public void testGetOneWithBadId() {
        Mockito.when(adopterService.getAdopter(1000)).thenReturn(null);

        ResponseEntity<?> result = adopterController.getAdopter(1000);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        Mockito.verify(adopterService).getAdopter(1000);
    }

    @Test
    public void testPostAdopter() throws URISyntaxException {
        Adopter c = new Adopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                        Pet.builder(PetType.DOG).name("woofie").breed("mixed").build());
        c.setId(1);

        Mockito.when(adopterService.addAdopter(c))
                .thenReturn(c);

        String uriStr = "http://localhost:8080/petservice" + c.getId();
        URI uri = new URI(uriStr);
        Mockito.when(uriCreator.getUriFor(1)).thenReturn(uri);

        ResponseEntity<?> result = adopterController.addAdopter(c);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        String locHdr = result.getHeaders().get("Location").get(0);
        assertEquals(uriStr, locHdr);

        Mockito.verify(adopterService).addAdopter(c);
        Mockito.verify(uriCreator).getUriFor(c.getId());
    }

    @Test
    public void testDeleteAdopterWithGoodId() {
        Mockito.when(adopterService.deleteAdopter(1)).thenReturn(true);

        ResponseEntity<?> result = adopterController.deleteAdopter(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        Mockito.verify(adopterService).deleteAdopter(1);
    }

    @Test
    public void testdeleteAdopterWithBadId() {
        Mockito.when(adopterService.deleteAdopter(1000)).thenReturn(false);

        ResponseEntity<?> result = adopterController.deleteAdopter(1000);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        Mockito.verify(adopterService).deleteAdopter(1000);
    }

    @Test
    public void testUpdateAdopterWithGoodId() {
        Mockito.when(adopterService.updateAdopter(adopters.get(0))).thenReturn(true);

        ResponseEntity<?> result = adopterController.updateAdopter(adopters.get(0));

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        Mockito.verify(adopterService).updateAdopter(adopters.get(0));
    }

    @Test
    public void testupdateAdopterWithBadId() {
        Mockito.when(adopterService.updateAdopter(adopters.get(0))).thenReturn(false);

        ResponseEntity<?> result = adopterController.updateAdopter(adopters.get(0));

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        Mockito.verify(adopterService).updateAdopter(adopters.get(0));
    }
}
