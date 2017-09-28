package serikov.dmitrii.utils;

public class LoggerFactory {

	public static Logger getLogger(String className) {
		return new Logger(className);
	}
	
	public static Logger getLogger(Class<?> c) {
		return new Logger(c);		
	}
	
}
