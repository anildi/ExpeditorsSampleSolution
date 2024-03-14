package expeditors.backend.adoptapp.service;

import expeditors.backend.adoptapp.domain.Adopter;
import expeditors.backend.adoptapp.domain.Pet;
import expeditors.backend.adoptapp.domain.PetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author whynot
 */
@SpringBootTest
@Transactional
public class TestAdopterRepoService {

    @Autowired
    private AdopterRepoService adopterService;

    @BeforeEach
    public void init() {
//        DAOFactory.clear();
//        adopterService = DAOFactory.adopterService();
    }

    @Test
    public void testGetAll() {

        List<Adopter> adopters = adopterService.getAllAdopters();
        int oldCount = adopters.size();

        Adopter adopter = new Adopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                Pet.builder(PetType.DOG).name("woofie").build());
        adopterService.addAdopter(adopter);


        adopters = adopterService.getAllAdopters();
        assertEquals(oldCount + 1, adopters.size());

        adopters.forEach(System.out::println);
    }

    @Test
    public void testGetOneWithGoodId() {

        List<Adopter> adopters = adopterService.getAllAdopters();
        Adopter adopter = new Adopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                Pet.builder(PetType.DOG).name("woofie").build());
        adopterService.addAdopter(adopter);

        Adopter a = adopterService.getAdopter(1);
        assertEquals(1, a.getId());
    }

    @Test
    public void testGetOneWithNonExistentId() {
        Adopter adopter = adopterService.getAdopter(1000);
        assertNull(adopter);
    }

    @Test
    public void testDeleteWithExistingAdopter() {

        Adopter adopter = new Adopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                Pet.builder(PetType.DOG).name("woofie").build());
        Adopter newAdopter = adopterService.addAdopter(adopter);

        Adopter foundAdopter = adopterService.getAdopter(newAdopter.getId());
        assertNotNull(foundAdopter);

        boolean result = adopterService.deleteAdopter(newAdopter.getId());
        assertTrue(result);

        foundAdopter = adopterService.getAdopter(newAdopter.getId());
        assertNull(foundAdopter);
    }


    @Test
    public void testUpdateWithExistingAdopter() {

        Adopter adopter = new Adopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                Pet.builder(PetType.DOG).name("woofie").build());
        Adopter newAdopter = adopterService.addAdopter(adopter);

        assertTrue(newAdopter.getName().contains("Joey"));

        newAdopter.setName("Martha");

        boolean result = adopterService.updateAdopter(newAdopter);
        assertTrue(result);

        newAdopter = adopterService.getAdopter(newAdopter.getId());
        assertEquals("Martha", newAdopter.getName());
    }

    @Test
    public void testAddAdopterAndPet() {
        Adopter adopter = new Adopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                Pet.builder(PetType.DOG).name("woofie").build());
        Adopter newAdopter = adopterService.addAdopter(adopter);

    }

    @Test
    public void testGetByPetType() {
        List<Adopter> result = adopterService.getAdoptersByPetType(PetType.TURTLE);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().contains("Darlene"));

        result = adopterService.getAdoptersByPetType(PetType.CAT);

        assertEquals(0, result.size());
    }
}
