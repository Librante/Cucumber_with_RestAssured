package assignment.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Setup of the environment and specific for this environment endpoint
 * Environment variable should be set at the VM options
 */
public final class PropertyManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyManager.class);
    private static final Properties properties = new Properties();
    private static final String environment = System.getProperty("env");
    private static final String PROPERTIES = System.getProperty("user.dir") + "/src/main/resources/" + environment + ".properties";



    static {
        File file = new File(PROPERTIES);
        LOGGER.info(file.getAbsolutePath());
        try (InputStream in = new FileInputStream(file)) {
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot start, properties not found.");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
