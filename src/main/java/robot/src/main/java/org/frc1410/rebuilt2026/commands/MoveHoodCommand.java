package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.LEDs;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.LEDs.Color;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot.HoodStates;

public class MoveHoodCommand extends Command {

    private final Shoot shoot;
    private final HoodStates hoodstate;
    private final LEDs lEDs;

    public MoveHoodCommand(Shoot shoot, HoodStates hoodState, LEDs lEDs) {
        this.shoot = shoot;
        this.hoodstate = hoodState;
        this.lEDs = lEDs;
    }

    @Override
    public void initialize() {
        this.shoot.setHoodPos(hoodstate);
        switch (hoodstate) {
            case LOW_LEFT:
                this.lEDs.setColor(Color.RED);
                break;
            case LOW_RIGHT:
                this.lEDs.setColor(Color.GREEN);
                break;
            case HIGH_LEFT:
                this.lEDs.setColor(Color.BLUE);
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
