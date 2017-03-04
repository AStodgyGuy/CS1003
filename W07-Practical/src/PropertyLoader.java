import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {

    private Properties properties;

    private String dbURL;
	private String userName;
	private String password;

    public PropertyLoader(String propertiesFileName) throws IOException {
        FileInputStream propInputStream = new FileInputStream(propertiesFileName);
        properties = new Properties();
        properties.load(propInputStream);
        propInputStream.close();

        this.setURL();
        this.setUserName();
        this.setPassword();
    }

    private void setURL() throws IOException {

        String type = properties.getProperty("type");
		String host = properties.getProperty("host");
		String port = properties.getProperty("port");
		String db = properties.getProperty("db");

        this.dbURL = "jdbc:" + type + "://" + host + ":" + port + "/" + db;
    }

    private void setUserName() throws IOException {
        this.userName = properties.getProperty("username");
    }

    private void setPassWord() throws IOException {
        this.password = properties.getProperty("password");
    }

    public String getDBUrl() {
        return dbURL;
    }

    private String getUserName() {
        return userName;
    }

    private String getPassword() {
        return password;
    }	
}
