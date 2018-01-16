package org.hibernate.gson.jbossmodules;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class VersionsHelper {

	public String getGsonModuleSlot() {
		return getPropertiesVariable("gson.slot.id");
	}

	private static String getPropertiesVariable(String key) {
		Properties projectCompilationProperties = new Properties();
		final InputStream resourceAsStream = VersionsHelper.class.getClassLoader()
				.getResourceAsStream("module-versions.properties");
		try {
			projectCompilationProperties.load(resourceAsStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				resourceAsStream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return projectCompilationProperties.getProperty(key);
	}

}
