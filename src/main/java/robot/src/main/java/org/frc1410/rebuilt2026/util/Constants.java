package robot.src.main.java.org.frc1410.rebuilt2026.util;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.DegreesPerSecondPerSecond;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.MetersPerSecondPerSecond;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularAcceleration;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearAcceleration;
import edu.wpi.first.units.measure.LinearVelocity;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.PATH_AUTO_ROTATION_CONSTRAINTS;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.PATH_AUTO_TRANSLATION_CONSTRAINTS;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.PATH_FOLLOWING_ROTATION_CONSTRAINTS;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.PATH_FOLLOWING_TRANSLATION_CONSTRAINTS;

public final class Constants {

    //LED's
    public static final double LED_BRIGHTNESS = 1.0;

    //Drivetrain
    public static double driveAccelerationProportionalLimitationMultiplier = 0;

    public static RobotConfig ROBOT_CONFIG;

    static {
        try {
            ROBOT_CONFIG = RobotConfig.fromGUISettings();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

    }

    // Robot constants
    public static final double DRIVE_GEAR_RATIO = (50.0 / 16.0) * (17.0 / 27.0) * (45.0 / 15.0);
//    public static final double DRIVE_L_TWO_GEAR_RATIO = (50.0 / 14.0) * (17.0 / 27.0) * (45.0 / 15.0);
    public static final Distance WHEEL_RADIUS = Inches.of(2);
    public static final Distance WHEEL_CIRCUMFERENCE = WHEEL_RADIUS.times(2 * Math.PI);
    public static final Distance TRACKWIDTH_METERS = Meters.of(0.6032627);

    // Drive constants
    public static final Angle FRONT_LEFT_STEER_ENCODER_OFFSET = Degrees.of(88.505859375); //88.769531
    public static final Angle FRONT_RIGHT_STEER_ENCODER_OFFSET = Degrees.of(54.052734375); //53.349609
    public static final Angle BACK_LEFT_STEER_ENCODER_OFFSET = Degrees.of(50.712890625); //155.039062-180
    public static final Angle BACK_RIGHT_STEER_ENCODER_OFFSET = Degrees.of(37.44140625); //37.265625
    // public static final Angle FRONT_LEFT_STEER_ENCODER_OFFSET = Degrees.of(88.76953125); //88.769531
    // public static final Angle FRONT_RIGHT_STEER_ENCODER_OFFSET = Degrees.of(56.953125); //53.349609
    // public static final Angle BACK_LEFT_STEER_ENCODER_OFFSET = Degrees.of(49.5703125); //155.039062-180
    // public static final Angle BACK_RIGHT_STEER_ENCODER_OFFSET = Degrees.of(37.265625); //37.265625

    public static final boolean FRONT_LEFT_DRIVE_MOTOR_INVERTED = false;
    public static final boolean FRONT_RIGHT_DRIVE_MOTOR_INVERTED = true;
    public static final boolean BACK_LEFT_DRIVE_MOTOR_INVERTED = true;
    public static final boolean BACK_RIGHT_DRIVE_MOTOR_INVERTED = true;

    public static final boolean FRONT_LEFT_STEER_MOTOR_INVERTED = false;
    public static final boolean FRONT_RIGHT_STEER_MOTOR_INVERTED = false;
    public static final boolean BACK_LEFT_STEER_MOTOR_INVERTED = false;
    public static final boolean BACK_RIGHT_STEER_MOTOR_INVERTED = false;

    public static final Translation2d FRONT_LEFT_SWERVE_MODULE_LOCATION = new Translation2d(0.3048, 0.3048);
    public static final Translation2d FRONT_RIGHT_SWERVE_MODULE_LOCATION = new Translation2d(0.3048, -0.3048);
    public static final Translation2d BACK_LEFT_SWERVE_MODULE_LOCATION = new Translation2d(-0.3048, 0.3048);
    public static final Translation2d BACK_RIGHT_SWERVE_MODULE_LOCATION = new Translation2d(-0.3048, -0.3048);

    public static final SwerveDriveKinematics SWERVE_DRIVE_KINEMATICS = new SwerveDriveKinematics(
        FRONT_LEFT_SWERVE_MODULE_LOCATION,
        FRONT_RIGHT_SWERVE_MODULE_LOCATION,
        BACK_LEFT_SWERVE_MODULE_LOCATION,
        BACK_RIGHT_SWERVE_MODULE_LOCATION
    );

    public static final double SLOW_MULTIPLIER = 1;
    public static final LinearVelocity SWERVE_DRIVE_MAX_SPEED = MetersPerSecond.of(5.5 * SLOW_MULTIPLIER);
    public static final AngularVelocity SWERVE_DRIVE_MAX_ANGULAR_VELOCITY = DegreesPerSecond.of(500 * SLOW_MULTIPLIER);
    public static final LinearAcceleration SWERVE_DRIVE_MAX_ACCELERATION = MetersPerSecondPerSecond.of(6 * SLOW_MULTIPLIER);
    public static final AngularAcceleration SWERVE_DRIVE_MAX_ANGULAR_ACCELERATION = DegreesPerSecondPerSecond.of(1062 * SLOW_MULTIPLIER);

    public static final double DRIVE_MOTOR_CURRENT_LIMIT = 40;
    public static final int STEER_MOTOR_CURRENT_LIMIT = 30;

    public static PPHolonomicDriveController HOLONOMIC_AUTO_CONFIG = new PPHolonomicDriveController(
        PATH_AUTO_TRANSLATION_CONSTRAINTS,
        PATH_AUTO_ROTATION_CONSTRAINTS
    );

    public static PathConstraints PATH_FINDING_CONSTRAINTS = new PathConstraints(
        SWERVE_DRIVE_MAX_SPEED,
        SWERVE_DRIVE_MAX_ACCELERATION,
        SWERVE_DRIVE_MAX_ANGULAR_VELOCITY,
        SWERVE_DRIVE_MAX_ANGULAR_ACCELERATION
    );
    public static final AprilTagFieldLayout APRIL_TAG_FIELD_LAYOUT = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);

    public static PPHolonomicDriveController PATH_FOLLOWING_CONTROLLER = new PPHolonomicDriveController(
        PATH_FOLLOWING_TRANSLATION_CONSTRAINTS,
        PATH_FOLLOWING_ROTATION_CONSTRAINTS
    );

    public static final Matrix<N3, N1> kSingleTagStdDevs = VecBuilder.fill(4, 4, 8);
    public static final Matrix<N3, N1> kMultiTagStdDevs = VecBuilder.fill(0.5, 0.5, 1);

    public static final int PIGEON_MOUNT_PITCH = 0;
    public static final int PIGEON_MOUNT_ROLL = 90;
    public static final int PIGEON_MOUNT_YAW = 0;

    public static final double INTAKE_FRAME_RAISED_POSITION = 10.0;
    public static final double INTAKE_FRAME_LOWERED_POSITION = 0.0;

    public static final double HOOD_LOW_LEFT_SETPOINT = 0.0;
    public static final double HOOD_LOW_RIGHT_SETPOINT = 0.5;
    public static final double HOOD_HIGH_LEFT_SETPOINT = 1.0;
}
