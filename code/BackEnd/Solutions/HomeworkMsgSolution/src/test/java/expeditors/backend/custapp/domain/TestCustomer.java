package expeditors.backend.custapp.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author whynot
 */
public class TestCustomer {

    @Test
    public void testCustomerWithPhoneNumbers() {
        Customer customer = new Customer("Francine", LocalDate.of(1960, 10, 10),
                Customer.Status.PRIVILEGED);
        PhoneNumber pn = new PhoneNumber("333 88 88393", PhoneNumber.Type.HOME);
        customer.addPhoneNumber(pn);

        PhoneNumber pn2 = new PhoneNumber("895 7577 8484", PhoneNumber.Type.MOBILE);
        customer.addPhoneNumber(pn2);

        assertEquals(2, customer.getPhoneNumbers().size());
    }

    @Test
    public void testDeletePhoneNumber() {
        Customer customer = new Customer("Francine", LocalDate.of(1960, 10, 10),
                Customer.Status.PRIVILEGED);
        PhoneNumber pn = new PhoneNumber("333 88 88393", PhoneNumber.Type.HOME);
        customer.addPhoneNumber(pn);

        PhoneNumber pn2 = new PhoneNumber("895 7577 8484", PhoneNumber.Type.MOBILE);
        customer.addPhoneNumber(pn2);

        assertEquals(2, customer.getPhoneNumbers().size());

        customer.deletePhoneNumber(pn);

        assertEquals(1, customer.getPhoneNumbers().size());
    }
}
