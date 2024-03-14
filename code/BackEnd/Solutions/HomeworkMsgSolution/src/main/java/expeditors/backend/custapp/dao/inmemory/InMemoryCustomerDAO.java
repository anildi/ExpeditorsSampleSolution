package expeditors.backend.custapp.dao.inmemory;



import expeditors.backend.custapp.dao.CustomerDAO;
import expeditors.backend.custapp.domain.Customer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author whynot
 */
public class InMemoryCustomerDAO implements CustomerDAO {

    private Map<Integer, Customer> customers = new ConcurrentHashMap<>();
    private AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Customer insert(Customer c) {
        int newId = nextId.getAndIncrement();
        c.setId(newId);
        customers.put(newId, c);

        return c;
    }

    @Override
    public boolean delete(int id) {
        return customers.remove(id) != null;
    }

    @Override
    public boolean update(Customer c) {
        return customers.replace(c.getId(), c) != null;
    }

    @Override
    public Customer find(int id) {
        return customers.get(id);
    }

    @Override
    public List<Customer> findAll() {
        return List.copyOf(customers.values());
    }

    @Override
    public List<Customer> findByExample(Customer example) {

        Predicate<Customer> predicate = null;
        if (example.getName() != null) {
            Predicate<Customer> pr = c -> c.getName().contains(example.getName());
            predicate = predicate == null ? pr : predicate.or(pr);
        }
        if (example.getDob() != null) {
            Predicate<Customer> pr = c -> c.getDob().equals(example.getDob());
            predicate = predicate == null ? pr : predicate.or(pr);
        }
        if (example.getStatus() != null) {
            Predicate<Customer> pr = c -> c.getStatus().equals(example.getStatus());
            predicate = predicate == null ? pr : predicate.or(pr);
        }

        predicate = predicate == null ? (t ->true) : predicate;


        List<Customer> result = findAll()
                .stream()
                .filter(predicate)
                .toList();

        return result;
    }
}
