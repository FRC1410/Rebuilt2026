package framework.src.main.java.org.frc1410.framework;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.Objects;
import java.util.function.Supplier;

public record AutoProfile(String name, Supplier<Command> supplier, int id) {
	public AutoProfile {
		Objects.requireNonNull(name);
		Objects.requireNonNull(supplier);
	}
}