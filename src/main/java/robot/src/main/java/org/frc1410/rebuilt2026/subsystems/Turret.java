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
    
    public Turret(){
        // this.intakeMotor = new SparkMax(INTAKE_SPARK, SparkLowLevel.MotorType.kBrushless);
        // SparkMaxConfig intakeMotorConfig = new SparkMaxConfig();
        // intakeMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        // intakeMotorConfig.smartCurrentLimit(30);
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
    
    @Override
    public void periodic(){

    }
    @Override
    public void telem(){
        
    }
}
