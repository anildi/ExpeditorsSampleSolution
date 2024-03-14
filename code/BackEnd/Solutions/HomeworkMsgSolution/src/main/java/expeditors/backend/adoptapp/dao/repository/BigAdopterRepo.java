package expeditors.backend.adoptapp.dao.repository;

import expeditors.backend.adoptapp.domain.BigAdopter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BigAdopterRepo extends JpaRepository<BigAdopter, Integer> {
}
