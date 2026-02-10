package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import java.util.Optional;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DriverStation;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.SubsystemStore;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.BACK_LEFT_DRIVE_MOTOR_INVERTED;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.BACK_LEFT_STEER_ENCODER_OFFSET;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.BACK_LEFT_STEER_MOTOR_INVERTED;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.BACK_RIGHT_DRIVE_MOTOR_INVERTED;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.BACK_RIGHT_STEER_ENCODER_OFFSET;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.BACK_RIGHT_STEER_MOTOR_INVERTED;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.FRONT_LEFT_DRIVE_MOTOR_INVERTED;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.FRONT_LEFT_STEER_ENCODER_OFFSET;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.FRONT_LEFT_STEER_MOTOR_INVERTED;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.FRONT_RIGHT_DRIVE_MOTOR_INVERTED;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.FRONT_RIGHT_STEER_ENCODER_OFFSET;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.FRONT_RIGHT_STEER_MOTOR_INVERTED;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.SWERVE_DRIVE_KINEMATICS;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.SWERVE_DRIVE_MAX_SPEED;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.BACK_LEFT_DRIVE_MOTOR;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.BACK_LEFT_STEER_ENCODER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.BACK_LEFT_STEER_MOTOR;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.BACK_RIGHT_DRIVE_MOTOR;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.BACK_RIGHT_STEER_ENCODER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.BACK_RIGHT_STEER_MOTOR;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.FRONT_LEFT_DRIVE_MOTOR;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.FRONT_LEFT_STEER_ENCODER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.FRONT_LEFT_STEER_MOTOR;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.FRONT_RIGHT_DRIVE_MOTOR;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.FRONT_RIGHT_STEER_ENCODER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.FRONT_RIGHT_STEER_MOTOR;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.PIGEON_ID;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;

public class Drivetrain implements TickedSubsystem {
    private final NetworkTable table = NetworkTableInstance.getDefault().getTable("Drivetrain");

    private final DoublePublisher frontLeftVelocitySetpoint = NetworkTables.PublisherFactory(this.table, "Front Left Velocity Setpoint", 0);
    private final DoublePublisher frontRightVelocitySetpoint = NetworkTables.PublisherFactory(this.table, "Front Right Velocity Setpoint", 0);
    private final DoublePublisher backLeftVelocitySetpoint = NetworkTables.PublisherFactory(this.table, "Back Left Velocity Setpoint", 0);
    private final DoublePublisher backRightVelocitySetpoint = NetworkTables.PublisherFactory(this.table, "Back Right Velocity Setpoint", 0);

    private final DoublePublisher frontLeftAngleSetpoint = NetworkTables.PublisherFactory(this.table, "Front Left Angle Setpoint", 0);
    private final DoublePublisher frontRightAngleSetpoint = NetworkTables.PublisherFactory(this.table, "Front Right Angle Setpoint", 0);
    private final DoublePublisher backLeftAngleSetpoint = NetworkTables.PublisherFactory(this.table, "Back Left Angle Setpoint", 0);
    private final DoublePublisher backRightAngleSetpoint = NetworkTables.PublisherFactory(this.table, "Back Right Angle Setpoint", 0);

    private final DoublePublisher frontLeftObservedVelocity = NetworkTables.PublisherFactory(this.table, "Front Left Observed Velocity", 0);
    private final DoublePublisher frontRightObservedVelocity = NetworkTables.PublisherFactory(this.table, "Front Right Observed Velocity", 0);
    private final DoublePublisher backLeftObservedVelocity = NetworkTables.PublisherFactory(this.table, "Back Left Observed Velocity", 0);
    private final DoublePublisher backRightObservedVelocity = NetworkTables.PublisherFactory(this.table, "Back Right Observed Velocity", 0);

