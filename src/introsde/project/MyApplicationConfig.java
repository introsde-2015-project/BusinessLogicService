package introsde.project;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
/**
 * Application configs
 * @author Toomas
 *
 */
@ApplicationPath("/")
public class MyApplicationConfig extends ResourceConfig {
    public MyApplicationConfig () {
        packages("introsde.project");
    }
}
