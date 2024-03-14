package expeditors.backend.adoptapp.controller.web;

import expeditors.backend.adoptapp.domain.Adopter;
import expeditors.backend.adoptapp.domain.Pet;
import expeditors.backend.adoptapp.domain.PetType;
import expeditors.backend.adoptapp.service.AdopterRepoService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdopterWebController {

   @Autowired
   private AdopterRepoService adopterRepoService;

   @GetMapping ("/pets")
   public ModelAndView getAllAdoptersAndPets() {
      List<Adopter> adopters = adopterRepoService.getAllAdopters();
      ModelAndView mav = new ModelAndView("showAdopters", "adopters", adopters);
      return mav;
   }

   @GetMapping ("/pets/showAdopter")
   public ModelAndView getAdopterWithPets(@RequestParam("adopterId") int adopterId) {
      Adopter adopter = adopterRepoService.getAdopter(adopterId);
      ModelAndView mav = new ModelAndView("showAdopter", "adopter", adopter);
      return mav;
   }

   @GetMapping("/pets/addAdopter")
   public ModelAndView addAdopterShowForm() {
      Adopter blankAdopter = new Adopter();
      ModelAndView mav = new ModelAndView("addAdopter", "adopter", blankAdopter);
      return mav;
   }

   @PostMapping("/pets/addAdopter")
   public ModelAndView addAdopter(Adopter adopter) {
      Adopter addedAdopter = adopterRepoService.addAdopter(adopter);
      ModelAndView mav = new ModelAndView("showAdopter", "adopter", addedAdopter);
      return mav;
   }

   @GetMapping("/pets/addPetToAdopter")
   public ModelAndView addPetToAdopterShowForm(
         @RequestParam(name = "adopterId") int adopterId) {

      Adopter blankAdopter = new Adopter();
      PetType [] types = PetType.values();
      Pet blankPet = new Pet();
      var params = Map.of("adopterId", adopterId,
            "petTypes", types,
            "pet", blankPet);
      ModelAndView mav = new ModelAndView("addPetToAdopter", params);
      return mav;
   }

   @PostMapping("/pets/addPetToAdopter/{adopterId}")
   public ModelAndView addPetToAdopterPost(@PathVariable("adopterId") int adopterId,
                                           Pet newPet) {
      boolean result = adopterRepoService.addPetToAdopter(adopterId, newPet);
      if(result) {
         var adopterWithNewPet = adopterRepoService.getAdopter(adopterId);
         ModelAndView mav = new ModelAndView("showAdopter", "adopter", adopterWithNewPet);
         return mav;
      } else {
         ModelAndView mav = new ModelAndView("realerror", "message", "No Adopter With Id " + adopterId);
         return mav;
      }
   }

   @PostMapping("/pets/removePetFromAdopter/{adopterId}/{petId}")
   public ModelAndView removePetFromAdopterPostWithPathParams(@PathVariable("adopterId") int adopterId,
                                                @PathVariable("petId") int petId) {
      return removePetFromAdopterPost(adopterId, petId);
   }

   @PostMapping("/pets/removePetFromAdopter")
   public ModelAndView removePetFromAdopterPost(@RequestParam("adopterId") int adopterId,
                                           @RequestParam("petId") int petId) {
      boolean result = adopterRepoService.removePetFromAdopter(adopterId, petId);
      if(result) {
         var adopterWithNewPet = adopterRepoService.getAdopter(adopterId);
         ModelAndView mav = new ModelAndView("showAdopter", "adopter", adopterWithNewPet);
         return mav;
      } else {
         ModelAndView mav = new ModelAndView("realerror", "message", "No Adopter Or Pet With Id "
               + adopterId + ", " + petId);
         return mav;
      }
   }
}
