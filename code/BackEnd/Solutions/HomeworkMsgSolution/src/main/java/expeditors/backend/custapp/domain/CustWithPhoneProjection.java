package expeditors.backend.custapp.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author whynot
 */
public interface CustWithPhoneProjection {
    String getName();
    List<PhoneNumber> getPhoneNumbers();
}
