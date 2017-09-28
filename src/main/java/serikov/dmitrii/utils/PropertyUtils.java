package serikov.dmitrii.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class PropertyUtils {

	private static final String APP_PROPERTIES_RESOURCE = "/resources/app.properties";

	private static Map<String, String> propertiesStore;

	public static Map<String, String> readAppProperties() {
		if (propertiesStore != null) {
			return propertiesStore;
		}
		try (InputStream inputStream = PropertyUtils.class.getClassLoader()
				.getResourceAsStream(APP_PROPERTIES_RESOURCE)) {
			Properties prop = new Properties();
			prop.load(inputStream);

			propertiesStore = Maps.fromProperties(prop);
			
		} catch(IOException e){
			propertiesStore = ImmutableMap.of();
		}
		return propertiesStore;
	}

}
