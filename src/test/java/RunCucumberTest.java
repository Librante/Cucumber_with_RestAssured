import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:build/cucumber", "json:build/cucumber.json"},
        features={"classpath:features"},
glue = {"assignment/stepdefinitions"},
tags = {"@cards"})

public class RunCucumberTest {

    static final Logger LOGGER = LoggerFactory.getLogger(RunCucumberTest.class);

    @BeforeClass
    public static void setUp() {
        LOGGER.info("Current environment is: " + System.getProperty("env"));
    }

}