    private final DoublePublisher frontLeftObservedAngle = NetworkTables.PublisherFactory(this.table, "Front Left Observed Angle", 0);
    private final DoublePublisher frontRightObservedAngle = NetworkTables.PublisherFactory(this.table, "Front Right Observed Angle", 0);
    private final DoublePublisher backLeftObservedAngle = NetworkTables.PublisherFactory(this.table, "Back Left Observed Angle", 0);
    private final DoublePublisher backRightObservedAngle = NetworkTables.PublisherFactory(this.table, "Back Right Observed Angle", 0);

    private final DoublePublisher poseX = NetworkTables.PublisherFactory(this.table, "X position", 0);
    private final DoublePublisher poseY = NetworkTables.PublisherFactory(this.table, "y position", 0);
    private final DoublePublisher heading = NetworkTables.PublisherFactory(this.table, "Heading", 0);

    private final DoublePublisher yaw = NetworkTables.PublisherFactory(this.table, "yaw", 0);
    private final DoublePublisher pitch = NetworkTables.PublisherFactory(this.table, "pitch", 0);
    private final DoublePublisher roll = NetworkTables.PublisherFactory(this.table, "roll", 0);

    // private final DoublePublisher rawPidgionVal = NetworkTables.PublisherFactory(this.table, "Pidgion Val", 0);

    private final DoublePublisher characterizationVolts = NetworkTables.PublisherFactory(this.table,
            "characterization volts", 0);

    private final StructPublisher<Pose2d> posePublisher = NetworkTableInstance.getDefault()
            .getStructTopic("pose", Pose2d.struct).publish();

    private final DoublePublisher fieldOrientedPublisher = NetworkTables.PublisherFactory(this.table, "Field orientation", 0);

    private final SwerveModule frontLeftModule;
    private final SwerveModule frontRightModule;
    private final SwerveModule backLeftModule;
    private final SwerveModule backRightModule;

    private final Pigeon2 gyro = new Pigeon2(PIGEON_ID);

    private final SwerveDrivePoseEstimator poseEstimator;

    private Rotation2d fieldRelativeOffset = new Rotation2d();

    private boolean slowmode = false;
    public boolean fieldOriented = false;
    private boolean guardMode = false;

    public boolean aligning = false;
    private double turnRate = 0;

    public Drivetrain(SubsystemStore subsystems) {
        this.frontLeftModule = subsystems.track(new SwerveModule(
                FRONT_LEFT_DRIVE_MOTOR,
                FRONT_LEFT_STEER_MOTOR,
                FRONT_LEFT_STEER_ENCODER,
                FRONT_LEFT_DRIVE_MOTOR_INVERTED,
                FRONT_LEFT_STEER_MOTOR_INVERTED,
                FRONT_LEFT_STEER_ENCODER_OFFSET,
                this.frontLeftVelocitySetpoint,
                this.frontLeftAngleSetpoint,
                this.frontLeftObservedVelocity,
                this.frontLeftObservedAngle
        ));

        this.frontRightModule = subsystems.track(new SwerveModule(
                FRONT_RIGHT_DRIVE_MOTOR,
                FRONT_RIGHT_STEER_MOTOR,
                FRONT_RIGHT_STEER_ENCODER,
                FRONT_RIGHT_DRIVE_MOTOR_INVERTED,
                FRONT_RIGHT_STEER_MOTOR_INVERTED,
                FRONT_RIGHT_STEER_ENCODER_OFFSET,
                this.frontRightVelocitySetpoint,
                this.frontRightAngleSetpoint,
                this.frontRightObservedVelocity,
                this.frontRightObservedAngle
        ));

        this.backLeftModule = subsystems.track(new SwerveModule(
                BACK_LEFT_DRIVE_MOTOR,
                BACK_LEFT_STEER_MOTOR,
                BACK_LEFT_STEER_ENCODER,
                BACK_LEFT_DRIVE_MOTOR_INVERTED,
                BACK_LEFT_STEER_MOTOR_INVERTED,
                BACK_LEFT_STEER_ENCODER_OFFSET,
                this.backLeftVelocitySetpoint,
                this.backLeftAngleSetpoint,
                this.backLeftObservedVelocity,
                this.backLeftObservedAngle
        ));

        this.backRightModule = subsystems.track(new SwerveModule(
                BACK_RIGHT_DRIVE_MOTOR,
                BACK_RIGHT_STEER_MOTOR,
                BACK_RIGHT_STEER_ENCODER,
                BACK_RIGHT_DRIVE_MOTOR_INVERTED,
                BACK_RIGHT_STEER_MOTOR_INVERTED,
                BACK_RIGHT_STEER_ENCODER_OFFSET,
                this.backRightVelocitySetpoint,
                this.backRightAngleSetpoint,
                this.backRightObservedVelocity,
                this.backRightObservedAngle
        ));

        this.gyro.reset();

        final Pigeon2Configuration bird = new Pigeon2Configuration();
        bird.MountPose.MountPosePitch = 90;
        bird.MountPose.MountPoseRoll = 0;
        bird.MountPose.MountPoseYaw = -180;

        this.gyro.getConfigurator().apply(bird);

        // Define the standard deviations for the pose estimator, which determine how fast the pose
        // estimate converges to the vision measurement. This should depend on the vision measurement
        // noise
        // and how many or how frequently vision measurements are applied to the pose estimator.
        var stateStdDevs = VecBuilder.fill(0.1, 0.1, 0.1);
        var visionStdDevs = VecBuilder.fill(1, 1, 1);
        this.poseEstimator = new SwerveDrivePoseEstimator(
                SWERVE_DRIVE_KINEMATICS,
                this.getGyroYaw(),
                this.getSwerveModulePositions(),
                new Pose2d()
                stateStdDevs,
                visionStdDevs
        );
        
    }

