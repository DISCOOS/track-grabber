package no.hvl.dowhile.tests;

import no.hvl.dowhile.utility.StringTools;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringToolsTest {

    private String validName;
    private String invNameParenthesis;
    private String invNameStars;
    private String invNameSlash;
    private String invNameFwdSlash;

    @Before
    public void before() {
        validName = "Jordalstølen Savnet Mann 39";
        invNameParenthesis = "Jordalstølen Savnet Mann (39)";
        invNameStars = "Hardangervidda*SavnetKvinne";
        invNameSlash = "Hardangervidda/SavnetKvinne";
        invNameFwdSlash = "Jotunheimen\\SavnetGutt";
    }

    @Test
    public void validOperationNameIsValid() {
        assertTrue(StringTools.isValidOperationName(validName));
    }

    @Test
    public void parenthesisOperationNameIsInvalid() {
        assertFalse(StringTools.isValidOperationName(invNameParenthesis));
    }

    @Test
    public void starOperationNameIsInvalid() {
        assertFalse(StringTools.isValidOperationName(invNameStars));
    }

    @Test
    public void slashOperationNameIsInvalid() {
        assertFalse(StringTools.isValidOperationName(invNameSlash));
    }

    @Test
    public void fwdSlashOperationNameIsInvalid() {
        assertFalse(StringTools.isValidOperationName(invNameFwdSlash));
    }
}
