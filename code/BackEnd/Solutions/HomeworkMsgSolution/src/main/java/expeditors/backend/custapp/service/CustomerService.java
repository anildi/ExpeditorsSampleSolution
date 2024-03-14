package expeditors.backend.custapp.service;



import expeditors.backend.custapp.domain.Customer;
import expeditors.backend.custapp.dao.CustomerDAO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author whynot
 */
public class CustomerService {

    private final int MIN_AGE = 18;

    private CustomerDAO customerDAO; // = DAOFactory.customerDAO(); //new InMemoryCustomerDAO();

    public Customer addCustomer(Customer c) {
        //Maybe do some validation on customer
        //Must be older than 18
        long age = c.getDob().until(LocalDate.now(), ChronoUnit.YEARS);
        if (age < 18) {
            throw new RuntimeException("Customer age must be at least " + MIN_AGE);
        }
        return customerDAO.insert(c);

    }

    public boolean deleteCustomer(int id) {
        return customerDAO.delete(id);
        //delete customer with id
    }

    public boolean updateCustomer(Customer c) {
        return customerDAO.update(c);

    }

    public Customer getCustomer(int id) {
        return customerDAO.find(id);
    }

    public List<Customer> getCustomers() {
        return customerDAO.findAll();
    }

    public List<Customer> findByExample(Map<String, Object> example) {
        Customer customer = new Customer();
        Predicate<Customer> predicate = null;
        for(var entry : example.entrySet()) {
                switch(entry.getKey()) {
                case "name" -> customer.setName((String)entry.getValue());
                case "dob" -> customer.setDob((LocalDate)entry.getValue());
                case "status" -> customer.setStatus((Customer.Status) entry.getValue());
            };
        }

        List<Customer> result = customerDAO.findByExample(customer);

        return result;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
}