    public void drive(ChassisSpeeds chassisSpeeds) {
        if (this.guardMode) {
            return;
        }

        var discretizedChassisSpeeds = ChassisSpeeds.discretize(
                chassisSpeeds.vxMetersPerSecond,
                chassisSpeeds.vyMetersPerSecond,
                chassisSpeeds.omegaRadiansPerSecond,
                0.02
        );

        var swerveModuleStates = SWERVE_DRIVE_KINEMATICS.toSwerveModuleStates(discretizedChassisSpeeds);

//        var swerveModuleStates = SWERVE_DRIVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, SWERVE_DRIVE_MAX_SPEED.in(MetersPerSecond));

        this.frontLeftModule.setDesiredState(swerveModuleStates[0]);
        this.frontRightModule.setDesiredState(swerveModuleStates[1]);
        this.backLeftModule.setDesiredState(swerveModuleStates[2]);
        this.backRightModule.setDesiredState(swerveModuleStates[3]);
    }

    public void fieldOrientedDrive(ChassisSpeeds chassisSpeeds) {
        if (this.aligning) {
            chassisSpeeds.omegaRadiansPerSecond = turnRate;
        }


        Rotation2d robotAngle = this.getGyroYaw().minus(this.fieldRelativeOffset);
        if (DriverStation.getAlliance().equals(Optional.of(DriverStation.Alliance.Red))) {
            robotAngle = robotAngle.rotateBy(Rotation2d.fromDegrees(180));
        }

        var robotRelativeChassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(chassisSpeeds, robotAngle);

        this.drive(robotRelativeChassisSpeeds);
    }

    public void setGuardMode(boolean enabled) {
        this.guardMode = enabled;
        
        if (enabled) {
            this.frontLeftModule.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
            this.frontRightModule.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
            this.backLeftModule.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
            this.backRightModule.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
        }
    }

    public void toggleGuardMode() {
        this.setGuardMode(!this.guardMode);
    }

    public boolean isGuardModeEnabled() {
        return this.guardMode;
    }

    public void setTurnRate(double rate) {
        this.turnRate = rate;
    }

