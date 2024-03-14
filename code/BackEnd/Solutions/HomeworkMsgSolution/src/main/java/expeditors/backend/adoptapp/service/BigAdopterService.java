package expeditors.backend.adoptapp.service;

import expeditors.backend.adoptapp.dao.inmemory.BigInMemoryAdopterDAO;
import expeditors.backend.adoptapp.domain.BigAdopter;
import expeditors.backend.adoptapp.domain.PetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BigAdopterService {

    @Autowired
    private BigInMemoryAdopterDAO adopterDAO;

    public BigAdopter addAdopter(BigAdopter adopter) {
        return adopterDAO.insert(adopter);
    }

    public BigAdopter getAdopter(int id) {
        return adopterDAO.findById(id);
    }

    public List<BigAdopter> getAllAdopters() {
        return adopterDAO.findAll();
    }

    public boolean deleteAdopter(int id) {
        return adopterDAO.delete(id);
    }

    public boolean updateAdopter(BigAdopter adopter) {
        return adopterDAO.update(adopter);
    }


    public List<BigAdopter> getAdoptersByPetType(PetType type) {
        List<BigAdopter> result = adopterDAO
                .findAll().stream()
                .filter(a -> a.getPetType() == type)
                .toList();
        return result;
    }
}
