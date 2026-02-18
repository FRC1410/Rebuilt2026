package robot.src.main.java.org.frc1410.rebuilt2026.commands.autocommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Storage;

public class AutoStorageToggleCommand extends Command {
    private final Storage storage;

    public AutoStorageToggleCommand(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void initialize() {
        // Default to INTAKE state for auto
        this.storage.setSpeedState(Storage.StorageStates.INTAKE);
        System.out.println("Trying storage thing to " + Storage.StorageStates.INTAKE);
    }
}
