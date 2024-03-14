package expeditors.backend.adoptapp.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ADOPTER")
public class Adopter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "NAME")
    private String name;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "adopter",
            orphanRemoval = true)
    private Set<Pet> pets = new HashSet<>();

    public Adopter(String name, String phoneNumber, Set<Pet> pets) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        pets.forEach(this::addPet);
    }

    public Adopter(String name, String phoneNumber, LocalDate adoptionDate, List<Pet> pets) {
        this(name, phoneNumber, Set.copyOf(pets));
    }

    public Adopter(String name, String phoneNumber, LocalDate adoptionDate, Pet pet) {
        this(name, phoneNumber, Set.of(pet));
    }

    public Adopter(String name, String phoneNumber, Pet pet) {
        this(name, phoneNumber, Set.of(pet));
    }

    public Adopter() {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Pet> getPets() {
        return pets;
    }

    public void setPets(Set<Pet> pets) {
        this.pets.clear();
        pets.forEach(this::addPet);
    }

    public void addPet(Pet pet) {
        pet.setAdopter(this);
        pet.setAdoptionDate(LocalDate.now());
        pets.add(pet);
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.setAdopter(null);
    }

    @Override
    public String toString() {
        return "Adopter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", pets=" + pets +
                '}';
    }
}
