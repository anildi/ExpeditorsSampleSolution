package expeditors.backend.adoptapp.controller;

import expeditors.backend.adoptapp.domain.Adopter;
import expeditors.backend.adoptapp.domain.Pet;
import expeditors.backend.adoptapp.service.AdopterRepoService;
import expeditors.backend.utils.UriCreator;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whynot
 */
@RestController
@RequestMapping("/petreposervice")
public class AdopterRepoController {

    private AdopterRepoService adopterService;
    private UriCreator uriCreator;

    public AdopterRepoController(AdopterRepoService adopterService,
                                 UriCreator uriCreator) {
        this.adopterService = adopterService;
        this.uriCreator = uriCreator;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Adopter> adopters = adopterService.getAllAdopters();
        return ResponseEntity.ok(adopters);
    }

    @GetMapping("/{id:\\d+}")   //id of only digits
    public ResponseEntity<?> getAdopter(@PathVariable("id") int id) {
        Adopter adopters = adopterService.getAdopter(id);
        if(adopters == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No adopters with id: " + id);
        }
        return ResponseEntity.ok(adopters);
    }

    @PostMapping
    public ResponseEntity<?> addAdopter(@RequestBody Adopter adopter) {
        Adopter newAdopter = adopterService.addAdopter(adopter);

        URI uri = uriCreator.getUriFor(newAdopter.getId());

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<?> deleteAdopter(@PathVariable("id") int id) {
        boolean result = adopterService.deleteAdopter(id);
        if(!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Adopter with id: " + id);
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateAdopter(@RequestBody Adopter adopter) {
        boolean result = adopterService.updateAdopter(adopter);
        if(!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No adopter with id: " + adopter.getId());
        }

        return ResponseEntity.noContent().build();
    }

    //Add a Pet
    @PostMapping("/{adopterId}")
    public ResponseEntity<?> addPet(@PathVariable("adopterId") int adopterId, @RequestBody Pet pet) {
        boolean result = adopterService.addPetToAdopter(adopterId, pet);
        if(!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                  .body("No adopter with id: " + adopterId);
        }

        return ResponseEntity.noContent().build();
    }

    //Remove a Pet
    @DeleteMapping("/{adopterId}/{petId}")
    public ResponseEntity<?> removePet(@PathVariable("adopterId") int adopterId,
                                       @PathVariable("petId") int petId) {
        boolean result = adopterService.removePetFromAdopter(adopterId, petId);
        if(!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                  .body(STR."No adopter with id: \{adopterId}");
        }

        return ResponseEntity.noContent().build();
    }
}
