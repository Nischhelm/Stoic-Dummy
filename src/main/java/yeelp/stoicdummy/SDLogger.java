package yeelp.stoicdummy;

import org.apache.logging.log4j.Logger;

import yeelp.stoicdummy.config.ModConfig;

/**
 * Logger for Stoic Dummy
 * @author Yeelp
 *
 */
public final class SDLogger {
	private static Logger logger;
	
	/**
	 * Initialize the logger
	 * @param logger The logger the SDLogger will use.
	 */
	public static void init(Logger logger) {
		SDLogger.logger = logger;
	}
	
	public static void debug(String msg) {
		if(ModConfig.debug) {
			info(msg);
		}
	}
	
	public static void debug(String msg, Object...objects) { 
		if(ModConfig.debug) {
			info(msg, objects);
		}
	}
	
	public static void info(String msg) {
		logger.info(msg);
	}
	
	public static void info(String msg, Object...objects) {
		logger.info(msg, objects);
	}
	
	public static void warn(String msg) {
		logger.warn(msg);
	}
	
	public static void warn(String msg, Object...objects) {
		logger.warn(msg, objects);
	}
	
	public static void err(String msg) {
		logger.error(msg);
	}
	
	public static void err(String msg, Object...objects) {
		logger.error(msg, objects);
	}
	
	public static void fatal(String msg) {
		logger.fatal(msg);
	}

}
