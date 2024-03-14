package expeditors.backend.custapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author whynot
 */

@Entity
public class Customer implements Comparable<Customer>{


    public enum Status {
        PRIVILEGED,
        NORMAL,
        RESTRICTED
    }

    /*
        1. Constructors
    a. Create appropriate Constructors for your class.  Try and initialize
    your object in only one place in the code.
    b. Make sure your object is always properly initialized, no matter
    which constructor is called.  All variables that should not be null should have an appropriate value.
    c. Write Unit tests for all your constructors. Aim for 100% coverage on the constructors.

     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL,
    mappedBy = "customer",
    orphanRemoval = true)
//    @JoinColumn(name="CUSTOMER_ID", referencedColumnName="id")
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();

    public Customer(int id, String name, LocalDate dob, Status status,
                    Set<PhoneNumber> phoneNumbers) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.status = status;
//        this.phoneNumbers.addAll(phoneNumbers);
        phoneNumbers.forEach(this::addPhoneNumber);
    }

    public Customer(int id, String name, LocalDate dob, Status status,
                    PhoneNumber ...phoneNumbers) {
        this(id, name, dob, status, new HashSet<>(Arrays.asList(phoneNumbers)));
    }

    public Customer(int id, String name, LocalDate dob, Status status,
                    List<PhoneNumber> phoneNumbers) {
        this(id, name, dob, status, new HashSet<>(phoneNumbers));
    }

    public Customer(String name, LocalDate dob, Status status, PhoneNumber ... phoneNumbers) {
        this(0, name, dob, status, new HashSet<>(Arrays.asList(phoneNumbers)));
    }

    public Customer(String name, LocalDate dob) {
        this(0, name, dob, Status.NORMAL, new HashSet<>());
    }

    public Customer() {
        this(0, "", null, Status.NORMAL, new HashSet<>());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<PhoneNumber> phoneNumbers) {
        phoneNumbers.forEach(this::addPhoneNumber);
//        this.phoneNumbers.addAll(phoneNumbers);
    }

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.add(phoneNumber);
        phoneNumber.setCustomer(this);
    }

    public void deletePhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.remove(phoneNumber);
        phoneNumber.setCustomer(null);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", status=" + status +
                '}';
    }

    public String toLongString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", status=" + status +
                ", phoneNumbers=" + phoneNumbers +
                '}';
    }

    @Override
    public int compareTo(Customer o) {
        return Integer.compare(id, o.getId());
    }
}
