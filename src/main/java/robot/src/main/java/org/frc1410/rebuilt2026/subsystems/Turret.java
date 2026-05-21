package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.controller.PIDController;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.TURRET_HOOD_SPARK;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.TURRET_SPARK;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.TURRET_SWIVEL_SPARK;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.HOOD_P;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.HOOD_I;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.HOOD_D;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.SWIVEL_P;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.SWIVEL_I;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.SWIVEL_D;

public class Turret implements TickedSubsystem {

    //SparkMaxes control the motors that operate the turret, declare them here
    private final SparkMax hood;
    private final SparkMax swivel;
    private final SparkMax shoot;

    //Declare stuff that we will be altering the sparks with 
    private double targetSwivelPos;
    private double targetHoodPos;
    private double shooterSpeed;
    private double distance;

    private PIDController hoodController;
    private PIDController swivelController;

    //State machine to control shooter states
    public enum ShooterStates {
        FULLPOWER, LOWPOWER, NEUTRAL, REVERSE
    }
    //Initialize the statemachine ina neutral state
    private ShooterStates shooterState = ShooterStates.NEUTRAL;

    //Constructor
    public Turret() {

        //Declare PID controllers
        hoodController = new PIDController(HOOD_P, HOOD_I, HOOD_D);
        swivelController = new PIDController(SWIVEL_P, SWIVEL_I, SWIVEL_D);

        //Declare our swivel and hood sparks(which use PID) and create the config for them
        this.swivel = new SparkMax(TURRET_SWIVEL_SPARK, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig swivelHoodConfig = new SparkMaxConfig();
        swivelHoodConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        swivelHoodConfig.smartCurrentLimit(30);
        this.hood = new SparkMax(TURRET_HOOD_SPARK, SparkLowLevel.MotorType.kBrushless);

        //Declare the shooter spark and create the config for the spark
        this.shoot = new SparkMax(TURRET_SPARK, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig shootConfig = new SparkMaxConfig();
        shootConfig.idleMode(SparkBaseConfig.IdleMode.kCoast);
        shootConfig.smartCurrentLimit(30);

        //Configure our sparks based on the configs we just made
        this.hood.configure(swivelHoodConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);
        this.swivel.configure(swivelHoodConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);
        this.shoot.configure(shootConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);
        distance = 0.0;
    }

    /**
     * Sets shooter speed based on state
     *
     * @param state the state you want to feed in, refer to
     * {@link ShooterStates}.
     */
    public void setShooterSpeed(ShooterStates state) {
        shooterState = state;

        //Based off of what the current state is, sets the power of the spark
        switch (shooterState) {
            case FULLPOWER ->
                shooterSpeed = .9;
            case LOWPOWER ->
                shooterSpeed = .4;
            case NEUTRAL ->
                shooterSpeed = 0;
            case REVERSE ->
                shooterSpeed = -0.7;
        }
    }
    /** Sets the {@link Turret hood} to a desired angle in degrees
    *  @param deg Feed in your degrees value here, assume 0 is lowest position for the hood and you're incrementing ccw up to 90 degrees
    */
    public void setHoodAngle(double deg){
        targetHoodPos = deg;
    }

    /** Sets the {@link Turret swivel} to a desired angle in degrees
    *  @param deg Feed in your degrees value here, assume 0 is the opening facing the intake, up to 359 ccw or cw
    */
    public void setSwivelAngle(double deg)
    {
        targetSwivelPos = deg;
    }
    /**Sets the distance of the robot from the hub(in meters)
     * @param dist Distance from the camera to the target
     */
    public void setDistance(double dist){
        distance = dist;
    }
    @Override
    public void periodic() {

        //Calculates the power needed to make a given motor go to a desired angle in degrees
        double swivelOutput = swivelController.calculate(swivel.getEncoder().getPosition() * 360, targetSwivelPos);
        double hoodOutput = hoodController.calculate(hood.getEncoder().getPosition() * 360, targetHoodPos);

        shoot.set(shooterSpeed);
        swivel.set(swivelOutput);
        hood.set(hoodOutput);
    }

    @Override
    public void telem() {
        //Put NetworkTables here if there are any
    }

    /** @return the actual current angle of the swivel */
    public double getSwivel() {
        return swivel.getEncoder().getPosition() * 360;
    }

    /** @return actual current hood angle */
    public double getHood() {
        return hood.getEncoder().getPosition() * 360;
    }
    
    /** @return current shooter speed*/
    public double getShooter() {
        return shooterSpeed;
    }

    /** @return what the hood should be at degrees*/
    public double getDesiredHood(){
        return targetHoodPos;
    }

    /** @return what the swivel should be at in degrees*/
    public double getDesiredSwivel(){
        return targetSwivelPos;
    }
    
    /** @return Distance from camera to target in meters*/
    public double returnDistance(){
        return distance;
    }
    
}
