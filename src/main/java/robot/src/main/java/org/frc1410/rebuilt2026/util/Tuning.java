package robot.src.main.java.org.frc1410.rebuilt2026.util;

import com.pathplanner.lib.config.PIDConstants;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;

public final class Tuning {

    // Drivetrain
    public static final double SWERVE_DRIVE_P = 0.401;
    public static final double SWERVE_DRIVE_I = 0;
    public static final double SWERVE_DRIVE_D = 0.000;

    public static final double SWERVE_STEER_P = 4.7; //4.7
    public static final double SWERVE_STEER_I = 0.0;
    public static final double SWERVE_STEER_D = 0.0;

//    public static final double SWERVE_STEER_P = 0.0;
//    public static final double SWERVE_STEER_I = 0.0;
//    public static final double SWERVE_STEER_D = 0.0;

    public static final double DRIVE_KS = 0.36498;
    public static final double DRIVE_KV = 0.11769;

    public static double DRIVE_MULTIPLIER = 1.0;

    //Vision
    public static final double VISION_TURN_kP = 0.07;
    public static final double VISION_TURN_kI = 0;
    public static final double VISION_TURN_kD = 0;
    
    public static final Transform3d EoC1_OFFSET = new Transform3d(new Translation3d(
        //How forward or backward the came is from robot center
        0.5, //0.5
        //How left or right cam is from robot center
        0, //0
        //Measures how far up or down from the robot center the cam is
        0.5 //0.5
        ),
    new Rotation3d(
        0,
        0,
        0
        ));
    public static final Transform3d EoC2_OFFSET = new Transform3d(new Translation3d(
        //How forward or backward the came is from robot center
        0.5, //0.5
        //How left or right cam is from robot center
        0, //0
        //Measures how far up or down from the robot center the cam is
        0.5 //0.5
        ),
    new Rotation3d(
        0,
        0,
        0
        ));
    // Path following
    public static final PIDConstants PATH_AUTO_TRANSLATION_CONSTRAINTS = new PIDConstants(3.2, 0, 0);
    public static final PIDConstants PATH_AUTO_ROTATION_CONSTRAINTS = new PIDConstants(1.3, 0, 0.1);

    public static final PIDConstants PATH_FOLLOWING_TRANSLATION_CONSTRAINTS = new PIDConstants(4, 0, 0);
    public static final PIDConstants PATH_FOLLOWING_ROTATION_CONSTRAINTS = new PIDConstants(0.6, 0, 0.05);
}
