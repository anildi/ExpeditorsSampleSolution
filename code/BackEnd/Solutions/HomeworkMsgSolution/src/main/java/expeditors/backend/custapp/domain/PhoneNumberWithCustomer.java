package expeditors.backend.custapp.domain;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * @author whynot
 */
@Entity
public class PhoneNumberWithCustomer {

    public enum Type {
        MOBILE,
        WORK,
        HOME,
        SATELLITE,
        OTHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    private Customer customer;

    public PhoneNumberWithCustomer(String phoneNumber, Type type, Customer customer) {
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.customer = customer;
    }

    public PhoneNumberWithCustomer(String phoneNumber, Type type) {
        this(phoneNumber, type, null);
    }

    public PhoneNumberWithCustomer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String toLongString() {
        return "PhoneNumber{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", customer='" + customer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumberWithCustomer that = (PhoneNumberWithCustomer) o;
        return Objects.equals(phoneNumber, that.phoneNumber) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber, type);
    }
}
