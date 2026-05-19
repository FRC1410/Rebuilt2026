package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.TURRET_HOOD_SPARK;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.TURRET_SPARK;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.TURRET_SWIVEL_SPARK;
public class Turret implements TickedSubsystem{
    
    //SparkMaxes control the motors that operate the turret, declare them here
    private final SparkMax hood;
    private final SparkMax swivel;
    private final SparkMax shoot;

    //Declare stuff that we will be altering the sparks with 
    private double TargetSwivelPos;
    private double TargetHoodPos;
    private double shooterSpeed;
    
    //Statemachine to control shooter states
    public enum ShooterStates{
        FULLPOWER, LOWPOWER, NEUTRAL, REVERSE
    }
    //Initialize the statemachine ina neutral state
    private ShooterStates ShooterState = ShooterStates.NEUTRAL; 

    //Constructor
    public Turret(){
        
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
    }

    /** Pretty self explanatory, sets shooter speed based on state
    * @param state the state you want to feed in, refer to {@link ShooterStates}.
    */
    public void setShooterSpeed(ShooterStates state){
        ShooterState = state;
    }
    

    @Override
    public void periodic(){
        
        //Based off of what the current state is, sets the power of the spark
        switch(ShooterState){
            case FULLPOWER:
                shooterSpeed = .9;
                break;
            case LOWPOWER:
                shooterSpeed = .4;
                break;
            case NEUTRAL:
                shooterSpeed = 0;
                break;
            case REVERSE:
                shooterSpeed = -0.7;
                break;
        }
        shoot.set(shooterSpeed);
        //To Be Implemented
        swivel.set(TargetSwivelPos);
        //To Be Implemented
        hood.set(TargetHoodPos);
    }
    @Override
    public void telem(){
        //copy everything from periodic here once it fully works
    }
}
