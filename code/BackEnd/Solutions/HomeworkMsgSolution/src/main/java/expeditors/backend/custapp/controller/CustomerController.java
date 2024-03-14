package expeditors.backend.custapp.controller;

import expeditors.backend.custapp.domain.Customer;
import expeditors.backend.custapp.service.CustomerService;
import expeditors.backend.utils.UriCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @author whynot
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;
    private UriCreator uriCreator;

    public CustomerController(CustomerService customerService,
                              UriCreator uriCreator) {
        this.customerService = customerService;
        this.uriCreator = uriCreator;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Customer> customers = customerService.getCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id:\\d+}")   //id of only digits
    public ResponseEntity<?> getCustomer(@PathVariable("id") int id) {
        Customer customer = customerService.getCustomer(id);
        if(customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No customer with id: " + id);
        }
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
        Customer newCustomer = customerService.addCustomer(customer);

        URI uri = uriCreator.getUriFor(newCustomer.getId());

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") int id) {
        boolean result = customerService.deleteCustomer(id);
        if(!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No customer with id: " + id);
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        boolean result = customerService.updateCustomer(customer);
        if(!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No customer with id: " + customer.getId());
        }

        return ResponseEntity.noContent().build();
    }
}
