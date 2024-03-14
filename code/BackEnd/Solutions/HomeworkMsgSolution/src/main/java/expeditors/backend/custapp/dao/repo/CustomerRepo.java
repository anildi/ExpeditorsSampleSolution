package expeditors.backend.custapp.dao.repo;

import expeditors.backend.custapp.domain.CustWithPhoneProjection;
import expeditors.backend.custapp.domain.Customer;
import expeditors.backend.custapp.domain.CustomerDTO;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author whynot
 */
@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer>, JPARepo {

    //We override the default findById method to return a CustomerDTO
    //with no phone numbers
    @Query("select new expeditors.backend.custapp.domain.CustomerDTO(c.id, c.name, c.dob, c.status) from Customer c where c.id = :id")
    Optional<CustomerDTO> findOnlyCustomer(int id);

    @Query("select new expeditors.backend.custapp.domain.CustomerDTO(c.id, c.name, c.dob, c.status) from Customer c")
    List<CustomerDTO> findOnlyCustomers();

    //This one will give you the Customer with phone numbers
    @Query("select c from Customer c join fetch c.phoneNumbers p where c.id = :id")
    Optional<Customer> findCustomerWithPhoneNumberById(int id);

    @Query("select c from Customer c join fetch c.phoneNumbers p")
    List<Customer> findWithPhoneNumbers();

//    @EntityGraph(attributePaths = {"phoneNumbers"})
    List<CustWithPhoneProjection> findAllCustWithPhoneProjectionBy();
}
