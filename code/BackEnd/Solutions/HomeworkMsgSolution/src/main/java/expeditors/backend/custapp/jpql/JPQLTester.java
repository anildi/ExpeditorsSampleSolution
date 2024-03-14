package expeditors.backend.custapp.jpql;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author whynot
 */

@Component
public class JPQLTester {
    @PersistenceUnit
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;

//    public <T> List<T> executeQuery(Class<T> clazz, String jpqlStr, Map<String, Object> namedParams) {
//        return executeQuery(clazz, jpqlStr, namedParams);
//    }

    public <T> List<T> executeQuery(Class<T> clazz, String jpqlStr, Object ...posParams) {
        return executeQuery(clazz, jpqlStr, Map.of(), posParams);

    }
    public <T> List<T> executeQuery(Class<T> clazz, String jpqlStr, Map<String, Object> namedParams, Object ...posParams) {
        TypedQuery<T> query = em.createQuery(jpqlStr, clazz);

        for(var entry : namedParams.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        for(int i = 0; i < posParams.length; i++) {
            query.setParameter(i + 1, posParams[i]);
        }

        List<T> result = query.getResultList();

        return result;
    }
}
