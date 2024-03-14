package expeditors.backend.custapp.dao.repo;

import expeditors.backend.custapp.domain.*;
import expeditors.backend.custapp.sql.ScriptFileProperties;
import jakarta.persistence.*;
import org.hibernate.query.ResultListTransformer;
import org.hibernate.query.TupleTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author whynot
 */
@SpringBootTest
@Transactional
public class TestCustomerRepo {

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
    private ApplicationContext context;

    @BeforeEach
    public void init() throws SQLException {
//        customerDAO = new JPACustomerDAO();
        try (Connection conn = dataSource.getConnection()) {
            props.getAllSchemaLocs().forEach(schemaFile -> {
                System.err.println("Running schemaFile: " + schemaFile);
                ScriptUtils.executeSqlScript(conn, context.getResource(schemaFile));
            });

            props.getAllDataLocs().forEach(dataFile -> {
                System.err.println("Running dataFile: " + dataFile);
                ScriptUtils.executeSqlScript(conn, context.getResource(dataFile));
            });
        }
    }

    @Test
    public void testCustWithPhoneWithEntityManager() {
        EntityManager manager = emf.createEntityManager();
        String jpqlStr = """
                   select c.id, c.name , p
                   from Customer c 
                   join c.phoneNumbers p
                   order by c.id
                """;

//        Query query = manager.createQuery(jpqlStr);
//        org.hibernate.query.Query<CustWithPhone> unwrapped =
//                query.unwrap(org.hibernate.query.Query.class)
//                        .setTupleTransformer(new CustWithPhoneTransformer());
//
//        List<CustWithPhone> result = unwrapped.getResultList();
        var transformer = new CustWithPhoneTransformer();
        TypedQuery<CustNameWithPhonesDTO> query =
                manager.createQuery(jpqlStr, CustNameWithPhonesDTO.class)
                        .unwrap(org.hibernate.query.Query.class)
                        .setTupleTransformer(transformer)
                        .setResultListTransformer(transformer);

        List<CustNameWithPhonesDTO> result = query.getResultList();

        System.out.println("Result: " + result);
    }

    class CustWithPhoneTransformer implements TupleTransformer<CustNameWithPhonesDTO>,
            ResultListTransformer<CustNameWithPhonesDTO> {

        Map<Integer, CustNameWithPhonesDTO> cWithP = new HashMap<>();

        @Override
        public CustNameWithPhonesDTO transformTuple(Object[] tuple, String[] aliases) {

            int id = (int) tuple[0];
            String name = (String) tuple[1];
            PhoneNumber phoneNumber = (PhoneNumber) tuple[2];
            CustNameWithPhonesDTO cwp = cWithP.computeIfAbsent(id, i ->
                    new CustNameWithPhonesDTO(name)
            );
            cwp.getPhoneNumbers().add(phoneNumber);

            return cwp;
        }

        @Override
        public List<CustNameWithPhonesDTO> transformList(List<CustNameWithPhonesDTO> resultList) {
            return new ArrayList<>(cWithP.values());
        }
    }

    @Test
    public void testCustWithPhoneUsingJPARepo() {
        List<CustNameWithPhonesDTO> result = customerDAO.findAllCustNameWithPhones();

        result.forEach(System.out::println);

        assertEquals(4, result.size());

        assertEquals(2, result.get(0).getPhoneNumbers().size());
    }

    @Test
    public void testFindByIdForCustomerDTO() {
        CustomerDTO onlyCust = customerDAO.findOnlyCustomer(1).orElse(null);

        assertNotNull(onlyCust);
        assertTrue(onlyCust.name().contains("Manoj"));
    }



    @Test
    public void testGetSmallWithProjection() {
        List<CustWithPhoneProjection> result = customerDAO.findAllCustWithPhoneProjectionBy();

        result.forEach(cp -> System.out.println(cp));
//        System.out.println("custWithProjection: " + result);

        assertEquals(4, result.size());
    }

    @Test
    public void testGetAllWithPhoneNumbers() {
        List<Customer> customers = customerDAO.findWithPhoneNumbers();
        System.out.println("customer: " + customers);
    }

