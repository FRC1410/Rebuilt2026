package robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.INTAKE_LEFT_FRAME_LOWERED_POSITION;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.INTAKE_LEFT_FRAME_RAISED_POSITION;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.INTAKE_RIGHT_FRAME_RAISED_POSITION;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_D;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_I;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_P;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_TOLERANCE;


public class IntakeAutoCommand extends Command {

    private final Intake intake;
    private final double intakeSpeed;

    private final PIDController leftPID;
    private final PIDController rightPID;

    private boolean isRaised = false;

    public IntakeAutoCommand(Intake intake, double intakeSpeed) {
        this.intake = intake;
        this.intakeSpeed = intakeSpeed;

        this.leftPID = new PIDController(INTAKE_FRAME_P, INTAKE_FRAME_I, INTAKE_FRAME_D);
        this.rightPID = new PIDController(INTAKE_FRAME_P, INTAKE_FRAME_I, INTAKE_FRAME_D);

        this.leftPID.setTolerance(INTAKE_FRAME_TOLERANCE);
        this.rightPID.setTolerance(INTAKE_FRAME_TOLERANCE);

        addRequirements();
    }

    @Override
    public void initialize() {
        isRaised = false;
        leftPID.setSetpoint(INTAKE_LEFT_FRAME_RAISED_POSITION);
        rightPID.setSetpoint(INTAKE_RIGHT_FRAME_RAISED_POSITION);
    }

    @Override
    public void execute() {
        // Intake button is always "held" for the full duration of this command
        this.intake.setSpeed(intakeSpeed);
        this.intake.setTestSpeed(intakeSpeed);
        if (!isRaised) {
            isRaised = true;
            leftPID.setSetpoint(INTAKE_LEFT_FRAME_LOWERED_POSITION);
            rightPID.setSetpoint(INTAKE_LEFT_FRAME_LOWERED_POSITION);
        }

        double leftPosition = intake.getFrameLeftPosition();
        double rightPosition = intake.getFrameRightPosition();

        double leftOutput = leftPID.calculate(leftPosition);
        double rightOutput = rightPID.calculate(rightPosition);

        leftOutput = Math.max(-1.0, Math.min(1.0, leftOutput));
        rightOutput = Math.max(-1.0, Math.min(1.0, rightOutput));

        intake.raiseLowerIntakeFrame(leftOutput, rightOutput);
    }

    @Override
    public void end(boolean interrupted) {
        if (this.intake.getSpeed() == intakeSpeed) {
            this.intake.setSpeed(0);
            this.intake.setTestSpeed(0);
        }
        // Raise the frame back up
        if (isRaised) {
            isRaised = false;
            leftPID.setSetpoint(INTAKE_LEFT_FRAME_RAISED_POSITION);
            rightPID.setSetpoint(INTAKE_RIGHT_FRAME_RAISED_POSITION);

            double leftOutput = Math.max(-1.0, Math.min(1.0, leftPID.calculate(intake.getFrameLeftPosition())));
            double rightOutput = Math.max(-1.0, Math.min(1.0, rightPID.calculate(intake.getFrameRightPosition())));
            intake.raiseLowerIntakeFrame(leftOutput, rightOutput);
        }
    }

    @Override
    public boolean isFinished() {
        return false; // Runs until interrupted by a race/deadline group in the auto
    }
}
