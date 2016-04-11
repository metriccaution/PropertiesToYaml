package com.github.metriccaution.propertiesConversion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

// TODO - Actually assert instead of manually inspecting test cases
public class ParseTest {

	private Properties readTestFile(final String file) throws IOException {
		final Properties props = new Properties();
		try (InputStream is = this.getClass().getResourceAsStream('/' + file)) {
			props.load(is);
		}

		return props;
	}

	@Test
	public void dataTypes() throws IOException {
		final Properties props = readTestFile("dataTypes.properties");
		System.out.println(Utils.mapper().writeValueAsString(
				new Property(props).toJsonNode()));
	}

	@Test
	public void testDouble() throws IOException {
		final Properties props = readTestFile("doubleConfig.properties");
		System.out.println(Utils.mapper().writeValueAsString(
				new Property(props).toJsonNode()));
	}

	@Test
	public void basic() throws IOException {
		final Properties props = readTestFile("example.properties");
		System.out.println(Utils.mapper().writeValueAsString(
				new Property(props).toJsonNode()));
	}

	@Test
	public void nesting() throws IOException {
		final Properties props = readTestFile("nesting.properties");
		System.out.println(Utils.mapper().writeValueAsString(
				new Property(props).toJsonNode()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void nodeWithProps() throws IOException {
		final Properties props = readTestFile("nodeWithProperties.properties");
		new Property(props).toJsonNode();
	}
}
