package expeditors.backend.adoptapp.dao.repo;

import expeditors.backend.adoptapp.dao.repository.BigAdopterRepo;
import expeditors.backend.adoptapp.domain.BigAdopter;
import expeditors.backend.adoptapp.domain.PetType;
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
public class TestBigAdopterRepo {

    @Autowired
    private BigAdopterRepo adopterDAO;

    @Test
    public void testGetAll() {
        List<BigAdopter> adopters = adopterDAO.findAll();
        int oldCount = adopters.size();

        BigAdopter adopter = new BigAdopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                PetType.DOG, "woofie", null);
        adopterDAO.save(adopter);

        adopters = adopterDAO.findAll();
        assertEquals(oldCount + 1, adopters.size());
    }

    @Test
    public void testGetAdopterWithExistingId() {

        BigAdopter adopter = new BigAdopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                PetType.DOG, "woofie", null);
        assertEquals(0, adopter.getId());
        adopterDAO.save(adopter);

        adopter = adopterDAO.findById(1).orElse(null);
        assertNotNull(adopter);
    }

    @Test
    public void testGetAdopterWithNonExistingId() {

        BigAdopter adopter = adopterDAO.findById(1000).orElse(null);
        assertNull(adopter);
    }

    @Test
    public void testInsert() {
        int oldCount = adopterDAO.findAll().size();

        BigAdopter adopter = new BigAdopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                PetType.DOG, "woofie", null);
        assertEquals(0, adopter.getId());
        adopter = adopterDAO.save(adopter);

        int newCount = adopterDAO.findAll().size();
        assertEquals(oldCount + 1, newCount);
    }

    @Test
    public void testDelete() {
        int testId = 1;
        BigAdopter adopter = new BigAdopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                PetType.DOG, "woofie", null);
        adopter = adopterDAO.save(adopter);

        BigAdopter a = adopterDAO.findById(testId).orElse(null);
        assertNotNull(a);

        adopterDAO.delete(a);

        a = adopterDAO.findById(testId).orElse(null);
        assertNull(a);
    }

    @Test
    public void testUpdateExistingAdopter() {
        int testId = 1;
        BigAdopter adopter = new BigAdopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                PetType.DOG, "woofie", null);
        adopter = adopterDAO.save(adopter);

        BigAdopter a = adopterDAO.findById(testId).orElse(null);
        assertNotNull(a);

        String newName = "Martha";
        a.setName(newName);

        BigAdopter result = adopterDAO.save(a);

        a = adopterDAO.findById(testId).orElse(null);
        assertEquals(newName, a.getName());
    }
}
