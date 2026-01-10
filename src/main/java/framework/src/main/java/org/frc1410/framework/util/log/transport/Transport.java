package framework.src.main.java.org.frc1410.framework.util.log.transport;

import framework.src.main.java.org.frc1410.framework.util.log.Log;

@FunctionalInterface
public interface Transport {

	void accept(Log log);
}