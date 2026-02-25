package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import com.revrobotics.servohub.ServoChannel;
import com.revrobotics.servohub.ServoHub;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Timer;

// public class LinearServo extends Servo {

//     double m_speed;
//     double m_length;
//     double setPos;
//     double curPos;

//     /**
//      * Parameters for L16-R Actuonix Linear Actuators
//      *
//      * @param channel PWM channel used to control the servo
//      * @param length max length of the servo [mm]
//      * @param speed max speed of the servo [mm/second]
//      */
//     public LinearServo(int channel, int length, int speed) {
//         super(channel);
//         setBoundsMicroseconds(2000, 1800, 1500, 1200, 1000);
//         m_length = length;
//         m_speed = speed;
//     }


public class LinearServo extends ServoHub {
    double setPos;
    double curPos;
    double m_speed;
    double m_length;

    ServoChannel actuator;
    /**
     * Parameters for L16-R Actuonix Linear Actuators
     *
     * @param channel PWM channel used to control the servo
     * @param length max length of the servo [mm]
     * @param speed max speed of the servo [mm/second]
     */
    public LinearServo(int hubChannel, int servoChannel, int length, int speed) {
        super(hubChannel);
        this.actuator = this.getServoChannel(ServoChannel.ChannelId.fromInt(servoChannel));

        this.actuator.setEnabled(true);
        this.actuator.setPowered(true);
        this.setBankPulsePeriod(ServoHub.Bank.kBank0_2, 20000);
        m_length = length;
        m_speed = speed;
    }

    /**
     * Run this method in any periodic function to update the position
     * estimation of your servo
     *
     * @param setpoint the target position of the servo [mm]
     */
    public void setPosition(double setpoint) {
        setPos = MathUtil.clamp(setpoint, 0, m_length);
        this.actuator.setPulseWidth((int) (((((setPos / m_length)) * 1000) + 1000)));
    }
    double lastTime = 0;

    /**
     * Run this method in any periodic function to update the position
     * estimation of your servo
     */
    public void updateCurPos() {
        double currentTime = Timer.getFPGATimestamp();
        double dt = currentTime - lastTime;
        lastTime = currentTime;
        if (curPos > setPos + m_speed * dt) {
            curPos -= m_speed * dt;
        } else if (curPos < setPos - m_speed * dt) {
            curPos += m_speed * dt;
        } else {
            curPos = setPos;
        }
    }

    /**
     * Current position of the servo
     *
     * @return Servo Position [pulse length]
     */
    public double getPosition() {
        return (int) (((((setPos / m_length)) * 1000) + 1000));
    }

    public double getSetPos() {
        return setPos;
    }

    /**
     * Checks if the servo is at its target position, must be calling {@link #updateCurPos()
     * updateCurPos()} periodically
     *
     * @return true when servo is at its target
     */
    public boolean isFinished() {
        return curPos == setPos;
    }
}

// import edu.wpi.first.wpilibj.Servo;
// public class LinearServo extends Servo {
//     double setPos;
//     /**
//      * Parameters for L16-R Actuonix Linear Actuators
//      *
//      * @param channel PWM channel used to control the servo
//      */
//     public LinearServo(int channel) {
//         super(channel);
//     }
//     @Override
//     public void setPosition(double setpoint) {
//         this.setPos = setpoint;
//     }
//     private double voltageCalc() {
//         return (this.setPos - getPosition()) * 0.5;
//     }
//     public void periodic() {
//         this.set(this.setPos);
//     }
//     public double getSetPos() {
//         return this.setPos;
//     }

// }
