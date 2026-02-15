package robot.src.main.java.org.frc1410.rebuilt2026.util;

import framework.src.main.java.org.frc1410.framework.control.Axis;
import framework.src.main.java.org.frc1410.framework.control.Button;
import framework.src.main.java.org.frc1410.framework.control.Controller;

public class ControlScheme {

    private Controller driverController;
    private Controller operatorController;

    public Axis DRIVE_FORWARD;
    public Axis DRIVE_SIDEWAYS;
    public Axis DRIVE_TURN;
    public Button ROBOT_RELATIVE_TOGGLE;

    public Axis INTAKE_FORWARD;
    public Axis INTAKE_REVERSE;
    public Button FRAME_TEST_1;
    public Button FRAME_TEST_2;
    public Button FRAME_RAISE;
    public Button FRAME_LOWER;

    public Button STORAGE_INTAKE;
    public Button STORAGE_NEUTRAL;
    public Button STORAGE_OUTTAKE;
    public Button TRANSFER;
 
    public Button SHOOTER_UP;
    public Button SHOOTER_DOWN;

    public Button HOOD_LOW_LEFT;
    public Button HOOD_LOW_RIGHT;
    public Button HOOD_HIGH_LEFT;

    public Button AUTO_ALIGN;

    public Button SLOWMODE_TOGGLE;
    public Button GUARDMODE_TOGGLE;

    public ControlScheme(Controller C1, Controller C2) {
        driverController = C1;
        operatorController = C2;
        DRIVE_FORWARD = this.driverController.LEFT_Y_AXIS;
        DRIVE_SIDEWAYS = this.driverController.LEFT_X_AXIS;
        DRIVE_TURN = this.driverController.RIGHT_X_AXIS;
        ROBOT_RELATIVE_TOGGLE = this.driverController.RIGHT_BUMPER;

        INTAKE_FORWARD = this.driverController.LEFT_TRIGGER;
        INTAKE_REVERSE = this.driverController.RIGHT_TRIGGER;
        FRAME_TEST_1 = this.operatorController.DPAD_LEFT;
        FRAME_TEST_2 = this.operatorController.DPAD_RIGHT;
        FRAME_RAISE = this.operatorController.DPAD_UP;
        FRAME_LOWER = this.operatorController.DPAD_DOWN;

        STORAGE_INTAKE = this.operatorController.A;
        STORAGE_NEUTRAL = this.operatorController.B;
        STORAGE_OUTTAKE = this.operatorController.X;
        TRANSFER = this.operatorController.Y;
    
        SHOOTER_UP = this.operatorController.RIGHT_BUMPER;
        SHOOTER_DOWN = this.operatorController.LEFT_BUMPER;

        HOOD_LOW_LEFT = this.operatorController.RIGHT_TRIGGER.button();
        HOOD_LOW_RIGHT = this.operatorController.LEFT_TRIGGER.button();
        HOOD_HIGH_LEFT = this.operatorController.RIGHT_STICK;

        AUTO_ALIGN = this.driverController.LEFT_STICK;


        SLOWMODE_TOGGLE = this.driverController.X;
        GUARDMODE_TOGGLE = this.driverController.Y;
    }
}
