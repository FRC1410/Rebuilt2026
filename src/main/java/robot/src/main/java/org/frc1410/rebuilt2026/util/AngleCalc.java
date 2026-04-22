package robot.src.main.java.org.frc1410.rebuilt2026.util;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Radians;

public class AngleCalc {
    double GRAVITY = 9.81;
    double VELOCITY = 17;

    double TARGET_HEIGHT = 1.8288;


    private double[] getVels(double angle) {
        return new double[]{
            Math.sin(Radians.convertFrom(angle, Degrees)) 
            * VELOCITY, 
            Math.cos(Radians.convertFrom(angle, Degrees)) 
            * VELOCITY
        };
    }

    private double height(double time, double vUp) {
        return ((vUp * time) - ((GRAVITY / 2) * time * time));
    }

    private double forward(double time, double vForward) {
        return (vForward * time);
    }

    public double calcAngle(double distance) {
        double vUp;
        double vForward;

        for (double angle = 90; angle > 0; angle-= 0.5){
            double[] vels = getVels(angle);
            vUp = vels[0];
            vForward = vels[1];
            double time = 0.1;
            while (height(time, vUp) > 0) {
                if (
                    (
                        height(time, vUp) > TARGET_HEIGHT 
                        && 
                        height(time, vUp) < TARGET_HEIGHT + 0.5
                    )
                    && 
                    (
                        forward(time, vForward) > distance 
                        && 
                        forward(time, vForward) < distance + 0.5
                    )
                ) {
                    return angle;
                }
                time += 0.1;
            }
        }
        return -1;

    }
}
