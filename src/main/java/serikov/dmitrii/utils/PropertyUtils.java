package serikov.dmitrii.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyUtils {

	private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

	private static final String APP_PROPERTIES_RESOURCE = "/WEB-INF/classes/app.properties";

	private static Map<String, String> propertiesStore = new HashMap<>();

	public static Map<String, String> readAppProperties() {
		if (!propertiesStore.isEmpty()) {
			return propertiesStore;
		}
		try (InputStream inputStream = PropertyUtils.class.getClassLoader()
				.getResourceAsStream(APP_PROPERTIES_RESOURCE)) {
			if (inputStream != null) {
				Properties prop = new Properties();
				prop.load(inputStream);
				prop.forEach((key, value) -> propertiesStore.put(key.toString(), value.toString()));
			} else {
				logger.error(String.format("Cannot find \"%s\"", APP_PROPERTIES_RESOURCE));
			}
		} catch (IOException e) {
		}
		return propertiesStore;
	}

}
