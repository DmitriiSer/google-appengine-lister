package serikov.dmitrii.utils;

public class Logger {

	java.util.logging.Logger logger;

	public Logger(String className) {
		logger = java.util.logging.Logger.getLogger(className);
	}

	public Logger(Class<?> c) {
		logger = java.util.logging.Logger.getLogger(c.getName());
	}

	public void info(String message) {
		logger.info(message);
	}

	public void error(String message) {
		logger.severe(message);
	}

}
