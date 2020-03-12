package Helpers;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetEnv {

    static DataStorage dataStorage = new DataStorage();
    static Logger LOGGER = LoggerFactory.getLogger(SetEnv.class);


    public static void defineBaseURLFromPropertiesFile() {
        String baseURL = PropertyManager.getProperty("base.url");
        dataStorage.setBaseURL(baseURL);
        LOGGER.info("current BaseURL is", baseURL);
    }
}
