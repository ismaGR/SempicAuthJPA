/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic;

import fr.uga.miashs.sempic.entities.SempicGroup;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.entities.SempicUserType;
import fr.uga.miashs.sempic.dao.GroupFacade;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.faces.annotation.FacesConfig;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 * In this class, some generic application config are given
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@DataSourceDefinition(
        name = ApplicationConfig.DATA_SOURCE,
        // HSQL
        className = "org.hsqldb.jdbcDriver",
        //url="jdbc:hsqldb:mem:hsqldb", // memory database that is deleted when the server is stopped
        url = "jdbc:hsqldb:file:sempicdb",
        databaseName = "SempicDB",
        user = "sempic",
        password = "sempic"
)

/*
Works only for glassfish (ie JavaEE8 fully compliant server), for tomm use web.xml login-config + declaration in context.xml
 */
 /*@DatabaseIdentityStoreDefinition(
        dataSourceLookup = ApplicationConfig.DATA_SOURCE,
        callerQuery = "select passwordhash from sempic.sempicuser where email=?", // prefix sempicuser with sempic. for Derby
        groupsQuery = "select userType from sempic.sempicuser where email = ?", // prefix sempicuser with sempic. for Derby
        hashAlgorithm = Pbkdf2PasswordHash.class,
        hashAlgorithmParameters = { "Pbkdf2PasswordHash.Iterations=3072", 
                                    "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512", 
                                    "Pbkdf2PasswordHash.SaltSizeBytes=64"},

        priority = 30)



@FormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/faces/login.xhtml",
                errorPage = "/login-error.html",
                useForwardToLogin=false))
 */
@FacesConfig(version = FacesConfig.Version.JSF_2_3)

@Singleton
@Named
@Startup
public class ApplicationConfig {


    public final static String DATA_SOURCE = "java:app/sempicdb";
    public final static String WEB_API = "/webapi";

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private SempicUserFacade userFacade;
    @Inject
    private GroupFacade groupFacade;

    @PostConstruct
    public void init() {
        SempicUser admin = new SempicUser();
        admin.setFirstname("Jack");
        admin.setLastname("Rabbit");
        admin.setEmail("admin@miashs.fr");
        admin.setUserType(SempicUserType.ADMIN);
        admin.setPasswordHash(passwordHash.generate("admin".toCharArray()));

        SempicGroup g = new SempicGroup();
        g.setName("admins");
        g.setOwner(admin);
        try {
            userFacade.create(admin);
            groupFacade.create(g);
            Logger.getLogger(ApplicationConfig.class.getName()).log(Level.WARNING, "Admin created");
        } catch (SempicModelException e) {
            Logger.getLogger(ApplicationConfig.class.getName()).log(Level.WARNING, "Admin already exists");
        }

    }

}
