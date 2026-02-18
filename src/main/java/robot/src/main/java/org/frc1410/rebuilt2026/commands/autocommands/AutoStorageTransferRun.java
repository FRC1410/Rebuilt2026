package robot.src.main.java.org.frc1410.rebuilt2026.commands.autocommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Storage;

public class AutoStorageTransferRun extends Command{
    private final Storage storage;

    public AutoStorageTransferRun(Storage storage) {
        this.storage = storage;
        
    }

    @Override
    public void initialize() {
        this.storage.setTransferSpeed(1.0);
    }

    @Override
    public void end(boolean interrupted) {
        this.storage.setTransferSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
