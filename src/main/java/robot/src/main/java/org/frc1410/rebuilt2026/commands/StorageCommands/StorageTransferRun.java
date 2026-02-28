package robot.src.main.java.org.frc1410.rebuilt2026.commands.StorageCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Storage;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Storage.StorageStates;

public class StorageTransferRun extends Command{
    private final Storage storage;

    public StorageTransferRun(Storage storage) {
        this.storage = storage;
        
    }

    @Override
    public void initialize() {
        this.storage.setTransferSpeed(1);
        this.storage.setSpeedState(StorageStates.INTAKE);
    }

    @Override
    public void end(boolean interrupted) {
        this.storage.setTransferSpeed(0);
        this.storage.setSpeedState(StorageStates.NEUTRAL);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
