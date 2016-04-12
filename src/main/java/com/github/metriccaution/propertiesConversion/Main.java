package com.github.metriccaution.propertiesConversion;

import java.util.Properties;

public class Main {

	public static void main(final String[] args) throws Exception {
		final String fromFile = args[0];
		if (fromFile == null)
			throw new IllegalArgumentException("No file provided");

		final Properties props = Utils.readProperties(fromFile);
		final Property parsed = new Property(props);

		final String yamlString = Utils.mapper().writeValueAsString(
				parsed.toJsonNode());

		final String quotesRemoved = yamlString.replaceAll(": \"", ":")
				.replaceAll("\\s*\"", "");

		System.out.println(quotesRemoved);
	}
}
