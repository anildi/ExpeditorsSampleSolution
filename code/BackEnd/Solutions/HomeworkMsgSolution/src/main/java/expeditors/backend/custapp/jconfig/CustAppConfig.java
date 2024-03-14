package expeditors.backend.custapp.jconfig;

import expeditors.backend.custapp.dao.CustomerDAO;
import expeditors.backend.custapp.dao.inmemory.InMemoryCustomerDAO;
import expeditors.backend.custapp.dao.jpa.JPACustomerDAO;
import expeditors.backend.custapp.service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author whynot
 */
@Configuration
public class CustAppConfig {

    @Bean
    @Profile("dev")
    public CustomerDAO customerDAO() {
       CustomerDAO dao = new InMemoryCustomerDAO();
       return dao;
    }

    @Bean(name = "customerDAO")
    @Profile("prod")
    public CustomerDAO jpaCustomerDAO() {
        CustomerDAO dao = new JPACustomerDAO();
        return dao;
    }

    @Bean
    public CustomerService customerService() {
        CustomerService service = new CustomerService();

        CustomerDAO dao = customerDAO();
        service.setCustomerDAO(dao);

        return service;
    }
}
