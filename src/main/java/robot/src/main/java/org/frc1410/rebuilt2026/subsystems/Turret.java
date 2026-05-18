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
    private final SparkMax hood;
    private final SparkMax swivel;
    private final SparkMax shoot;
    private double TargetSwivelPos;
    private double TargetHoodPos;
    private double shooterSpeed;
    public enum ShooterStates{
        FULLPOWER, LOWPOWER, NEUTRAL, REVERSE
    }
    private ShooterStates ShooterState = ShooterStates.NEUTRAL; 

    
    public Turret(){
        this.swivel = new SparkMax(TURRET_SWIVEL_SPARK, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig swivelHoodConfig = new SparkMaxConfig();
        swivelHoodConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        swivelHoodConfig.smartCurrentLimit(30);
        this.hood = new SparkMax(TURRET_HOOD_SPARK, SparkLowLevel.MotorType.kBrushless);
        this.shoot = new SparkMax(TURRET_SPARK, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig shootConfig = new SparkMaxConfig();
        shootConfig.idleMode(SparkBaseConfig.IdleMode.kCoast);
        shootConfig.smartCurrentLimit(30);
        this.hood.configure(swivelHoodConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);
        this.hood.configure(shootConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);
    }
    public void setShooterSpeed(ShooterStates s){
        ShooterState = s;
    }
    

    @Override
    public void periodic(){
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
    }
    @Override
    public void telem(){
        
    }
}
