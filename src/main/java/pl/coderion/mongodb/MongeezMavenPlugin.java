package pl.coderion.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.mongeez.Mongeez;
import org.mongeez.MongoAuth;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;
import org.springframework.core.io.FileSystemResource;

import com.mongodb.Mongo;

@Mojo(name = "update")
public class MongeezMavenPlugin extends AbstractMojo {

    @Parameter(property = "update.changeLogFile", defaultValue = "src/main/mongeez/mongeez.xml")
    private File changeLogFile;
    
    @Parameter(property = "auth.db.name")
    private String        authDbName;

    @Parameter(property = "db.hostname")
    private String              dbHostName;

    @Parameter(property = "dbName")
    private String              dbName;

    @Parameter(property = "db.port")
    private String              dbPort;

    @Parameter(property = "db.password")
    private String              password;

    @Parameter(property = "update.propertyFile", defaultValue = "src/main/mongeez/config.properties")
    private File propertyFile;

    @Component(role = org.sonatype.plexus.components.sec.dispatcher.SecDispatcher.class, hint = "default")
    private SecDispatcher       securityDispatcher;

    @Parameter(defaultValue = "false")
    private boolean             skip;

    @Parameter(property = "db.username")
    private String              username;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {

            final Properties properties = new Properties();

            if (null != propertyFile) {
                final FileInputStream propertyFileInputStream = new FileInputStream(propertyFile);
                properties.load(propertyFileInputStream);
            }

            // use the property for parent projects (pom only) or for ignoring execution completely
            if (skip) {
                getLog().info("Skip Property found .. not executing.");
            }
            else {
                try {
                    setPassword(securityDispatcher.decrypt(getPassword()));
                }
                catch (final SecDispatcherException e) {
                    throw new MojoExecutionException(e.getMessage());
                }
                final Mongeez mongeez = new Mongeez();
                mongeez.setFile(new FileSystemResource(changeLogFile));
                mongeez.setMongo(new Mongo(getDbHostName(), Integer.valueOf(getDbPort())));
                mongeez.setAuth(new MongoAuth(getUsername(), getPassword(), getAuthDbName()));
                mongeez.setDbName(getDbName());
                mongeez.process();
            }
        } catch (final FileNotFoundException e) {
            getLog().error(String.format("Configuration file %s not found", propertyFile.getAbsolutePath()));
            throw new RuntimeException();

        } catch (final IOException e) {
            getLog().error(String.format("An error occured during loading configuration file %s: %s",
                    propertyFile.getAbsolutePath(), e.getMessage()));
            throw new RuntimeException();
        } catch (final Exception e) {
            getLog().error(String.format("An unknown error occured: %s", e.getMessage()));
            throw new RuntimeException();
        }
    }
    public String getAuthDbName() {
        return authDbName;
    }
    public String getDbHostName() {
        return dbHostName;
    }
    public String getDbName() {
        return dbName;
    }
    public String getDbPort() {
        return dbPort;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public void setAuthDbName(String authDbName) {
        this.authDbName = authDbName;
    }
    public void setDbHostName(String dbHostName) {
        this.dbHostName = dbHostName;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
