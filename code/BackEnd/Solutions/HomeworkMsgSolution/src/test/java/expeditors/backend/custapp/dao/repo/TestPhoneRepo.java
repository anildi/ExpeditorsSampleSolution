package expeditors.backend.custapp.dao.repo;

import expeditors.backend.custapp.domain.PhoneNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author whynot
 */
@SpringBootTest
public class TestPhoneRepo {

    @Autowired
    private PhoneRepo phoneRepo;

    @Test
    public void testGetAllPhones() {
        List<PhoneNumber> phones = phoneRepo.findAll();
        phones.forEach(p -> System.out.println(p));
    }
}
