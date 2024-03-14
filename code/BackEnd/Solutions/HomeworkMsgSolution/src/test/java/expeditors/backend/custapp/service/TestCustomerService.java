package expeditors.backend.custapp.service;

import expeditors.backend.custapp.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whynot
 */
@SpringBootTest
public class TestCustomerService {

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    public void init() {
//        customerService = new CustomerService();
    }

    @Test
    public void testGetAll() {

        List<Customer> customers = customerService.getCustomers();
        int oldSize = customers.size();

        Customer customer = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        customerService.addCustomer(customer);

        customers = customerService.getCustomers();
        assertEquals(oldSize + 1, customers.size());

        System.out.println("TestGetall:");
        customers.forEach(System.out::println);
    }

    @Test
    public void testGetOneWithGoodId() {

        List<Customer> customers = customerService.getCustomers();
        Customer customer = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        customerService.addCustomer(customer);

        customer = customerService.getCustomer(1);
        assertEquals(1, customer.getId());
    }

    @Test
    public void testGetOneWithNonExistentId() {

        List<Customer> customers = customerService.getCustomers();
        Customer customer = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        customerService.addCustomer(customer);

        customer = customerService.getCustomer(1000);
        assertNull(customer);
    }

    @Test
    public void createWithGoodAge() {
        Customer customer = new Customer("JoeyWithGoodAge", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        customerService.addCustomer(customer);

        customer = customerService.getCustomer(1);
        assertNotNull(customer);
    }

    @Test
    public void createWithAgeTooYoung() {
        assertThrows(RuntimeException.class, () -> {
            Customer customer = new Customer("JoeywithAgeTooYoung", LocalDate.of(2010, 6, 9), Customer.Status.NORMAL);
            customerService.addCustomer(customer);
        });
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testDeleteWithExistingCustomer() {

        Customer customer = new Customer("JoeyDeleteExisting", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        Customer newCustomer = customerService.addCustomer(customer);

        boolean result = customerService.deleteCustomer(newCustomer.getId());
        assertTrue(result);

        customer = customerService.getCustomer(newCustomer.getId());
        assertNull(customer);
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

        customer = customerService.getCustomer(newCustomer.getId());
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
    public void testFindByExample() {
//        List<Customer> customers = List.of(
//                new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL),
//                new Customer("Francine", LocalDate.of(1980, 6, 9), Customer.Status.PRIVILEGED),
//                new Customer("Lata", LocalDate.of(2000, 7, 9), Customer.Status.NORMAL),
//                new Customer("Francine", LocalDate.of(1990, 6, 9), Customer.Status.RESTRICTED)
//        );
//        customers.forEach(c -> customerService.addCustomer(c));

        Map<String, Object> props = Map.of("name", "Manoj",
                "status", Customer.Status.RESTRICTED,
                "dob", LocalDate.of(1994, 10, 7));
        Customer example = new Customer("Manoj", LocalDate.of(1994, 10, 7), Customer.Status.RESTRICTED);
        List<Customer> foundCustomers = customerService.findByExample(props);

        System.out.println(foundCustomers);
        assertEquals(3, foundCustomers.size());
    }
}
