package expeditors.backend.custapp.dao.repo;

import expeditors.backend.adoptapp.domain.Adopter;
import expeditors.backend.custapp.domain.Customer;
import expeditors.backend.custapp.domain.CustomerDTO;
import expeditors.backend.custapp.jpql.JPQLTester;
import expeditors.backend.custapp.sql.ScriptFileProperties;
import expeditors.backend.custapp.sql.SqlScriptBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author whynot
 */

//@SpringBootTest
@DataJpaTest(properties = {"spring.profiles.include=jpqltester"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ScriptFileProperties.class, JPQLTester.class})
public class TestJPQLTester extends SqlScriptBase {
    @Autowired
    private CustomerRepo customerDAO;
    
    @PersistenceUnit
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ScriptFileProperties props;

    @Autowired
    private JPQLTester tester;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    public void init() throws SQLException {
    }

    @Test
    public void testGetwithTester() {
        String jpqlStr = "select c from Customer c where id = :id";
        Map<String, Object> params = Map.of("id", 1);
        List<Customer> result = tester.executeQuery(Customer.class, jpqlStr, params);
        assertEquals(1, result.size());

        Customer cust = result.get(0);
        System.out.println("cust: " + cust);
        assertTrue(cust.getName().contains("Manoj"));
    }

    @Test
    public void testGetwithTesterWithPositionalArgs() {
        String jpqlStr = "select c from Customer c where id = ?1";
        Map<String, Object> params = Map.of();
        List<Customer> result = tester.executeQuery(Customer.class, jpqlStr, 1);
        assertEquals(1, result.size());

        Customer cust = result.get(0);
        System.out.println("cust: " + cust);
        assertTrue(cust.getName().contains("Manoj"));
    }


    @Test
    public void testJPQLLessSimple() {
        //Fetch Customers who match a particular Phone Number
        //We do this with a subselect:
        //  Select customers and their phones for all
        //    customers where they have a phone which matches the one we
        //    are looking for.

        String phoneNumber = "222 333-5555";
        String jpqlStr = " select " +
                "new expeditors.backend.custapp.domain.CustomerDTO(c.id, " +
                "c.name, c.dob, c.status) " +
                "from Customer c ";

        List<CustomerDTO> result =
                tester.executeQuery(CustomerDTO.class, jpqlStr);

        result.forEach(System.out::println);
    }

    @Test
    public void testJPQLEvenLessSimple() {
        //Fetch Customers who match a particular Phone Number
        //We do this with a subselect:
        //  Select customers and their phones for all
        //    customers where they have a phone which matches the one we
        //    are looking for.

        String phoneNumber = "222 333-5555";
//        String jpqlStr = """
//                select c from Customer c
//                left join fetch c.phoneNumbers p where
//                c.id in
//                (select p.customer.id from PhoneNumber p where p.phoneNumber in :phones)
//                """;
        String jpqlStr = """
                select c from Customer c 
                left join c.phoneNumbers p where
                p.phoneNumber in :phones
                """;

        List<Customer> result =
                tester.executeQuery(Customer.class, jpqlStr, Map.of("phones", List.of(phoneNumber, "383 598-8279")));

        result.forEach(c -> System.out.println(c.toLongString()));
    }

    @Test
    public void testAdopterDistinct() {
        String jpqlStr = "select a from Adopter a left join fetch a.pets";
        List<Adopter> result = tester.executeQuery(Adopter.class, jpqlStr);
        result.forEach(System.out::println);
    }

    @Test
    public void testAdopterDistinctWithObjects() {
        String jpqlStr = "select distinct a from Adopter a left join fetch a.pets";
        List<Object[]> result = em.createQuery(jpqlStr, Object[].class).getResultList();
        result.forEach(System.out::println);
    }
}
