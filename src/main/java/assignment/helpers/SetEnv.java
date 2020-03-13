package assignment.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetEnv {

    static final Logger LOGGER = LoggerFactory.getLogger(SetEnv.class);

    public static void defineBaseURLFromPropertiesFile(DataStorage dataStorage) {
        String baseURL = PropertyManager.getProperty("base.url");
        dataStorage.setBaseURL(baseURL);
        LOGGER.info("current BaseURL is {}", baseURL);
    }
}
