package frc.robot.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ConfigTest {

    /**
     * This is a silly test. I've placed it here as a trivial example of a test.
     * Try changing the expected value of 4 to be 5, and then try to rebuild the
     * project.
     * 
     * A failing test should prevent the whole build from succeeding. If a test
     * fails, the error messages should tell you exactly which line of which test
     * failed.
     */
    @Test
    public void testReality() {
        assertEquals(2 + 2, 4);
    }

    /**
     * Anything that is even mildly tricky or non-obvious is a candidate for
     * testing. In this case, I think regular expressions are often tricky enough to
     * deserve testing. Be sure to add test cases for all obvious situations (such
     * as "true" and "0") as well as edge cases (such as empty strings and
     * double-negative numbers).
     */
    @Test
    public void testRegularExpressions() {

        // boolean values must be either lower-case "true" or "false"
        assertFalse("".matches(Config.BOOLEAN));
        assertTrue("true".matches(Config.BOOLEAN));
        assertTrue("false".matches(Config.BOOLEAN));
        assertFalse("yes".matches(Config.BOOLEAN));
        assertFalse("1".matches(Config.BOOLEAN));
        assertFalse("1234L".matches(Config.BOOLEAN));

        // integers are a string of digits, with an option minus sign
        assertFalse("".matches(Config.INTEGER));
        assertTrue("0".matches(Config.INTEGER));
        assertTrue("1".matches(Config.INTEGER));
        assertTrue("-0".matches(Config.INTEGER));
        assertTrue("-1234".matches(Config.INTEGER));
        assertFalse("--1234".matches(Config.INTEGER));
        assertFalse("1234L".matches(Config.INTEGER));
        assertFalse("3.14".matches(Config.INTEGER));
        assertFalse("1234F".matches(Config.INTEGER));
        assertFalse("12 34".matches(Config.INTEGER));
        assertFalse("1234D".matches(Config.INTEGER));
        assertFalse("true".matches(Config.INTEGER));

        // For the config files, long integers must end in "L"
        assertFalse("".matches(Config.LONG));
        assertFalse("1".matches(Config.LONG));
        assertFalse("-1234".matches(Config.LONG));
        assertTrue("1234L".matches(Config.LONG));
        assertTrue("-1234L".matches(Config.LONG));
        assertTrue("1L".matches(Config.LONG));
        assertFalse("3.14".matches(Config.LONG));
        assertFalse("1234F".matches(Config.LONG));
        assertFalse("1234D".matches(Config.LONG));
        assertFalse("true".matches(Config.LONG));

        // Floats in config files must end in "F" and must contain a decimal point
        assertFalse("".matches(Config.FLOAT));
        assertFalse("1".matches(Config.FLOAT));
        assertFalse("-1234".matches(Config.FLOAT));
        assertTrue("1234.F".matches(Config.FLOAT));
        assertTrue("-1234.0F".matches(Config.FLOAT));
        assertTrue("234.000F".matches(Config.FLOAT));
        assertFalse("3.14".matches(Config.FLOAT));
        assertTrue("3.14F".matches(Config.FLOAT));
        assertFalse("3.14.0F".matches(Config.FLOAT));
        assertFalse("1234D".matches(Config.FLOAT));
        assertFalse("true".matches(Config.FLOAT));

        // Doubles in config files must contain a decimal point and may end in "D"
        assertFalse("".matches(Config.DOUBLE));
        assertFalse("1".matches(Config.DOUBLE));
        assertFalse("-1234".matches(Config.DOUBLE));
        assertTrue("-1234.".matches(Config.DOUBLE));
        assertTrue("0.987".matches(Config.DOUBLE));
        assertTrue("1234.D".matches(Config.DOUBLE));
        assertTrue("-1234.0D".matches(Config.DOUBLE));
        assertTrue("234.000D".matches(Config.DOUBLE));
        assertTrue("3.14".matches(Config.DOUBLE));
        assertFalse("3.14.2".matches(Config.DOUBLE));
        assertTrue("3.14D".matches(Config.DOUBLE));
        assertFalse("1234.3F".matches(Config.DOUBLE));
        assertFalse("true".matches(Config.DOUBLE));
    }

}