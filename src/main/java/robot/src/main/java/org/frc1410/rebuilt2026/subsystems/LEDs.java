package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.LED_BRIGHTNESS;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.LED_ID;

import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.configs.LEDConfigs;
import com.ctre.phoenix6.configs.CANdleConfiguration;
import com.ctre.phoenix6.configs.CANdleFeaturesConfigs;
import com.ctre.phoenix6.signals.Enable5VRailValue;
import com.ctre.phoenix6.signals.LossOfSignalBehaviorValue;
import com.ctre.phoenix6.signals.StatusLedWhenActiveValue;
import com.ctre.phoenix6.signals.StripTypeValue;
import com.ctre.phoenix6.signals.VBatOutputModeValue;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.signals.RGBWColor;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Subsystem;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;

public class LEDs implements Subsystem {

    private final CANdle canDLE;

    // private final int LEDnum = 400; // This is nessisary for animations

    private int r = 0;
    private int g = 0;
    private int b = 0;

    public enum Color {
        RED,
        ORANGE,
        YELLOW,
        GREEN,
        BLUE,
        PURPLE,
        NONE,
        ANIMATION
    }

    private Color currColor = Color.NONE;

    public LEDs() {
        this.canDLE = new CANdle(LED_ID, "CTRE");

        LEDConfigs lEDConfig = new LEDConfigs();
        lEDConfig.BrightnessScalar = LED_BRIGHTNESS;
        lEDConfig.LossOfSignalBehavior = LossOfSignalBehaviorValue.DisableLEDs;
        lEDConfig.StripType = StripTypeValue.BRG;

        CANdleFeaturesConfigs CANdleFeaturesConfig = new CANdleFeaturesConfigs();
        CANdleFeaturesConfig.Enable5VRail = Enable5VRailValue.Enabled;
        CANdleFeaturesConfig.StatusLedWhenActive = StatusLedWhenActiveValue.Enabled;
        // CANdleFeaturesConfig.VBatOutputMode = VBatOutputModeValue.Off; // IDK what this is, change how you like

        CANdleConfiguration config = new CANdleConfiguration()
                .withLED(lEDConfig)
                .withCANdleFeatures(CANdleFeaturesConfig);

        canDLE.getConfigurator().apply(config);
    }

    public void setRGB(Color color) {
        switch (color) {
            case RED:
                this.r = 255;
                this.g = 0;
                this.b = 0;

            case ORANGE:
                this.r = 255;
                this.g = 172;
                this.b = 0;

            case YELLOW:
                this.r = 255;
                this.g = 255;
                this.b = 0;

            case GREEN:
                this.r = 0;
                this.g = 255;
                this.b = 0;

            case BLUE:
                this.r = 0;
                this.g = 0;
                this.b = 255;

            case PURPLE:
                this.r = 255;
                this.g = 0;
                this.b = 255;

            case NONE:
                this.r = 0;
                this.g = 0;
                this.b = 0;
            default:
                this.r = 0;
                this.g = 0;
                this.b = 0;
        }
    }

    public void setColor(Color color) {
        this.setRGB(color);
        this.canDLE.setControl(
            new SolidColor(0, 10)
                .withColor(new RGBWColor(this.r, this.g, this.b))
        );
        this.currColor = color;
    }
}
