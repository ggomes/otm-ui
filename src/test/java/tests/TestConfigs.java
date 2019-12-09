package tests;

import error.OTMException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import xml.JaxbLoader;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class TestConfigs {

    String testname;

    public TestConfigs(String testname){
        this.testname = testname;
    }

    @Parameterized.Parameters
    public static Collection getConfigs(){
        ArrayList<String []> x = new ArrayList<>();
        for(String s : JaxbLoader.get_test_config_names())
            x.add(new String[]{s});
        return x;
    }

    @Test
    public void test_load_testconfig() {
        try {
            jaxb.Scenario scenario = JaxbLoader.load_test_scenario(JaxbLoader.get_test_filename(testname), true);
            assertNotNull(scenario);
        } catch (OTMException e) {
            e.printStackTrace();
            fail();
        }
    }

}
