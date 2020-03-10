import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features={"classpath:features"},
glue = {"StepDefinitions"},
tags = {"@wip"})

public class RunCucumberTest {
}
