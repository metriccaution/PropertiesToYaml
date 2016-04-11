package com.github.metriccaution.propertiesConversion;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Has either a value, or children
 * <p>
 * Really should be two objects, but this is more about getting something quick
 * than best practice
 */
// TODO - Be less lazy
public class Property {
	private final Object value;
	private final Map<String, Property> children;

	public Property(final Properties properties) {
		value = null;
		children = Maps.newHashMap();

		for (final Entry<Object, Object> property : properties.entrySet()) {
			addProperty(property.getKey().toString(), property.getValue());
		}
	}

	private Property() {
		value = null;
		children = Maps.newHashMap();
	}

	private Property(final Object value) {
		// TODO - Validate object
		this.value = value;
		children = null;
	}

	public void addProperty(final String key, final Object value) {
		if (value == null)
			throw new NullPointerException("Appending null property to model");

		final List<String> splitKey = Lists.newArrayList(key.split("\\."));
		try {
			addProperty(splitKey, value);
		} catch (final Exception e) {
			final String message = "Could not append property " + key + " to "
					+ toString();
			throw new IllegalArgumentException(message, e);
		}
	}

	/**
	 * Travel down the tree until it's either invalid or we can find a home for
	 * the object
	 *
	 * @param key
	 *            The chain of properties
	 * @param value
	 *            The property
	 */
	private void addProperty(final List<String> key, final Object value) {
		final String currentKey = key.get(0);

		if (key.size() == 1) {
			children.put(currentKey, new Property(value));
			return;
		}

		if (children == null)
			throw new IllegalArgumentException(
					"Attempting to append children to a value node");

		if (!children.containsKey(currentKey))
			children.put(currentKey, new Property());

		final Property child = children.get(currentKey);
		child.addProperty(key.subList(1, key.size()), value);
	}

	public Object getValue() {
		return value;
	}

	public Map<String, Property> getChildren() {
		return Maps.newHashMap(children);
	}

	/**
	 * A quick and dirty replacement for writing a {@link JsonSerializer} and
	 * plugging it into Jackson YAML
	 *
	 * @return The object as a node
	 */
	public JsonNode toJsonNode() {
		if (value != null) {
			return Utils.mapper().convertValue(value, JsonNode.class);
		} else if (children != null) {

			final Map<String, JsonNode> ret = Maps.newHashMap();

			for (final Entry<String, Property> child : children.entrySet()) {
				ret.put(child.getKey(), child.getValue().toJsonNode());
			}

			return Utils.mapper().convertValue(ret, JsonNode.class);
		} else {
			throw new IllegalStateException(
					"Property has neither children or a value");
		}
	}

	@Override
	public String toString() {
		if (value != null)
			return value.toString();
		else if (children != null)
			return children.toString();
		else
			throw new IllegalStateException(
					"Property has neither children or a value");
	}
}
