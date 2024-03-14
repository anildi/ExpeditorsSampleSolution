package expeditors.backend.custapp.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author whynot
 */
public class CustNameWithPhonesDTO {
    private String name;
    private List<PhoneNumber> phoneNumbers;

    public CustNameWithPhonesDTO(String name, List<PhoneNumber> phoneNumbers) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    public CustNameWithPhonesDTO(String name) {
        this(name, new ArrayList<PhoneNumber>());
    }

    public CustNameWithPhonesDTO() {
        this("", new ArrayList<PhoneNumber>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
