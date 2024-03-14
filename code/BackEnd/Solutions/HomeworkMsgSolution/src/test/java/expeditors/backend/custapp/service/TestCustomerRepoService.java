package expeditors.backend.custapp.service;

import expeditors.backend.custapp.domain.*;
import expeditors.backend.custapp.sql.ScriptFileProperties;
import expeditors.backend.custapp.sql.SqlScriptBase;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author whynot
 */
@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class TestCustomerRepoService extends SqlScriptBase {

    @Autowired
    private CustomerRepoService customerService;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ScriptFileProperties props;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    public void init() throws SQLException {
    }


    @Test
    public void testGetAll() {

        List<CustomerDTO> customers = customerService.getCustomers();
        int oldSize = customers.size();

        Customer customer = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        customerService.addCustomer(customer);

        customers = customerService.getCustomers();
        assertEquals(oldSize + 1, customers.size());

        System.out.println("TestGetall:");
        customers.forEach(System.out::println);
    }

    /**
     * More fun and games with transactions.
     *
     * If we don't turn off the Transaction here, then this
     * method runs in the @Transactional default transaction.
     *
     * In the middle of this method, we are calling
     * 'customerService.getAllCustNameWithPhonesDTO', which is
     * using JPA directly.  If that method creates its own
     * EntityManager, then it does its work in its own Transaction.  Which
     * means that it will not see the newly added Customer, since
     * the @Transactional transaction has not committed yet.
     *
     * Two ways out:
     * 1) Run this method without a Transaction, i.e.
     *  @Transactional(propagation = Propagation.NEVER).
     *  Which means that each interaction with the database
     *  in this method is committed immediately (auto_commit = true).
     *  So the other method will see the committed add when it does
     *  a select.
     *
     *  2) Use the default transaction here, and have the
     * 'customerService.getAllCustNameWithPhonesDTO' method
     *  use an injected @PersistencContext EntityManger rather
     *  than create its own.  That will be  he container's EntityManger,
     *  and so will be part of the default Transaction.  (But then
     *  we can't do commits or flushes on it)
     *
     *
     *
     */
    @Test
//    @Transactional(propagation = Propagation.NEVER)
    public void testGetAllWithPhones() {

//        EntityManager manager = emf.createEntityManager();
//        manager.getTransaction().begin();
        List<CustNameWithPhonesDTO> customers = customerService.getAllCustNameWithPhonesDTO();
        int oldSize = customers.size();

        Customer customer = new Customer("Blech", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        customerService.addCustomer(customer);
//        manager.flush();
//        manager.getTransaction().commit();
//        manager.close();

        customers = customerService.getAllCustNameWithPhonesDTO();
        assertEquals(oldSize + 1, customers.size());

        System.out.println("TestGetall:");
        customers.forEach(System.out::println);
    }

    @Test
    public void testGetOneWithGoodId() {

        Customer customer = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        customerService.addCustomer(customer);

        CustomerDTO customerDTO = customerService.getCustomer(customer.getId());
        assertNotNull(customer);
    }

    @Test
    public void testGetOneWithNonExistentId() {

        Customer customer = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        customerService.addCustomer(customer);

        CustomerDTO customerDTO = customerService.getCustomer(1000);
        assertNull(customerDTO);
    }

    @Test
    public void createWithGoodAge() {
        Customer customer = new Customer("JoeyWithGoodAge",
                LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        customerService.addCustomer(customer);

        CustomerDTO customerDTO = customerService.getCustomer(customer.getId());
        assertNotNull(customer);
    }

    @Test
    public void testCreateWithAgeTooYoung() {
        assertThrows(RuntimeException.class, () -> {
            Customer customer = new Customer("JoeywithAgeTooYoung", LocalDate.of(2010, 6, 9), Customer.Status.NORMAL);
            customerService.addCustomer(customer);
        });
    }

    @Test
    public void testDeleteWithExistingCustomer() {

        Customer customer = new Customer("JoeyDeleteExisting", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        Customer newCustomer = customerService.addCustomer(customer);

        boolean result = customerService.deleteCustomer(newCustomer.getId());
        assertTrue(result);

        CustomerDTO customerDTO = customerService.getCustomer(newCustomer.getId());
        assertNull(customerDTO);
    }

    @Test
    public void testDeleteNonExistingCustomer() {

        Customer customer = new Customer("JoeyDeleteNonExisting", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        Customer newCustomer = customerService.addCustomer(customer);

        boolean result = customerService.deleteCustomer(1000);
        assertFalse(result);
    }

    @Test
    public void testUpdateWithExistingCustomer() {

        Customer customer = new Customer("JoeyUpdateExisting", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        Customer newCustomer = customerService.addCustomer(customer);

        assertTrue(newCustomer.getName().contains("Joey"));

        newCustomer.setName("Martha");

        boolean result = customerService.updateCustomer(newCustomer);
        assertTrue(result);

        CustomerDTO customerDTO = customerService.getCustomer(newCustomer.getId());
        assertEquals("Martha", customer.getName());
    }

    @Test
    public void testUpdateNonExistingCustomer() {

        Customer customer = new Customer("JoeyNonExisting", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        Customer newCustomer = customerService.addCustomer(customer);

        assertTrue(newCustomer.getName().contains("Joey"));

        newCustomer.setName("Martha");
        newCustomer.setId(1000);

        boolean result = customerService.updateCustomer(newCustomer);
        assertFalse(result);
    }

    @Test
//    @Transactional(propagation = Propagation.NEVER)
    public void testFindByExample() {
//        List<Customer> customers = List.of(
//                new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL),
//                new Customer("Francine", LocalDate.of(1980, 6, 9), Customer.Status.PRIVILEGED),
//                new Customer("Lata", LocalDate.of(2000, 7, 9), Customer.Status.NORMAL),
//                new Customer("Francine", LocalDate.of(1990, 6, 9), Customer.Status.RESTRICTED)
//        );
//        customers.forEach(c -> customerService.addCustomer(c));

        Map<String, Object> props = Map.of("name", "Manoj",
                "status", "RESTRICTED",
                "dob", "1978-03-10");
        List<Customer> foundCustomers = customerService.getCustomersBy(props);

        foundCustomers.forEach(c -> System.out.println(c.toLongString()));

        assertEquals(3, foundCustomers.size());
    }

    /**
     * Test the getAllCustomersWithPhones method which we have
     * implemented in JPARepo and JPARepoImpl.
     */
    @Test
    public void testGetAllCustWithPhones() {
        List<CustNameWithPhonesDTO> result = customerService.getAllCustNameWithPhonesDTO();

        System.out.println("object class: " + result.get(0).getClass());

        assertEquals(4, result.size());
        assertEquals(2, result.get(0).getPhoneNumbers().size());
    }

    /**
     * Test CustNameWithPhones for one phone
     */
    @Test
    public void testSingleCustWithPhones() {
        List<CustNameWithPhonesDTO> result = customerService.
                getCustNameWithPhonesDTO("222 333-5555");

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getPhoneNumbers().size());
    }

    /**
     *  Here we are testing the method which uses
     */
    @Test
    public void testGetAllCustWithPhoneProjection() {
        List<CustWithPhoneProjection> result = customerService.getAllCustomersWithPhoneUsingProjection();

        System.out.println("object class: " + result.get(0).getClass());

        assertEquals(4, result.size());
        assertEquals(2, result.get(0).getPhoneNumbers().size());
    }

    @Test
//    @Rollback(false)
    public void testGetAddPhoneToExistingCustomer() {
        Customer customer = customerService.getCustomerWithPhones(1);

        assertEquals(2, customer.getPhoneNumbers().size());

        PhoneNumber pn = new PhoneNumber("876 5432-4394", PhoneNumber.Type.SATELLITE);
        customer.addPhoneNumber(pn);

        assertEquals(3, customer.getPhoneNumbers().size());
    }
}
