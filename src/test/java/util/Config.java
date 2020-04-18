package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String CONFIG_FILE = "config.properties";

    private Config() {
    }

    public static String getBaseURI() {
        return getProps().getProperty("baseURI");
    }

    public static String getToken() {
        return getProps().getProperty("token");
    }


    private static Properties _props = null;

    private static Properties getProps() {
        if (_props == null) {
            _props = new Properties();
            try (InputStream propInput = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
                _props.load(propInput);
            } catch (IOException | NullPointerException e) {
                throw new RuntimeException("config.properties file not found in classpath", e);
            }
        }
        return _props;
    }
}
