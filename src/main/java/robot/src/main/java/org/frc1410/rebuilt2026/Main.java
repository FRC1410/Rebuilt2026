package robot.src.main.java.org.frc1410.rebuilt2026;

import edu.wpi.first.wpilibj.RobotBase;

public interface Main {
  static void main(String[] args) {
	  RobotBase.startRobot(Robot::new);
  }
}