    public void driveV(Voltage voltage) {
        this.characterizationVolts.set(voltage.in(Volts));

        frontLeftModule.drive(voltage);
        frontRightModule.drive(voltage);
        backLeftModule.drive(voltage);
        backRightModule.drive(voltage);
    }

    public Pose2d getEstimatedPosition() {
        return this.poseEstimator.getEstimatedPosition();
    }

    public void resetPose(Pose2d pose) {
        this.poseEstimator.resetPosition(
                this.getGyroYaw(),
                this.getSwerveModulePositions(),
                pose
        );

        this.fieldRelativeOffset = this.getGyroYaw().minus(pose.getRotation());
    }

    public void setYaw(Rotation2d yaw) {
        this.resetPose(new Pose2d(this.getEstimatedPosition().getTranslation(), yaw));
    }

    public ChassisSpeeds getChassisSpeeds() {
        return SWERVE_DRIVE_KINEMATICS.toChassisSpeeds(
                this.frontLeftModule.getState(),
                this.frontRightModule.getState(),
                this.backLeftModule.getState(),
                this.backRightModule.getState());
    }

    

    private SwerveModulePosition[] getSwerveModulePositions() {
        return new SwerveModulePosition[] {
                this.frontLeftModule.getPosition(),
                this.frontRightModule.getPosition(),
                this.backLeftModule.getPosition(),
                this.backRightModule.getPosition()
        };
    }

    public Rotation2d getGyroYaw() {
        return Rotation2d.fromDegrees(this.gyro.getYaw().getValue().in(Degrees));
    }
    public Rotation2d getGyroPitch(){
        return Rotation2d.fromDegrees(this.gyro.getPitch().getValue().in(Degrees));
    }
    public Rotation2d getGyroRoll(){
        return Rotation2d.fromDegrees(this.gyro.getRoll().getValue().in(Degrees));
    }

    public AngularVelocity getAverageDriveAngularVelocity() {
        return this.frontLeftModule.getAngularVelocity()
                .plus(this.frontRightModule.getAngularVelocity())
                .plus(this.backLeftModule.getAngularVelocity())
                .plus(this.backRightModule.getAngularVelocity())
                .div(4);
    }

    public void switchSlowmode() {
        slowmode = !slowmode;
    }

    public boolean isSlowModeEnabled() {
        return slowmode;
    }
    public void switchOrientation() {
        fieldOriented = !fieldOriented;
    }

    public boolean isFieldOriented() {
        return fieldOriented;
    }

    public void playMusic() {
        this.frontLeftModule.randomMusic();
        this.frontRightModule.randomMusic();
        this.backLeftModule.randomMusic();
        this.backRightModule.randomMusic();
    }
     public void addVisionMeasurement(Pose2d visionMeasurement, double timestampSeconds) {
        poseEstimator.addVisionMeasurement(visionMeasurement, timestampSeconds);
    }
    public void addVisionMeasurement(
            Pose2d visionMeasurement, double timestampSeconds, Matrix<N3, N1> stdDevs) {
        poseEstimator.addVisionMeasurement(visionMeasurement, timestampSeconds, stdDevs);
    }

    @Override
    public void periodic() {
        this.poseEstimator.update(
                this.getGyroYaw(),
                this.getSwerveModulePositions()
        );

        this.poseX.set(this.getEstimatedPosition().getX());
        this.poseY.set(this.getEstimatedPosition().getY());

        this.heading.set(this.getEstimatedPosition().getRotation().getDegrees());
        if (this.fieldOriented) {
            this.fieldOrientedPublisher.set(1);
        } else {
            this.fieldOrientedPublisher.set(0);
        }

        var x = new Pose2d(this.getEstimatedPosition().toMatrix());
        this.posePublisher.set(x);

        this.yaw.set(getGyroYaw().getDegrees());
        this.pitch.set(getGyroPitch().getDegrees());
        this.roll.set(getGyroRoll().getDegrees());
    }
}