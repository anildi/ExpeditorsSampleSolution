package expeditors.backend.adoptapp.controller;

import expeditors.backend.adoptapp.domain.BigAdopter;
import expeditors.backend.adoptapp.service.BigAdopterService;
import expeditors.backend.utils.UriCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @author whynot
 */
@RestController
@RequestMapping("/bigpetservice")
public class BigAdopterController {

    private BigAdopterService adopterService;
    private UriCreator uriCreator;

    public BigAdopterController(BigAdopterService adopterService,
                                UriCreator uriCreator) {
        this.adopterService = adopterService;
        this.uriCreator = uriCreator;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<BigAdopter> adopters = adopterService.getAllAdopters();
        return ResponseEntity.ok(adopters);
    }

    @GetMapping("/{id:\\d+}")   //id of only digits
    public ResponseEntity<?> getAdopter(@PathVariable("id") int id) {
        BigAdopter adopters = adopterService.getAdopter(id);
        if(adopters == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No adopters with id: " + id);
        }
        return ResponseEntity.ok(adopters);
    }

    @PostMapping
    public ResponseEntity<?> addAdopter(@RequestBody BigAdopter adopter) {
        BigAdopter newAdopter = adopterService.addAdopter(adopter);

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
    public ResponseEntity<?> updateAdopter(@RequestBody BigAdopter adopter) {
        boolean result = adopterService.updateAdopter(adopter);
        if(!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No adopter with id: " + adopter.getId());
        }

        return ResponseEntity.noContent().build();
    }
}
