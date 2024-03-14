package expeditors.backend.commonconfig.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.ResultSet;
import java.util.List;

public class MyUserDetailsDBService implements UserDetailsService {

    private JdbcTemplate userTemplate;
    private JdbcTemplate mainTemplate;

    private String mainUserSelect = "select id, username, password, enabled from users where username = ?";
    private String authoritiesSelect = "select authority from authorities where username = ?";
    private String realNameSelect = "select name from Customer c where c.id = ?";

    //Passwords are BCrypted 'password'
    public MyUserDetailsDBService(JdbcTemplate userTemplate, JdbcTemplate mainTemplate) {
        this.userTemplate = userTemplate;
        this.mainTemplate = mainTemplate;
    }


    @Override
    public UserDetails loadUserByUsername(String input) {
        try {
            MyUserDetails result = userTemplate.queryForObject(
                    mainUserSelect,
                    (ResultSet rs, int rowNum) -> {
                        int id = rs.getInt("id");
                        String userName = rs.getString("username");
                        String pw = rs.getString("password");
                        boolean enabled = rs.getBoolean("enabled");

                        //get the roles
                        List<String> auths = userTemplate.queryForList(authoritiesSelect, String.class, userName);

                        //Now get the realName from the main DB
                        List<String> tmp = mainTemplate.queryForList(realNameSelect, String.class, id);
                        String realName = tmp.size() > 0 ? tmp.get(0) : userName;

                        MyUserDetails mud = MyUserDetails.of(id, userName, pw, realName, auths);
                        return mud;
                    }, input);

            return result;
        }catch(Exception ex) {
            throw new UsernameNotFoundException("Auth Exception for: " + input);
        }
    }

    //Not used
    private String getRealName(int id) {

        String realName = mainTemplate.query(realNameSelect,
                rs -> rs.next() ? rs.getString("name") : null,
                id);

        return realName;
    }
}
