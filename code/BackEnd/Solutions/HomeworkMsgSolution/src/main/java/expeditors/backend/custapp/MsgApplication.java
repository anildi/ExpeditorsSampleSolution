package expeditors.backend.custapp;

import expeditors.backend.custapp.domain.Customer;
import expeditors.backend.custapp.domain.PhoneNumber;
import expeditors.backend.custapp.jpql.JPQLTester;
import expeditors.backend.custapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @author whynot
 */
//@SpringBootApplication
public class MsgApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsgApplication.class, args);
    }
}

@Component
class CustomerAppInit implements CommandLineRunner {

    @Autowired
    private CustomerService customerservice;

    @Override
    public void run(String... args) throws Exception {
        List.of(
                new Customer("Manoj-h2", LocalDate.parse("1956-08-15"),
                        Customer.Status.PRIVILEGED,
                        new PhoneNumber("222 333-4444", PhoneNumber.Type.MOBILE),
                        new PhoneNumber("222 333-5555", PhoneNumber.Type.WORK)),
                new Customer("Ana-h2", LocalDate.parse("1978-03-10"), Customer.Status.NORMAL,
                        new PhoneNumber("222 333-7900", PhoneNumber.Type.HOME)),
                new Customer("Roberta-h2", LocalDate.parse("2000-07-15"), Customer.Status.RESTRICTED,
                        new PhoneNumber("383 343-5879", PhoneNumber.Type.HOME)),
                new Customer("Madhu-h2", LocalDate.parse("1994-10-07"), Customer.Status.NORMAL,
                        new PhoneNumber("00345 598-8279 389", PhoneNumber.Type.SATELLITE),
                        new PhoneNumber("383 598-8279", PhoneNumber.Type.MOBILE))
        ).forEach(customerservice::addCustomer);

        List<Customer> customers = customerservice.getCustomers();

        System.out.println("customers: " + customers.size());
        customers.forEach(System.out::println);
    }
}
