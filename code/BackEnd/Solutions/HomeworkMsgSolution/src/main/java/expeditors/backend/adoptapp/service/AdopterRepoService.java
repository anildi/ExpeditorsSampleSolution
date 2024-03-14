package expeditors.backend.adoptapp.service;

import expeditors.backend.adoptapp.dao.repository.AdopterRepo;
import expeditors.backend.adoptapp.domain.Adopter;
import expeditors.backend.adoptapp.domain.Pet;
import expeditors.backend.adoptapp.domain.PetType;
import expeditors.backend.commonconfig.msg.MessageSender;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdopterRepoService {

    private final MessageSender messageSender;
    private final AdopterRepo adopterDAO;

    public AdopterRepoService(AdopterRepo adopterDAO, @Autowired(required = false)MessageSender messageSender) {
        this.adopterDAO = adopterDAO;
        this.messageSender = messageSender;
    }

    public Adopter addAdopter(Adopter adopter) {
        var newAdopter = adopterDAO.save(adopter);
        //Send out an event if we have a messageSender
        sendMessage(newAdopter);
        return newAdopter;
    }

    private void sendMessage(Adopter newAdopter) {
        if(messageSender != null) {
            messageSender.sendMessage(newAdopter);
        }
    }

    public Adopter getAdopter(int id) {
        return adopterDAO.findByIdWithPets(id);
    }

    public List<Adopter> getAllAdopters() {
        return adopterDAO.findAllWithPets();
    }

    public boolean deleteAdopter(int id) {
        if(adopterDAO.existsById(id)) {
            adopterDAO.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean updateAdopter(Adopter adopter) {
        if(adopterDAO.existsById(adopter.getId())) {
            adopterDAO.save(adopter);
            return true;
        }
        return false;
    }


    public List<Adopter> getAdoptersByPetType(PetType type) {
        List<Adopter> result = adopterDAO
                .findAll().stream()
                .filter(a -> a.getPets().stream().anyMatch(p -> p.getType()  == type))
                .toList();
        return result;
    }

    public boolean addPetToAdopter(int adopterId, Pet newPet) {
        var adopter = adopterDAO.findByIdWithPets(adopterId);
        if(adopter != null) {
            adopter.addPet(newPet);

            sendMessage(adopter);
            return true;
        }

        return false;
    }

    public boolean removePetFromAdopter(int adopterId, int petId) {
        var adopter = adopterDAO.findByIdWithPets(adopterId);
        if(adopter != null) {
            var pet = adopter.getPets().stream()
                  .filter(p -> p.getPetId() == petId)
                  .findFirst();
            if (pet.isPresent()) {
                adopter.removePet(pet.get());

                sendMessage(adopter);
                return true;
            }
        }
        return false;
    }
}
