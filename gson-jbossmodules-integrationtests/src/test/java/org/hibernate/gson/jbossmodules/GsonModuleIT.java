package org.hibernate.gson.jbossmodules;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Gson module testing inside Wildfly
 */
@RunWith(Arquillian.class)
public class GsonModuleIT {

	private static final VersionsHelper helper = new VersionsHelper();

	@Deployment
	public static Archive<?> deployment() {
		String dependencies = "com.google.code.gson:" + helper.getGsonModuleSlot();
		StringAsset manifest = new StringAsset(Descriptors.create(ManifestDescriptor.class)
				.attribute("Dependencies", dependencies)
				.exportAsString());
		return ShrinkWrap.create(WebArchive.class, GsonModuleIT.class.getSimpleName() + ".war")
				.addClasses(GsonModuleIT.class, VersionsHelper.class)
				.add(manifest, "META-INF/MANIFEST.MF");
	}

	@Test
	public void formatting() {
		JsonObject object = new JsonObject();
		object.addProperty("string", "stringValue");
		object.addProperty("integer", 42);
		JsonArray array = new JsonArray();
		array.add(1);
		array.add(2);
		object.add("array", array);

		Assert.assertEquals(
				"{\"string\":\"stringValue\",\"integer\":42,\"array\":[1,2]}",
				new Gson().toJson(object)
		);
	}

	@Test
	public void parsing() {
		JsonObject object = new Gson().fromJson(
				"{\"string\":\"stringValue\",\"null\":null}",
				JsonObject.class
		);

		Assert.assertEquals(2, object.size());
		Assert.assertEquals("stringValue", object.getAsJsonPrimitive("string").getAsString());
		Assert.assertEquals(JsonNull.INSTANCE, object.get("null"));
	}

}