    @Test
    public void testGetAll() {
        List<Customer> customers = customerDAO.findAll();
        int oldSize = customers.size();
        System.out.println("customer: " + customers);

        Customer customer = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        customerDAO.save(customer);

        customers = customerDAO.findAll();
        assertEquals(oldSize + 1, customers.size());
    }

    @Test
    public void testGetCustomerWithExistingId() {

        Customer customer = new Customer("Joey", LocalDate.of(1960, 6, 9),
                Customer.Status.NORMAL);
        assertEquals(0, customer.getId());
        customerDAO.save(customer);

        CustomerDTO customerDTO = customerDAO.findOnlyCustomer(1).orElse(null);
        assertNotNull(customer);
    }

    @Test
    public void testGetCustomerWithNonExistingId() {

        Customer customer = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        assertEquals(0, customer.getId());
        customerDAO.save(customer);

        CustomerDTO customerDTO = customerDAO.findOnlyCustomer(1000).orElse(null);
        assertNull(customerDTO);
    }

    @Test
    public void testInsert() {
        int maxId = customerDAO.findAll()
                .stream()
                .mapToInt(Customer::getId)
                .max().orElse(0);

        Customer customer = new Customer("Joey", LocalDate.of(1960, 6, 9),
                Customer.Status.NORMAL,
                new PhoneNumber("383 939 9393", PhoneNumber.Type.MOBILE));
        Customer insertedCustomer = customerDAO.save(customer);

        assertTrue(insertedCustomer.getId() >= maxId + 1);
    }

    @Test
    public void testDelete() {
        int testId = 1;
        Customer customer1 = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        customerDAO.save(customer1);

        CustomerDTO c = customerDAO.findOnlyCustomer(customer1.getId()).orElse(null);
        assertNotNull(c);

        customerDAO.delete(customer1);

        c = customerDAO.findOnlyCustomer(customer1.getId()).orElse(null);
        assertNull(c);
    }

    @Test
    public void testUpdateExistingCustomer() {
        Customer customer1 = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        Customer insertedCustomer = customerDAO.save(customer1);

        CustomerDTO c = customerDAO.findOnlyCustomer(insertedCustomer.getId()).orElse(null);
        assertNotNull(c);

        //Update the customer
        String newName = "Martha";
        insertedCustomer.setName(newName);
        customerDAO.save(insertedCustomer);

        c = customerDAO.findOnlyCustomer(insertedCustomer.getId()).orElse(null);
        assertEquals(newName, c.name());
    }

    @Test
    public void testAddPhonesToCustomer() {
        Customer customer1 = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);
        Customer insertedCustomer = customerDAO.save(customer1);

        PhoneNumber pn = new PhoneNumber("383 9393 9393", PhoneNumber.Type.SATELLITE);
        insertedCustomer.addPhoneNumber(pn);

        Customer custWithPhone = customerDAO.findCustomerWithPhoneNumberById(insertedCustomer.getId()).orElse(null);
        assertNotNull(custWithPhone);

