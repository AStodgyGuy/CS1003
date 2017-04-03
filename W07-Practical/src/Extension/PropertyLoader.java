import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {

    private Properties properties;

    private String dbURL;
    private String username;
    private String password;

    //constructor for PropertyLoader
    public PropertyLoader(String propertiesFileName) throws IOException {
        //load the property file
        FileInputStream propInputStream = new FileInputStream(propertiesFileName);
        properties = new Properties();
        properties.load(propInputStream);
        propInputStream.close();

        //set the fields
        setURL();
        setUserName();
        setPassword();
    }

    //method which sets the connection url, code is adapted from studres
    private void setURL() throws IOException {

        String type = properties.getProperty("type");
        String host = properties.getProperty("host");
        String port = properties.getProperty("port");
        String db = properties.getProperty("db");
        this.dbURL = "jdbc:" + type + "://" + host + ":" + port + "/" + db;
    }

    //method which sets the username for the connection, code is adapted from studres
    private void setUserName() throws IOException {
        this.username = properties.getProperty("username");
    }

    //method which sets the password for the connection, code is adapted from studres
    private void setPassword() throws IOException {
        this.password = properties.getProperty("password");
    }

    //method which returns database url
    public String getDBUrl() {
        return dbURL;
    }

    //method which returns username
    public String getUserName() {
        return username;
    }

    //method which returns password
    public String getPassword() {
        return password;
    }
}
