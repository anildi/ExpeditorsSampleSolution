package expeditors.backend.custapp.dao.repo;

import expeditors.backend.custapp.domain.CustNameWithPhonesDTO;
import expeditors.backend.custapp.domain.PhoneNumber;
import jakarta.persistence.*;
import org.hibernate.query.ResultListTransformer;
import org.hibernate.query.TupleTransformer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of our custom JPARepo interface.
 * NOTE: The naming seems to be important.  The name of the
 *  implementing class has to be [Interface name]Impl.
 * @author whynot
 */

@Component
public class JPARepoImpl implements JPARepo {
    @PersistenceUnit
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CustNameWithPhonesDTO> findCustsByPhoneNumber(String... phoneNumber){
        //Fetch Customers who match a particular Phone Number
        //where p.?honeNumber in ?1
        String jpqlStr = """
                   select c.id, c.name , p
                   from Customer c 
                   left join c.phoneNumbers p
                   where c.id in 
                   (select p.customer.id from PhoneNumber p where p.phoneNumber in ?1)
                """;
        List<CustNameWithPhonesDTO> tResult = doQuery(jpqlStr, phoneNumber);
        return tResult;
    }

    @Override
    public List<CustNameWithPhonesDTO> findAllCustNameWithPhones() {
        String jpqlStr = """
                   select c.id, c.name , p
                   from Customer c 
                   left join c.phoneNumbers p
                   order by c.id
                """;

        var result = doQuery(jpqlStr);
        return result;
    }

    private List<CustNameWithPhonesDTO> doQuery(String jpqlStr, String ...params) {
//        EntityManager manager = emf.createEntityManager();

        //        Query query = manager.createQuery(jpqlStr);
//        org.hibernate.query.Query<CustWithPhone> unwrapped =
//                query.unwrap(org.hibernate.query.Query.class)
//                        .setTupleTransformer(new CustWithPhoneTransformer());
//
//        List<CustWithPhone> result = unwrapped.getResultList();
        var transformer = new CustWithPhoneTransformer();
        TypedQuery<CustNameWithPhonesDTO> query =
                //manager.createQuery(jpqlStr, CustNameWithPhonesDTO.class)
                em.createQuery(jpqlStr, CustNameWithPhonesDTO.class)
                        .unwrap(org.hibernate.query.Query.class)
                        .setTupleTransformer(transformer)
                        .setResultListTransformer(transformer);

        for(int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }

        List<CustNameWithPhonesDTO> result = query.getResultList();

//        System.out.println("Result: " + result);

        return result;
    }

    class CustWithPhoneTransformer implements TupleTransformer<CustNameWithPhonesDTO>,
            ResultListTransformer<CustNameWithPhonesDTO> {

        Map<Integer, CustNameWithPhonesDTO> cWithP = new HashMap<>();

        @Override
        public CustNameWithPhonesDTO transformTuple(Object[] tuple, String[] aliases) {

            int id = (int) tuple[0];
            String name = (String) tuple[1];
            PhoneNumber phoneNumber = (PhoneNumber) tuple[2];
            CustNameWithPhonesDTO cwp = cWithP.computeIfAbsent(id, i ->
                    new CustNameWithPhonesDTO(name)
            );
            cwp.getPhoneNumbers().add(phoneNumber);

            return cwp;
        }

        @Override
        public List<CustNameWithPhonesDTO> transformList(List<CustNameWithPhonesDTO> resultList) {
            return new ArrayList<>(cWithP.values());
        }
    }
}
