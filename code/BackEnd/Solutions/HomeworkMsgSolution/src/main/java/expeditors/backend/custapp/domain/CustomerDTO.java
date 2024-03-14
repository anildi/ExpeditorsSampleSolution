package expeditors.backend.custapp.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import expeditors.backend.custapp.domain.Customer.Status;

/**
 * @author whynot
 */

public record CustomerDTO(int id, String name,
                          LocalDate dob, Customer.Status status) implements Comparable<CustomerDTO> {
    public CustomerDTO(String name, LocalDate dob, Status status) {
        this(0, name, dob, Status.NORMAL);
    }

    @Override
    public int compareTo(CustomerDTO o) {
        return Integer.compare(id, o.id());
    }
}
//public class CustomerDTO implements Comparable<CustomerDTO>{
//
//    /*
//        1. Constructors
//    a. Create appropriate Constructors for your class.  Try and initialize
//    your object in only one place in the code.
//    b. Make sure your object is always properly initialized, no matter
//    which constructor is called.  All variables that should not be null should have an appropriate value.
//    c. Write Unit tests for all your constructors. Aim for 100% coverage on the constructors.
//
//     */
//
//    private int id;
//
//    private String name;
//    private LocalDate dob;
//
//    private Customer.Status status;
//
//
//    public CustomerDTO(int id, String name, LocalDate dob, Status status) {
//        this.id = id;
//        this.name = name;
//        this.dob = dob;
//        this.status = status;
//    }
//
//    public CustomerDTO(String name, LocalDate dob, Status status) {
//        this(0, name, dob, status);
//    }
//
//
//    @Override
//    public String toString() {
//        return "CustomerDTO{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", dob=" + dob +
//                ", status=" + status +
//                '}';
//    }
//
//    public CustomerDTO(String name, LocalDate dob) {
//        this(0, name, dob, Status.NORMAL);
//    }
//
//    public CustomerDTO() {
//        this(0, "", null, Status.NORMAL);
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public LocalDate getDob() {
//        return dob;
//    }
//
//    public void setDob(LocalDate dob) {
//        this.dob = dob;
//    }
//
//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }
//
//
//    public String toLongString() {
//        return "Customer{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", dob=" + dob +
//                ", status=" + status +
//                '}';
//    }
//
//    @Override
//    public int compareTo(CustomerDTO o) {
//        return Integer.compare(id, o.getId());
//    }
//}
