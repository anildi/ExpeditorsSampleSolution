package expeditors.backend.adoptapp.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "BIG_ADOPTER")
public class BigAdopter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String name;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "ADOPT_DATE")
    private LocalDate adoptionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "PET_TYPE")
    private PetType petType;

    @Column(name = "PET_NAME")
    private String petName;

    @Column(name = "PET_BREED")
    private String petBreed;

    public BigAdopter(String name, String phoneNumber,
                      LocalDate adoptionDate, PetType petType, String petName, String petBreed) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.adoptionDate = adoptionDate;
        this.petType = petType;
        this.petName = petName;
        this.petBreed = petBreed;
    }

    public BigAdopter() {
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

    public LocalDate getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDate adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    @Override
    public String toString() {
        return "BigAdopter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", adoptionDate=" + adoptionDate +
                ", petType=" + petType +
                ", petName='" + petName + '\'' +
                ", petBreed='" + petBreed + '\'' +
                '}';
    }
}
