package robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.control.Axis;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.INTAKE_FRAME_LOWERED_POSITION;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.INTAKE_FRAME_RAISED_POSITION;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_D;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_I;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_P;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_TOLERANCE;



public class IntakeCommand extends Command{
    private final Intake intake;
    
    private final PIDController leftPID;
    private final PIDController rightPID;
    
    private boolean isRaised = false;

    public IntakeCommand(Intake intake) {
        this.intake = intake;

        this.leftPID = new PIDController(INTAKE_FRAME_P, INTAKE_FRAME_I, INTAKE_FRAME_D);
        this.rightPID = new PIDController(INTAKE_FRAME_P, INTAKE_FRAME_I, INTAKE_FRAME_D);
        
        this.leftPID.setTolerance(INTAKE_FRAME_TOLERANCE);
        this.rightPID.setTolerance(INTAKE_FRAME_TOLERANCE);
    }

    @Override
    public void initialize() {
        // this.testSpark.setSpeed(1);
    }

    @Override
    public void execute() {
        this.intake.setSpeed(1);
        System.out.println(this.intake.getSpeed());

        if (!isRaised) {
            isRaised = true;
            leftPID.setSetpoint(INTAKE_FRAME_RAISED_POSITION);
            rightPID.setSetpoint(INTAKE_FRAME_RAISED_POSITION);
        }
        
        double leftPosition = intake.getFrameLeftPosition();
        double rightPosition = intake.getFrameRightPosition();
        
        double leftOutput = leftPID.calculate(leftPosition);
        double rightOutput = rightPID.calculate(rightPosition);
        
        leftOutput = Math.max(-1.0, Math.min(1.0, leftOutput));
        rightOutput = Math.max(-1.0, Math.min(1.0, rightOutput));
        
        intake.raiseLowerIntakeFrame(leftOutput, rightOutput);
    }

    // @Override
    // public boolean isFinished() {
    
    //     return !this.toggleButton.button().isActive();
    // }

    @Override
    public void end(boolean interrupted) {
        if (this.intake.getSpeed() == 1) {
            this.intake.setSpeed(0);
        }
        
        leftPID.setSetpoint(INTAKE_FRAME_RAISED_POSITION);
        rightPID.setSetpoint(INTAKE_FRAME_RAISED_POSITION);

        double leftPosition = intake.getFrameLeftPosition();
        double rightPosition = intake.getFrameRightPosition();
        
        double leftOutput = leftPID.calculate(leftPosition);
        double rightOutput = rightPID.calculate(rightPosition);
        
        leftOutput = Math.max(-1.0, Math.min(1.0, leftOutput));
        rightOutput = Math.max(-1.0, Math.min(1.0, rightOutput));
        
        intake.raiseLowerIntakeFrame(leftOutput, rightOutput);
    }
}
