package framework.src.main.java.org.frc1410.framework.util.log;

import framework.src.main.java.org.frc1410.framework.util.log.transport.Transport;
import framework.src.main.java.org.frc1410.framework.util.log.transport.Transports;

import java.util.*;

public final class Logger {

	private final String name;
	private Set<Transport> transports = Set.of(Transports.CONSOLE);

	public Logger(String name) {
		this.name = name;
	}

	public void transports(Transport... transports) {
		this.transports = Set.of(transports);
	}

	public String getName() {
		return name;
	}

	public void log(Log log) {
		transports.forEach(target -> target.accept(log));
	}

	public void log(LogLevel level, String message) {
		log(new Log(this, level, message));
	}

	public void log(LogLevel level, String format, Object... args) {
		log(new Log(this, level, String.format(format, args)));
	}

	public void debug(String message) {
		log(LogLevel.DEBUG, message);
	}

	public void info(String message) {
		log(LogLevel.INFO, message);
	}

	public void warn(String message) {
		log(LogLevel.WARN, message);
	}

	public void error(String message) {
		log(LogLevel.ERROR, message);
	}

	public void fatal(String message) {
		log(LogLevel.FATAL, message);
	}

	public void debug(String format, Object... args) {
		log(LogLevel.DEBUG, format, args);
	}

	public void info(String format, Object... args) {
		log(LogLevel.INFO, format, args);
	}

	public void warn(String format, Object... args) {
		log(LogLevel.WARN, format, args);
	}

	public void error(String format, Object... args) {
		log(LogLevel.ERROR, format, args);
	}

	public void fatal(String format, Object... args) {
		log(LogLevel.FATAL, format, args);
	}
}