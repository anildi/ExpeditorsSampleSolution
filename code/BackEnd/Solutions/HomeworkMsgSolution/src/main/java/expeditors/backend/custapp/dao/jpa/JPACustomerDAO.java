package expeditors.backend.custapp.dao.jpa;



import expeditors.backend.custapp.dao.CustomerDAO;
import expeditors.backend.custapp.domain.Customer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/*/di/allcourses/ExpeditorsBootCamp/courseware/ttl/code/StudentWorkSolutions/src/main/java/expeditors/backend/custapp/week5/dao*
 * @author whynot
 */
public class JPACustomerDAO implements CustomerDAO {

    private Map<Integer, Customer> customers = new ConcurrentHashMap<>();
    private AtomicInteger nextId = new AtomicInteger(1);

    public Customer insert(Customer c) {
        int newId = nextId.getAndIncrement();
        c.setId(newId);
        c.setName(c.getName() + " from JPADAO");
        customers.put(newId, c);

        return c;
    }

    public boolean delete(int id) {
       return customers.remove(id) != null;
    }

    public boolean update(Customer c) {
       return customers.replace(c.getId(), c) != null;
    }

    public Customer find(int id) {
       return customers.get(id);
    }

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


        List<Customer> all = findAll();
        List<Customer> result = findAll()
                .stream()
                .filter(predicate)
                .toList();

        return result;
    }
}
