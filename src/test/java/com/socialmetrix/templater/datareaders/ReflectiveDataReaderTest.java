package com.socialmetrix.templater.datareaders;

import static org.junit.Assert.*;

import org.junit.*;

import com.socialmetrix.templater.TemplaterException;
import com.socialmetrix.templater.datareaders.ReflectiveDataReader;
import com.socialmetrix.templater.datareaders.DataReader.PropertyNotFound;

public class ReflectiveDataReaderTest {

	private ReflectiveDataReader dataReader;
	private SomeClass data;

	@Before
	public void setUp() {
		dataReader = new ReflectiveDataReader();
		data = new SomeClass();
	}

	@Test
	public void canGetPublicBoolProperty() {
		assertTrue((Boolean) dataReader.read(data, "boolProperty1"));
	}

	@Test(expected = PropertyNotFound.class)
	public void cantGetPrivateBoolProperty() {
		dataReader.read(data, "boolProperty2");
	}

	@Test
	public void canGetPublicBoolField() {
		assertTrue((Boolean) dataReader.read(data, "boolField1"));
	}

	@Test(expected = PropertyNotFound.class)
	public void cantGetBoolField() {
		dataReader.read(data, "boolField2");
	}

	@Test
	public void failingPropertiesThrowsWrappedException() {
		try {
			dataReader.read(data, "failingProperty");
		} catch (TemplaterException e) {
			assertTrue(e.getCause() instanceof SomeException);
			return;
		}
		fail();
	}

	@Test
	public void canGetParentProperty() {
		assertEquals("string1", dataReader.read(data, "stringProperty1"));
	}

	@Test
	public void canGetParentField() {
		assertEquals("parentField", dataReader.read(data, "parentField"));
	}

	@Test
	public void canGetByExactMethodName() {
		assertEquals("stringMethod", dataReader.read(data, "stringMethod"));
	}

	@Test
	public void canGetStaticMethod() {
		assertEquals("staticMethod", dataReader.read(data, "staticMethod"));
	}

	@Test(expected = PropertyNotFound.class)
	public void cantGetStaticProperty() {
		dataReader.read(data, "staticProperty");
	}

	@Test
	public void canGetStaticField() {
		assertEquals("staticField", dataReader.read(data, "staticField"));
	}

	@Test
	public void canGetParentStaticField() {
		assertEquals("parentStaticField", dataReader.read(data, "parentStaticField"));
	}

	@SuppressWarnings("unused")
	private static class ParentClass {
		public String parentField = "parentField";
		public String parentStaticField = "parentStaticField";

		public String getStringProperty1() {
			return "string1";
		}
	}

	@SuppressWarnings("unused")
	private static class SomeClass extends ParentClass {
		public boolean boolField1 = true;
		private boolean boolField2 = true;
		public static String staticField = "staticField";

		public boolean isBoolProperty1() {
			return true;
		}

		private boolean isBoolProperty2() {
			return true;
		}

		public String getFailingProperty() {
			throw new SomeException();
		}

		public String stringMethod() {
			return "stringMethod";
		}

		public static String staticMethod() {
			return "staticMethod";
		}

		public static String getStaticProperty() {
			return "staticProperty";
		}
	}

	private static class SomeException extends RuntimeException {
		private static final long serialVersionUID = -4508733590624885902L;
	}

}
