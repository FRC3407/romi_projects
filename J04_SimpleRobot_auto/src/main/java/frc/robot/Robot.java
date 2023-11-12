package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.romi.RomiGyro;
import edu.wpi.first.wpilibj.romi.RomiMotor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends TimedRobot {

  final double kCountsPerRevolution = 1440.0;
  final double kWheelDiameterCentimeter = 7;

  RomiMotor leftMotor = new RomiMotor(0);
  RomiMotor rightMotor = new RomiMotor(1);
  XboxController xboxController = new XboxController(0);
  DifferentialDrive diffDrive = new DifferentialDrive(leftMotor, rightMotor);
  RomiGyro gyro = new RomiGyro();
  Encoder encoder = new Encoder(4, 5); // left encoder

  /** The different states that the autonomous routine may be in. */
  static final int DRIVING_OUT = 1;
  static final int ROTATING = 2;
  static final int DRIVING_BACK = 3;
  static final int STOPPED = 4;
 
  /** The current autonomous state. */
  int autoState = STOPPED;

  /**
   * This function is run once when the robot is first started up.
   */
  @Override
  public void robotInit() {
    encoder.setDistancePerPulse((Math.PI * kWheelDiameterCentimeter) / kCountsPerRevolution);
  }

  /**
   * This function is run 50 times a second while in Teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {
    double forwardSpeed = xboxController.getRawAxis(1);
    double rotationSpeed = xboxController.getRawAxis(0);
    arcadeDrive(forwardSpeed, rotationSpeed);
  }

  private void arcadeDrive(double forwardSpeed, double rotationSpeed) {
    diffDrive.arcadeDrive(rotationSpeed, forwardSpeed);
  }

  /**
   * This function is run once when Autonomous mode is enabled.
   */
  @Override
  public void autonomousInit() {
    autoState = DRIVING_OUT;
    gyro.reset();
    encoder.reset();
  }

  /**
   * This function is run 50 times a second while in Authonomous mode.
   * This implementation goes through a series of states.
   */
  @Override
  public void autonomousPeriodic() {

    if (autoState == DRIVING_OUT) {
      arcadeDrive(0.70, 0.0);
      if (encoder.getDistance() >= 50) {
        gyro.reset();
        autoState = ROTATING;
      }

    } else if (autoState == ROTATING) {
      arcadeDrive(0.0, 0.35);
      if (gyro.getAngle() > 180.0) {
        encoder.reset();
        autoState = DRIVING_BACK;
      }

    } else if (autoState == DRIVING_BACK) {
      arcadeDrive(0.70, 0.0);
      if (encoder.getDistance() >= 50) {
        gyro.reset();
        autoState = STOPPED;
      }

    } else if (autoState == STOPPED) {
      arcadeDrive(0.0, 0.0);
    }
  }

}
