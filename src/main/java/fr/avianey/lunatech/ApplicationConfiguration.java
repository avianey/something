package fr.avianey.lunatech;

import fr.avianey.lunatech.entity.Country;
import fr.avianey.lunatech.service.ReportService;
import fr.avianey.lunatech.service.SearchService;
import org.flywaydb.core.Flyway;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Startup // required for PostConstruct init
@Singleton
@ApplicationPath("/api/v1")
@TransactionManagement(TransactionManagementType.BEAN) // flyway manages its transactions
public class ApplicationConfiguration extends Application {

    @Resource(lookup = "openejb:Resource/JTA_Datasource")
    private DataSource dataSource;

    /**
     * Ensure initialization of schema before datasource is used
     * TODO init schema with flyway and remove this dep
     */
    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    public Set<Class<?>> getClasses() {
        return new HashSet<>(asList(
                SearchService.class,
                ReportService.class
        ));
    }

    /**
     * Handle DB migration at startup
     */
    @PostConstruct
    public void init() {
        em.createQuery("SELECT c FROM " + Country.class.getSimpleName() + " c").getFirstResult();
        final Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.baseline();
        flyway.setLocations("classpath:sql");
        flyway.migrate();
    }

}
