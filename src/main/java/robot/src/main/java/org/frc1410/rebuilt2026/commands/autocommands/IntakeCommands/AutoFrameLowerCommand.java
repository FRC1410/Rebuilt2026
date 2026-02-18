package robot.src.main.java.org.frc1410.rebuilt2026.commands.autocommands.IntakeCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.INTAKE_FRAME_LOWERED_POSITION;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_D;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_I;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_P;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INTAKE_FRAME_TOLERANCE;

public class AutoFrameLowerCommand extends Command {
    private final Intake intake;
    
    private final PIDController leftPID;
    private final PIDController rightPID;
    
    public AutoFrameLowerCommand(Intake intake) {
        this.intake = intake;
        
        this.leftPID = new PIDController(INTAKE_FRAME_P, INTAKE_FRAME_I, INTAKE_FRAME_D);
        this.rightPID = new PIDController(INTAKE_FRAME_P, INTAKE_FRAME_I, INTAKE_FRAME_D);
        
        this.leftPID.setTolerance(INTAKE_FRAME_TOLERANCE);
        this.rightPID.setTolerance(INTAKE_FRAME_TOLERANCE);
        
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.resetFrameEncoders();
        leftPID.setSetpoint(INTAKE_FRAME_LOWERED_POSITION);
        rightPID.setSetpoint(INTAKE_FRAME_LOWERED_POSITION);
    }

    @Override
    public void execute() {
        double leftPosition = intake.getFrameLeftPosition();
        double rightPosition = intake.getFrameRightPosition();
        
        double leftOutput = leftPID.calculate(leftPosition);
        double rightOutput = rightPID.calculate(rightPosition);
        
        leftOutput = Math.max(-1.0, Math.min(1.0, leftOutput));
        rightOutput = Math.max(-1.0, Math.min(1.0, rightOutput));
        
        intake.raiseLowerIntakeFrame(leftOutput, rightOutput);
    }

    @Override
    public boolean isFinished() {
        return leftPID.atSetpoint() && rightPID.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        intake.raiseLowerIntakeFrame(0, 0);
    }
}
