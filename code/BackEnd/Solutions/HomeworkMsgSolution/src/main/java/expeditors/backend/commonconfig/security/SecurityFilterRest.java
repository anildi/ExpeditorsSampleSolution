package expeditors.backend.commonconfig.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * We have split up our security configuration into two parts.
 * This one deals with the REST API, i.e. "/adminrest/**".
 *
 * @author whynot
 */
@Configuration
@Order(1)
public class SecurityFilterRest {

    //We need these two to plug them into the http object below
    //so we get a chance to deal with 401 and 403 errors.
    @Autowired
    private AccessExceptionHandler accessDeniedHandler;
    @Autowired
    private AuthExceptionHandler authExceptionHandler;


    @Bean
    public SecurityFilterChain restFilterChain(HttpSecurity http) throws Exception {
        //The first antMatcher limits the scope of this filter chain.
        //to /adminrest/**
        http.securityMatcher("/customerrepo/**",
                        "/customer/**",
                        "/petservice/**",
                        "/petreposervice/**").authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // All GET requests are open
                .requestMatchers(HttpMethod.GET, "/customerrepo/**", "/customer/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/petreposervice/**", "/petservice/**").hasAnyRole("USER", "ADMIN")
                //PUT needs to be authenticated and is then further fine-tuned in StudentService.
                .requestMatchers(HttpMethod.PUT, "/customerrepo/**", "/customer/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/petreposervice/**", "/petservice/**").hasAnyRole("USER", "ADMIN")
                // POST, DELETE, etc. requests need Role of ADMIN
                .requestMatchers(HttpMethod.POST, "/customerrepo/**", "/customer/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/petreposervice/**", "/petservice/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/customerrepo/**", "/customer/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/petreposervice/**", "/petservice/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.PATCH, "/customerrepo/**", "/customer/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/petreposervice/**", "/petservice/**").hasRole("ADMIN")
                //Say NO to everything else.
                .requestMatchers("/customerrepo/**", "/customer/**").denyAll()
                .and()
                //Don't need this for REST.  Else we have to always supply a CSRF Token.
                .csrf(csrf -> csrf.disable())
                //We need to add the authenticationEntryPoint here to channel
                //Authorization Exceptions to our Exception Handler.
                .httpBasic(customizer -> customizer.authenticationEntryPoint(authExceptionHandler))
                .exceptionHandling(exh -> exh.accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }
}
