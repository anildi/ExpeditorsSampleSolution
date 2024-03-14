package expeditors.backend.adoptapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "PET")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PET_ID")
    private int petId;

    @Column(name = "ADOPT_DATE")
    private LocalDate adoptionDate;

    @Column(name = "PET_TYPE")
    @Enumerated(EnumType.STRING)
    private PetType type;

    @Column(name = "PET_NAME")
    private String name;

    @Column(name = "PET_BREED")
    private String breed;

    /**
     * You can implement this relationship as a unidirectional relationship
     * between Adopter and Pet.  In that case you need to get rid of
     * the 'mappedBy' attribute on the Adopter side, and add a @JoinColumn annotation
     * there.  If your application is always only going to go from Adopters to Pets
     * and never the other way around, then that may seem the logically correct way to set
     * up the relationship.  But if you look the SQL that Hibernate generates
     * (which you should ALWAYS be doing!) you will see a bunch of extra updates
     * and deletes that are not stricly necessary.
     * Moral of the story is that making a OneToMany relationship bidirectional is
     * probably the way to go, even if you never intend to go from a Pet to an
     * Adopter.
     */
    @ManyToOne
    @JsonIgnore
    private Adopter adopter;

    private Pet(int petId, PetType type, String name, String breed, LocalDate adoptionDate) {
        this.petId = petId;
        this.type = type;
        this.name = name;
        this.breed = breed;
        this.adoptionDate = adoptionDate;
    }

    public Pet(PetType type, String name, String breed) {
        this(0, type, name, breed, null);
    }

    public Pet(PetType type) {
        this(0, type, null, null, null);
    }

    public Pet() {
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public LocalDate getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDate adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public Adopter getAdopter() {
        return adopter;
    }

    public void setAdopter(Adopter adopter) {
        this.adopter = adopter;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petId=" + petId +
                ", adoptionDate=" + adoptionDate +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return petId == pet.petId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId);
    }

    //A Builder for Pets
    //Some static Factory methods to make the builder easier to
    //create.
    public static PetBuilder builder(PetType type, LocalDate adoptionDate) {
        return new PetBuilder().type(type).adoptionDate(adoptionDate);
    }

    public static PetBuilder builder(PetType type) {
        return new PetBuilder().type(type).adoptionDate(LocalDate.now());
    }

    //The actual Builder
    public static class PetBuilder {
        private int petId;
        private PetType type;
        private LocalDate adoptionDate;
        private String name;
        private String breed;

        public PetBuilder petId(int id) {
            this.petId = id;
            return this;
        }
        public PetBuilder type(PetType type) {
            this.type = type;
            return this;
        }

        public PetBuilder adoptionDate(LocalDate date) {
            this.adoptionDate = date;
            return this;
        }

        public PetBuilder adoptionDate(String dateStr) {
            return adoptionDate(LocalDate.parse(dateStr));
        }

        public PetBuilder name(String name) {
            this.name = name;
            return this;
        }
        public PetBuilder breed(String breed) {
            this.breed = breed;
            return this;
        }

        public Pet build() {
            if(this.type == null) {
                throw new RuntimeException("Type is REQUIRED");
            }
            return new Pet(type, name, breed);
        }
    }
}
