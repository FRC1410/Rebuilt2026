package robot.src.main.java.org.frc1410.rebuilt2026.util;

import framework.src.main.java.org.frc1410.framework.control.Controller;

import framework.src.main.java.org.frc1410.framework.control.Axis;
import framework.src.main.java.org.frc1410.framework.control.Button;

public class ControlScheme {

    private Controller driverController;
    private Controller operatorController;

    public Axis DRIVE_FORWARD;
    public Axis DRIVE_SIDEWAYS;
    public Axis DRIVE_TURN;
    public Axis ROBOT_RELATIVE_TOGGLE;

    public Button SLOWMODE_TOGGLE;
    public Button GUARDMODE_TOGGLE;

    public ControlScheme(Controller C1, Controller C2) {
        driverController = C1;
        operatorController = C2;
    }

    public void init() {
        DRIVE_FORWARD = this.driverController.LEFT_Y_AXIS;
        DRIVE_SIDEWAYS = this.driverController.LEFT_X_AXIS;
        DRIVE_TURN = this.driverController.RIGHT_X_AXIS;
        ROBOT_RELATIVE_TOGGLE = this.driverController.RIGHT_TRIGGER;

        SLOWMODE_TOGGLE = this.driverController.LEFT_BUMPER;
        GUARDMODE_TOGGLE = this.driverController.RIGHT_BUMPER;
    }
}
