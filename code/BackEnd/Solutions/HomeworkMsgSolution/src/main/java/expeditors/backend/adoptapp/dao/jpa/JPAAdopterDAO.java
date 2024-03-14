package expeditors.backend.adoptapp.dao.jpa;

import expeditors.backend.adoptapp.dao.AdopterDAO;
import expeditors.backend.adoptapp.domain.Adopter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Profile("prod")
public class JPAAdopterDAO implements AdopterDAO {

    private Map<Integer, Adopter> adopters = new ConcurrentHashMap<>();
    private AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Adopter insert(Adopter newAdopter) {
        newAdopter.setId(nextId.getAndIncrement());
        newAdopter.setName("Jpa: " + newAdopter.getName());
        adopters.put(newAdopter.getId(), newAdopter);

        return newAdopter;
    }

    @Override
    public boolean delete(int id) {
        return adopters.remove(id) != null;
    }

    @Override
    public boolean update(Adopter adopter) {
        return adopters.replace(adopter.getId(), adopter) != null;
    }

    @Override
    public Adopter findById(int id) {
        return adopters.get(id);
    }

    @Override
    public List<Adopter> findAll() {
        return new ArrayList(adopters.values());
    }
}
