package expeditors.backend.adoptapp.dao.inmemory;

import expeditors.backend.adoptapp.domain.BigAdopter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class BigInMemoryAdopterDAO {

    private Map<Integer, BigAdopter> adopters = new ConcurrentHashMap<>();
    private AtomicInteger nextId = new AtomicInteger(1);

    public BigAdopter insert(BigAdopter newAdopter) {
        newAdopter.setId(nextId.getAndIncrement());
        newAdopter.setName("InMem: " + newAdopter.getName());
        adopters.put(newAdopter.getId(), newAdopter);

        return newAdopter;
    }

    public boolean delete(int id) {
        return adopters.remove(id) != null;
    }

    public boolean update(BigAdopter adopter) {
        return adopters.replace(adopter.getId(), adopter) != null;
    }

    public BigAdopter findById(int id) {
        return adopters.get(id);
    }

    public List<BigAdopter> findAll() {
        return new ArrayList(adopters.values());
    }
}
