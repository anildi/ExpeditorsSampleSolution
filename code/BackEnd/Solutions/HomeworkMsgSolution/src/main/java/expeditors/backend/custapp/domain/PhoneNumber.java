package expeditors.backend.custapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

/**
 * @author whynot
 */
@Entity
public class PhoneNumber {

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

    /**
     * You can implement this relationship as a unidirectional relationship
     * between Customer and PhoneNumber.  In that case you need to get rid of
     * the 'mappedBy' attribute on the Customer side, and add a @JoinColumn annotation
     * there.  If your application is always only going to go from Customers to PhoneNumbers
     * and never the other way around, then that may seem the logically correct way to set
     * up the relationship.  But if you look the SQL that Hibernate generates
     * (which you should ALWAYS be doing!) you will see a bunch of extra updates
     * and deletes that are not stricly necessary.
     * Moral of the story is that making a OneToMany relationship bidirectional is
     * probably the way to go, even if you never intend to go from a PhoneNumber to a
     * Customer.
     */
    @ManyToOne
//    @Transient
    private Customer customer;

    public PhoneNumber(String phoneNumber, Type type) {
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    public PhoneNumber() {
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @JsonIgnore
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(phoneNumber, that.phoneNumber) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber, type);
    }
}
