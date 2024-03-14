package expeditors.backend.adoptapp;

import expeditors.backend.adoptapp.domain.Adopter;
import expeditors.backend.adoptapp.domain.BigAdopter;
import expeditors.backend.adoptapp.domain.Pet;
import expeditors.backend.adoptapp.domain.PetType;
import expeditors.backend.adoptapp.service.AdopterService;
import expeditors.backend.adoptapp.service.BigAdopterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static java.lang.System.out;

//@SpringBootApplication
public class AdopterSpringBootApp {
    public static void main(String[] args) {
        SpringApplication.run(AdopterSpringBootApp.class, args);
    }
}

@Component
class MyAdopterInMemoryInit implements CommandLineRunner
{

    @Autowired
    private AdopterService adopterService;

    @Autowired
    private BigAdopterService bigAdopterService;

    @Override
    public void run(String... args) throws Exception {
        var adopters = List.of(
                new Adopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                Pet.builder(PetType.DOG).name("woofie").breed("mixed").build()),

                new Adopter("Francine", "383 9339 9999 9393", LocalDate.of(2020, 5, 9),
                        Pet.builder(PetType.DOG).name("slinky").breed("dalmation").build()),

                new Adopter("Darlene", "4484 9339 77939", LocalDate.of(2020, 5, 9),
                        Pet.builder(PetType.TURTLE).name("swifty").build())
        );

        adopters.forEach(adopterService::addAdopter);

        var bigAdopters = List.of(
                new BigAdopter("Joey", "383 9999 9393", LocalDate.of(1960, 6, 9),
                        PetType.DOG, "woofie", "mixed"),

                new BigAdopter("Francine", "383 9339 9999 9393", LocalDate.of(2020, 5, 9),
                        PetType.DOG, "slinky", "dalmation"),

                new BigAdopter("Darlene", "4484 9339 77939", LocalDate.of(2020, 5, 9),
                        PetType.TURTLE, "swifty", null)
        );

        bigAdopters.forEach(bigAdopterService::addAdopter);

        List<Adopter> result = adopterService.getAllAdopters();
        out.println("Adopter result: " + result.size());
        result.forEach(out::println);

        List<BigAdopter> result2 = bigAdopterService.getAllAdopters();
        out.println("BigAdopter result: " + result2.size());
        result2.forEach(out::println);

    }
}
