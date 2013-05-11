package teammates.test.cases;

import static teammates.common.FieldValidator.COURSE_ID_ERROR_MESSAGE;
import static teammates.common.FieldValidator.COURSE_NAME_ERROR_MESSAGE;
import static teammates.common.FieldValidator.REASON_INCORRECT_FORMAT;
import static teammates.common.FieldValidator.REASON_TOO_LONG;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.AssertJUnit;
import static org.testng.AssertJUnit.*;
import teammates.common.Common;
import teammates.common.FieldValidator;
import teammates.common.datatransfer.CourseAttributes;

public class CourseAttributesTest extends BaseTestCase {

	@BeforeClass
	public static void setupClass() throws Exception {
		printTestClassHeader();
	}

	@Test
	public void testValidate() {
		CourseAttributes c = new CourseAttributes();
		
		// SUCCESS: Basic Success Case
		c.id = "valid-id-$_abc";
		c.name = "valid-name";
		
		AssertJUnit.assertTrue(c.isValid());
		
		// FAIL: ID null
		c.id = null;
		try {
			c.getInvalidStateInfo();
			throw new RuntimeException("Assumption violation not detected");
		} catch (AssertionError e) {
			assertTrue(true); //expected
		}
		
		// SUCCESS: ID at max length
		String veryLongId = Common.generateStringOfLength(Common.COURSE_ID_MAX_LENGTH);
		c.id = veryLongId;
		AssertJUnit.assertTrue(c.isValid());
		
		// FAIL: ID too long
		c.id += "a";
		AssertJUnit.assertFalse(c.isValid());
		AssertJUnit.assertEquals(
				String.format(COURSE_ID_ERROR_MESSAGE, c.id, REASON_TOO_LONG),
				c.getInvalidStateInfo());
		
		// FAIL : ID with invalid chars
		c.id = "my-uber-id!";
		AssertJUnit.assertFalse(c.isValid());
		AssertJUnit.assertEquals(
				String.format(COURSE_ID_ERROR_MESSAGE, c.id, REASON_INCORRECT_FORMAT),
				c.getInvalidStateInfo());
		
		// FAIL : Name null
		c.id = "valid-id";
		c.name = null;
		try {
			c.getInvalidStateInfo();
			throw new RuntimeException("Assumption violation not detected");
		} catch (AssertionError e) {
			assertTrue(true); //expected
		}
		
		// SUCCESS: Name at max length
		String veryLongName = Common.generateStringOfLength(FieldValidator.COURSE_NAME_MAX_LENGTH);
		c.name = veryLongName;
		AssertJUnit.assertTrue(c.isValid());
		
		// FAIL : Name too long
		c.name += "e";
		AssertJUnit.assertFalse(c.isValid());
		AssertJUnit.assertEquals(
				c.getInvalidStateInfo(), 
				String.format(COURSE_NAME_ERROR_MESSAGE, c.name, REASON_TOO_LONG));
	}
	
	@Test
	public void testGetInvalidStateInfo(){
	    //already tested in testValidate() above
	}

}