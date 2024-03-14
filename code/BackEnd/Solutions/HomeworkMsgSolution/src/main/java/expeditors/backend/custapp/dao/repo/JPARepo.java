package expeditors.backend.custapp.dao.repo;

import expeditors.backend.custapp.domain.CustNameWithPhonesDTO;

import java.util.List;

/**
 * @author whynot
 */
public interface JPARepo {
    List<CustNameWithPhonesDTO> findAllCustNameWithPhones();
    List<CustNameWithPhonesDTO> findCustsByPhoneNumber(String... query);
}
