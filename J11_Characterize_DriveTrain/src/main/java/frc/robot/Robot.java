package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.romi.RomiMotor;
import frc.robot.util.CharacterizingSpeedController;

public class Robot extends TimedRobot {

  final double MIN_SPEED = 0.1;
  final int MAX_DATA_POINTS = 50 * 30;
  final double MAX_VOLTAGE = 12.0;

  final double ENCODER_COUNTS_PER_REV = 1440.0;
  final double WHEEL_DIAMETER_CENTIMETERS = 7;

  Encoder leftEncoder = new Encoder(4, 5);
  Encoder rightEncoder = new Encoder(6, 7);

  CharacterizingSpeedController leftMotor = new CharacterizingSpeedController(new RomiMotor(0),
      leftEncoder::getDistance);
  CharacterizingSpeedController rightMotor = new CharacterizingSpeedController(new RomiMotor(1),
      rightEncoder::getDistance);
  DifferentialDrive diffDrive = new DifferentialDrive(leftMotor, rightMotor);

  XboxController xboxController = new XboxController(0);

  @Override
  public void robotInit() {
    leftEncoder.setDistancePerPulse((Math.PI * WHEEL_DIAMETER_CENTIMETERS) / ENCODER_COUNTS_PER_REV);
    rightEncoder.setDistancePerPulse((Math.PI * WHEEL_DIAMETER_CENTIMETERS) / ENCODER_COUNTS_PER_REV);
    rightMotor.setInverted(true);
  }

  @Override
  public void teleopInit() {
    leftMotor.startRecording();
    rightMotor.startRecording();
  }

  @Override
  public void teleopPeriodic() {
    double forwardSpeed = xboxController.getRawAxis(0) * -1;
    double rotationSpeed = xboxController.getRawAxis(1);
    diffDrive.arcadeDrive(rotationSpeed, forwardSpeed);
  }

  @Override
  public void disabledInit() {
    leftMotor.stopRecording();
    rightMotor.stopRecording();
    System.out.println(leftMotor);
    System.out.println(rightMotor);
  }

  @Override
  public void autonomousInit() {
    leftMotor.stopRecording();
    rightMotor.stopRecording();
  }
}
