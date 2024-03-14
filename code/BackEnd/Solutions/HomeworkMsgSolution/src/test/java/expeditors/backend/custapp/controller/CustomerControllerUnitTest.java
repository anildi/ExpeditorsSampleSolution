package expeditors.backend.custapp.controller;

import expeditors.backend.custapp.domain.Customer;
import expeditors.backend.custapp.service.CustomerService;
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
public class CustomerControllerUnitTest {

    @Mock
    private UriCreator uriCreator;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private List<Customer> customers = List.of(
            new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL),
            new Customer("Francine", LocalDate.of(1980, 6, 9), Customer.Status.PRIVILEGED),
            new Customer("Lata", LocalDate.of(2000, 7, 9), Customer.Status.NORMAL),
            new Customer("Francine", LocalDate.of(1990, 6, 9), Customer.Status.RESTRICTED)
    );

    @Test
    public void testGetAll() {
        Mockito.when(customerService.getCustomers()).thenReturn(customers);

        ResponseEntity<?> result = customerController.getAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());

        Mockito.verify(customerService).getCustomers();
    }

    @Test
    public void testGetOneWithGoodId() {
        Mockito.when(customerService.getCustomer(1)).thenReturn(customers.get(0));

        ResponseEntity<?> result = customerController.getCustomer(1);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        Mockito.verify(customerService).getCustomer(1);
    }

    @Test
    public void testGetOneWithBadId() {
        Mockito.when(customerService.getCustomer(1000)).thenReturn(null);

        ResponseEntity<?> result = customerController.getCustomer(1000);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        Mockito.verify(customerService).getCustomer(1000);
    }

    @Test
    public void testPostCustomer() throws URISyntaxException {
        Customer c = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        c.setId(1);

        Mockito.when(customerService.addCustomer(c))
                .thenReturn(c);

        String uriStr = "http://localhost:8080/customer/" + c.getId();
        URI uri = new URI(uriStr);
        Mockito.when(uriCreator.getUriFor(1)).thenReturn(uri);

        ResponseEntity<?> result = customerController.addCustomer(c);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        String locHdr = result.getHeaders().get("Location").get(0);
        assertEquals(uriStr, locHdr);

        Mockito.verify(customerService).addCustomer(c);
        Mockito.verify(uriCreator).getUriFor(c.getId());
    }

    @Test
    public void testDeleteCustomerWithGoodId() {
        Mockito.when(customerService.deleteCustomer(1)).thenReturn(true);

        ResponseEntity<?> result = customerController.deleteCustomer(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        Mockito.verify(customerService).deleteCustomer(1);
    }

    @Test
    public void testDeleteCustomerWithBadId() {
        Mockito.when(customerService.deleteCustomer(1000)).thenReturn(false);

        ResponseEntity<?> result = customerController.deleteCustomer(1000);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        Mockito.verify(customerService).deleteCustomer(1000);
    }

    @Test
    public void testUpdateCustomerWithGoodId() {
        Mockito.when(customerService.updateCustomer(customers.get(0))).thenReturn(true);

        ResponseEntity<?> result = customerController.updateCustomer(customers.get(0));

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        Mockito.verify(customerService).updateCustomer(customers.get(0));
    }

    @Test
    public void testUpdateCustomerWithBadId() {
        Mockito.when(customerService.updateCustomer(customers.get(0))).thenReturn(false);

        ResponseEntity<?> result = customerController.updateCustomer(customers.get(0));

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        Mockito.verify(customerService).updateCustomer(customers.get(0));
    }
}
