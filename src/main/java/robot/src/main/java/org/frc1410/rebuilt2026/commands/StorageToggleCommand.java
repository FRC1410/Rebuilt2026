package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Storage;

public class StorageToggleCommand extends Command {
    private final Storage storage;

    private final Storage.StorageStates storageState;

    public StorageToggleCommand(Storage storage, Storage.StorageStates storageState) {
        this.storage = storage;
        this.storageState = storageState;
    }

    @Override
    public void initialize() {
        this.storage.setSpeedState(this.storageState);
        System.out.println("Trying storage thing to " + this.storageState);
    }
}
