package expeditors.backend.adoptapp.service;

import expeditors.backend.adoptapp.dao.repository.BigAdopterRepo;
import expeditors.backend.adoptapp.domain.BigAdopter;
import expeditors.backend.adoptapp.domain.PetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BigAdopterRepoService {

    @Autowired
    private BigAdopterRepo adopterDAO;

    public BigAdopter addAdopter(BigAdopter adopter) {
        return adopterDAO.save(adopter);
    }

    public BigAdopter getAdopter(int id) {
        return adopterDAO.findById(id).orElse(null);
    }

    public List<BigAdopter> getAllAdopters() {
        return adopterDAO.findAll();
    }

    public boolean deleteAdopter(int id) {
        adopterDAO.deleteById(id);
        return true;
    }

    public boolean updateAdopter(BigAdopter adopter) {
        adopterDAO.save(adopter);
        return true;
    }


    public List<BigAdopter> getAdoptersByPetType(PetType type) {
        List<BigAdopter> result = adopterDAO
                .findAll().stream()
                .filter(a -> a.getPetType() == type)
                .toList();
        return result;
    }
}