        assertEquals(1, custWithPhone.getPhoneNumbers().size());

    }

    @Test
    public void testAddPhonesTo_New_CustomerWithEntityManager() {
        EntityManager manager = emf.createEntityManager();
        manager.setFlushMode(FlushModeType.COMMIT);

        Customer customer1 = new Customer("Joey", LocalDate.of(1960, 6, 9), Customer.Status.NORMAL);

        PhoneNumber pn = new PhoneNumber("383 9393 9393", PhoneNumber.Type.SATELLITE);
        customer1.addPhoneNumber(pn);
        manager.getTransaction().begin();
        manager.persist(customer1);
        manager.getTransaction().commit();
        manager.close();

        manager = emf.createEntityManager();
        Customer custWithPhone = customerDAO.findCustomerWithPhoneNumberById(customer1.getId()).orElse(null);
        assertNotNull(custWithPhone);

        assertEquals(1, custWithPhone.getPhoneNumbers().size());

        custWithPhone = customerDAO.findCustomerWithPhoneNumberById(customer1.getId()).orElse(null);
        assertNotNull(custWithPhone);

        assertEquals(1, custWithPhone.getPhoneNumbers().size());
    }

    @Test
    public void testAddPhonesTo_Existing_CustomerWithEntityManager() {
        EntityManager manager = emf.createEntityManager();
        manager.setFlushMode(FlushModeType.COMMIT);

        manager.getTransaction().begin();
        String jpqlStr = "select c from Customer c join fetch c.phoneNumbers p where c.id = :id";
        TypedQuery<Customer> query = manager.createQuery(jpqlStr, Customer.class);
        query.setParameter("id", 1);
        //Customer customer1 = customerDAO.findCustomerWithPhoneNumberById(1).orElse(null);
        Customer customer1 = query.getSingleResult();
        int oldCount = customer1.getPhoneNumbers().size();
        PhoneNumber pn = new PhoneNumber("383 9393 9393", PhoneNumber.Type.SATELLITE);
        customer1.addPhoneNumber(pn);
        manager.getTransaction().commit();
        manager.close();

        Customer custWithPhone = customerDAO.findCustomerWithPhoneNumberById(customer1.getId()).orElse(null);
        assertNotNull(custWithPhone);

        assertEquals(oldCount + 1, custWithPhone.getPhoneNumbers().size());

        custWithPhone = customerDAO.findCustomerWithPhoneNumberById(customer1.getId()).orElse(null);
        assertNotNull(custWithPhone);

        assertEquals(oldCount + 1, custWithPhone.getPhoneNumbers().size());
    }

    @Test
    public void testDeletePhonesFrom_Existing_CustomerWithEntityManager() {
        EntityManager manager = emf.createEntityManager();
        manager.setFlushMode(FlushModeType.COMMIT);

        manager.getTransaction().begin();
        String jpqlStr = "select c from Customer c join fetch c.phoneNumbers p where c.id = :id";
        TypedQuery<Customer> query = manager.createQuery(jpqlStr, Customer.class);
        query.setParameter("id", 1);
        //Customer customer1 = customerDAO.findCustomerWithPhoneNumberById(1).orElse(null);
        Customer customer1 = query.getSingleResult();
//        Customer customer1 = customerDAO.findCustomerWithPhoneNumberById(1).orElse(null);
        int oldCount = customer1.getPhoneNumbers().size();
        PhoneNumber pn = customer1.getPhoneNumbers().stream().findFirst().orElse(null);
        customer1.deletePhoneNumber(pn);
        manager.getTransaction().commit();
        manager.close();

        Customer custWithPhone = customerDAO.findCustomerWithPhoneNumberById(customer1.getId()).orElse(null);
        assertNotNull(custWithPhone);

        assertEquals(oldCount - 1, custWithPhone.getPhoneNumbers().size());

        custWithPhone = customerDAO.findCustomerWithPhoneNumberById(customer1.getId()).orElse(null);
        assertNotNull(custWithPhone);

        assertEquals(oldCount - 1, custWithPhone.getPhoneNumbers().size());
    }

    @Test
    @Rollback(false)
    public void deleteAParticularPhoneNumberFrom_ExistingCustomer() {
        Customer customer1 = customerDAO.findCustomerWithPhoneNumberById(1).orElse(null);
        int oldCount = customer1.getPhoneNumbers().size();

        PhoneNumber pn = new PhoneNumber("222 333-4444", PhoneNumber.Type.MOBILE);
        customer1.getPhoneNumbers().remove(pn);

        assertEquals(oldCount - 1, customer1.getPhoneNumbers().size());
    }

    @Test
    public void testFindCustomersForPhoneNumber() {

        String phoneNumber = "222 333-5555";

        Customer newCustomer = new Customer("Franzy", LocalDate.now(),
                Customer.Status.RESTRICTED, new PhoneNumber(phoneNumber, PhoneNumber.Type.WORK));

        newCustomer = customerDAO.save(newCustomer);

//        TypedQuery<Customer> query = em.createQuery(jpqlStr, Customer.class);

        List<CustNameWithPhonesDTO> cnwpd = customerDAO.findCustsByPhoneNumber(phoneNumber);

        System.out.println("CustNames");
        cnwpd.forEach(System.out::println);

        assertEquals(2, cnwpd.size());

    }

}
