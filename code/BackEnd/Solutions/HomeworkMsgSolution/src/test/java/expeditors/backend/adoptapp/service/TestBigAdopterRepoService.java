package expeditors.backend.adoptapp.service;

import expeditors.backend.adoptapp.domain.BigAdopter;
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
public class TestBigAdopterRepoService {

    @Autowired
    private BigAdopterRepoService adopterService;

    @BeforeEach
    public void init() {
//        DAOFactory.clear();
//        adopterService = DAOFactory.adopterService();
    }

    @Test
    public void testGetAll() {

        List<BigAdopter> adopters = adopterService.getAllAdopters();
        int oldCount = adopters.size();

        BigAdopter adopter = new BigAdopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                PetType.DOG, "woofie", null);
        adopterService.addAdopter(adopter);


        adopters = adopterService.getAllAdopters();
        assertEquals(oldCount + 1, adopters.size());

        adopters.forEach(System.out::println);
    }

    @Test
    public void testGetOneWithGoodId() {

        List<BigAdopter> adopters = adopterService.getAllAdopters();
        BigAdopter adopter = new BigAdopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                PetType.DOG, "woofie", null);
        adopterService.addAdopter(adopter);

        BigAdopter a = adopterService.getAdopter(1);
        assertEquals(1, a.getId());
    }

    @Test
    public void testGetOneWithNonExistentId() {
        BigAdopter adopter = adopterService.getAdopter(1000);
        assertNull(adopter);
    }

    @Test
    public void testDeleteWithExistingAdopter() {

        BigAdopter adopter = new BigAdopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                PetType.DOG, "woofie", null);
        BigAdopter newAdopter = adopterService.addAdopter(adopter);

        int oldCount = adopterService.getAllAdopters().size();

        boolean result = adopterService.deleteAdopter(newAdopter.getId());
        assertTrue(result);

        int newCount = adopterService.getAllAdopters().size();
        assertEquals(oldCount - 1, newCount);
    }


    @Test
    public void testUpdateWithExistingAdopter() {

        BigAdopter adopter = new BigAdopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                PetType.DOG, "woofie", null);
        BigAdopter newAdopter = adopterService.addAdopter(adopter);

        assertTrue(newAdopter.getName().contains("Joey"));

        newAdopter.setName("Martha");

        boolean result = adopterService.updateAdopter(newAdopter);
        assertTrue(result);

        newAdopter = adopterService.getAdopter(newAdopter.getId());
        assertEquals("Martha", newAdopter.getName());
    }

    @Test
    public void testGetByPetType() {
        List<BigAdopter> result = adopterService.getAdoptersByPetType(PetType.TURTLE);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().contains("Darlene"));

        result = adopterService.getAdoptersByPetType(PetType.CAT);

        assertEquals(0, result.size());
    }
}
