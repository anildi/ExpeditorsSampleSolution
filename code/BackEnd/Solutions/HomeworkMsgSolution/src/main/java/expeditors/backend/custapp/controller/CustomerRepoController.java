package expeditors.backend.custapp.controller;

import expeditors.backend.custapp.domain.Customer;
import expeditors.backend.custapp.domain.PhoneNumber;
import expeditors.backend.custapp.service.CustomerRepoService;
import expeditors.backend.utils.UriCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author whynot
 */
@RestController
@RequestMapping("/customerrepo")
public class CustomerRepoController {

    private CustomerRepoService customerService;
    private UriCreator uriCreator;

    public CustomerRepoController(CustomerRepoService customerService,
                                  UriCreator uriCreator) {
        this.customerService = customerService;
        this.uriCreator = uriCreator;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(name = "phones", defaultValue = "false") boolean phones) {
        List<?> customers = phones ?
                customerService.getAllCustNameWithPhonesDTO() :
                customerService.getCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/findBy")
    public ResponseEntity<?> getCustomersBy(@RequestParam Map<String, Object> params) {
        List<Customer> result = customerService.getCustomersBy(params);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id:\\d+}")   //id of only digits
    public ResponseEntity<?> getCustomer(@PathVariable("id") int id,
                                         @RequestParam(name = "phones", defaultValue = "false") boolean phones) {
        Object customer = phones ?
                customerService.getCustomerWithPhones(id) :
                customerService.getCustomer(id);
        if (customer == null) {
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
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No customer with id: " + id);
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        boolean result = customerService.updateCustomer(customer);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No customer with id: " + customer.getId());
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Add a phone number to an existing Customer
     * @param custId
     * @param phoneNumber
     * @param type
     * @return
     */
    @PostMapping("/{cust_id}/{phone_number}/{phone_type}")
    public ResponseEntity<?> addPhoneNumber(@PathVariable("cust_id") int custId,
                                            @PathVariable("phone_number") String phoneNumber,
                                            @PathVariable("phone_type") PhoneNumber.Type type) {
        Customer customer = customerService.getCustomerWithPhones(custId);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No customer with id: " + custId);
        }
        PhoneNumber pn = new PhoneNumber(phoneNumber, type);
        customer.addPhoneNumber(pn);

        return updateCustomer(customer);
    }

    /**
     * Delete a phone number from an existing customer
     * @param custId
     * @param phoneNumber
     * @param type
     * @return
     */
    @DeleteMapping("/{cust_id}/{phone_number}/{phone_type}")
    public ResponseEntity<?> deletePhoneNumber(@PathVariable("cust_id") int custId,
                                            @PathVariable("phone_number") String phoneNumber,
                                            @PathVariable("phone_type") PhoneNumber.Type type) {
        Customer customer = customerService.getCustomerWithPhones(custId);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No customer with id: " + custId);
        }
        PhoneNumber pn = new PhoneNumber(phoneNumber, type);
        customer.deletePhoneNumber(pn);

        return updateCustomer(customer);
    }
}
