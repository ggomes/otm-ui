/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
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

//        // load the scenario from XML
//        try {
//            myApp.otm.api.load(filename,myApp.params.sim_dt.floatValue(),validate);
//        } catch (Exception e) {
//            throw new OTMException(e);
//        }
//
//        // check
//        if(myApp.otm.scenario()==null)
//            return;
//
//        Scenario scenario = new Scenario(myApp.otm);
//
//        if(scenario!=null)
//            menubar.getScene().getRoot().fireEvent(new ChangeScenarioEvent(scenario));

    }

}
