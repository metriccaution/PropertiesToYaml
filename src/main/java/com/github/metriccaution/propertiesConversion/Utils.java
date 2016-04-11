package com.github.metriccaution.propertiesConversion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Helper methods
 */
public class Utils {
	private Utils() {
		// Prevent instantiation
	}

	public static Properties readProperties(final String fileLocation)
			throws IOException {

		final Properties props = new Properties();
		try (InputStream is = new FileInputStream(new File(fileLocation))) {
			props.load(is);
		}

		return props;
	}

	public static ObjectMapper mapper() {
		return new ObjectMapper(new YAMLFactory());
	}
}
