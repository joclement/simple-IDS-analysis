package de.tub.insin.ss17.grp1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class AppTest {

    @Before
    public void beforeEachTest() {
        System.out.println("This is executed before each Test");
    }

    @After
    public void afterEachTest() {
        System.out.println("This is exceuted after each Test");
    }

    @Test
    public void testScenarios() {
        String[] argv = {"-s", "1,2,3"};
        App.main(argv);
    }

    @Test(expected = Exception.class)
    public void testNoArgs() throws Exception {
        String[] argv = {};
        App.main(argv);
    }
}
