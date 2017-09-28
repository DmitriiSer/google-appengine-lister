package serikov.dmitrii.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

public class PropertyUtils {

	public static final String APP_PROPERTIES_RESOURCE = "/WEB-INF/classes/app.properties";

	private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

	private static Map<String, String> propertiesStore = new HashMap<>();

	public static void loadAppProperties(ServletContext context) {
		try (InputStream inputStream = context.getResourceAsStream(PropertyUtils.APP_PROPERTIES_RESOURCE)) {
			if (inputStream != null) {
				Properties prop = new Properties();
				prop.load(inputStream);
				prop.forEach((key, value) -> propertiesStore.put(key.toString(), value.toString()));
			} else {
				logger.error(String.format("Cannot find \"%s\"", APP_PROPERTIES_RESOURCE));
			}
		} catch (IOException e) {
		}
	}
	
	public static Map<String, String> getAppProperties(){
		return propertiesStore;
	}

}
