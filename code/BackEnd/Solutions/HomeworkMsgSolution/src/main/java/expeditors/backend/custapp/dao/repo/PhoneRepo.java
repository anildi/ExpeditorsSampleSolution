package expeditors.backend.custapp.dao.repo;

import expeditors.backend.custapp.domain.Customer;
import expeditors.backend.custapp.domain.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author whynot
 */
@Repository
public interface PhoneRepo extends JpaRepository<PhoneNumber, Integer> {

    //@Query("select p from PhoneNumber p left join fetch p.customer c")
    @Query("select p from PhoneNumber p")
    List<PhoneNumber> findAll();
}
