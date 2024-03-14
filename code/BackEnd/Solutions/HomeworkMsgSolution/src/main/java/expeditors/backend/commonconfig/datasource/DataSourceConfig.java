package expeditors.backend.commonconfig.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;


@Configuration
public class DataSourceConfig {

    /**
     * This is the Datasource to your application db.
     * Properties get picked up from application.properties.
     * Here we just collect all the properties, and then
     * the DataSourceProperties is passed in to the
     * @Bean method below to make the DataSource.
     * @return
     */
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties dsp = new DataSourceProperties();
        return dsp;
    }

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.configuration")
    public HikariDataSource dataSource() {
        HikariDataSource hds = dataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
        return hds;
    }

    /**
     * Our user database.
     * @return
     */
    @Bean
    @Qualifier("UserDS")
    public DataSource userDataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .setName("userdb")
//            .addScript("classpath:org/springframework/security/core/userdetails/jdbc/users.ddl")
            .addScript("classpath:/sql/userdb/h2/userdbSchema-h2.sql")
            .addScript("classpath:/sql/userdb/h2/userdbData-h2.sql")
            .build();
    }
}
