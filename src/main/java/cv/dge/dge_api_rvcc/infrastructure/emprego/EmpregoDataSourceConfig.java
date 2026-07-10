package cv.dge.dge_api_rvcc.infrastructure.emprego;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "cv.dge.dge_api_rvcc.infrastructure.emprego.repository",
        entityManagerFactoryRef = "empregoEntityManagerFactory",
        transactionManagerRef = "empregoTransactionManager"
)
public class EmpregoDataSourceConfig {

    @Bean(name = "empregoDataSource")
    public DataSource empregoDataSource(
            @Value("${spring.datasource.emprego.url}") String url,
            @Value("${spring.datasource.emprego.username}") String username,
            @Value("${spring.datasource.emprego.password}") String password
    ) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }

    @Bean(name = "empregoEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean empregoEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("empregoDataSource") DataSource dataSource
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.hbm2ddl.auto", "none");
        props.put("hibernate.default_schema", "public");

        return builder
                .dataSource(dataSource)
                .packages("cv.dge.dge_api_rvcc.infrastructure.emprego")
                .persistenceUnit("emprego")
                .properties(props)
                .build();
    }

    @Bean(name = "empregoTransactionManager")
    public PlatformTransactionManager empregoTransactionManager(
            @Qualifier("empregoEntityManagerFactory") EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }
}
