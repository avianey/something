package fr.avianey.lunatech;

import org.apache.openejb.OpenEjbContainer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public abstract class AbstractTest {

    protected static Context context;

    @BeforeClass
    public static void beforeClass() throws NamingException, IOException, SQLException {
        Properties p = new Properties();
        p.setProperty(OpenEjbContainer.OPENEJB_EMBEDDED_REMOTABLE, "true");
        p.setProperty(EJBContainer.APP_NAME, "/api/v1");
        context = EJBContainer.createEJBContainer(p).getContext();
    }

    @AfterClass
    public static void afterClass() throws NamingException {
        if (context != null) {
            context.close();
        }
    }


}