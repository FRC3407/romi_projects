// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.romi.RomiGyro;
import edu.wpi.first.wpilibj.romi.RomiMotor;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class Chassis extends SubsystemBase {

  // The Romi has the left and right motors set to
  // PWM channels 0 and 1 respectively
  private final RomiMotor m_leftMotor = new RomiMotor(0);
  private final RomiMotor m_rightMotor = new RomiMotor(1);

  // The Romi has onboard encoders that are hardcoded
  // to use DIO pins 4/5 and 6/7 for the left and right
  private final Encoder m_leftEncoder = new Encoder(4, 5);
  private final Encoder m_rightEncoder = new Encoder(6, 7);

  // Set up the differential drive controller
  private final DifferentialDrive m_diffDrive = new DifferentialDrive(m_leftMotor, m_rightMotor);

  // Set up the RomiGyro
  private final RomiGyro m_gyro = new RomiGyro();

  // Odometry class for tracking robot pose
  private final DifferentialDriveOdometry m_odometry;

  // Also show a field diagram
  private final Field2d m_field2d = new Field2d();

  /** Creates a new RomiDrivetrain. */
  public Chassis() {
    m_leftEncoder.setDistancePerPulse((Math.PI * ROMI_WHEEL_DIAMETER_METER) / ROMI_COUNTS_PER_REVOLUTION);
    m_rightEncoder.setDistancePerPulse((Math.PI * ROMI_WHEEL_DIAMETER_METER) / ROMI_COUNTS_PER_REVOLUTION);
    resetEncoders();
    m_gyro.reset();
    resetGyro();

    // Invert right side since motor is flipped
    m_rightMotor.setInverted(true);

    m_odometry = new DifferentialDriveOdometry(m_gyro.getRotation2d(), 0.0, 0.0);
  }

  /** Drive the robot based on values in the range -1.0 to 1.0. */
  public void arcadeDrive(double speed, double rotation) {
    m_diffDrive.arcadeDrive(speed, -1 * rotation);
  }

  public void stop() {
    m_diffDrive.arcadeDrive(0, 0);
  }

  /** Reset left and right encoder distances to zero. */
  public void resetEncoders() {
    m_leftEncoder.reset();
    m_rightEncoder.reset();
  }

  public void resetGyro() {
    m_gyro.reset();
  }

  /** @return left encoder distance in meters. */
  public double getLeftEncoder() {
    return m_leftEncoder.getDistance();
  }

  /** @return right encoder distance in meters. */
  public double getRightEncoder() {
    return m_rightEncoder.getDistance();
  }

  /** @return right robot distance traveled in meters. */
  public double getAverageEncoderDistance() {
    return (getRightEncoder() + getLeftEncoder()) / 2.0;
  }

  /** @return current yaw angle in degrees. */
  public double getAngle() {
    return m_gyro.getAngle();
  }

  /** @return current pitch angle in degrees. */
  public double getPitch() {
    return m_gyro.getAngleY();
  }

  /** @return current pitch angle in degrees. */
  public double getRoll() {
    return m_gyro.getAngleX();
  }

  @Override
  public void periodic() {
    // Update the odometry
    Rotation2d gyroAngleRadians = Rotation2d.fromDegrees(-getAngle());
    double leftDistanceMeters = getLeftEncoder();
    double rightDistanceMeters = getRightEncoder();
    m_odometry.update(gyroAngleRadians, leftDistanceMeters, rightDistanceMeters);

    // Also update the Field2D object (so that we can visualize this in sim)
    Pose2d currentPose = getPose();
    m_field2d.setRobotPose(currentPose);

    if (DEBUG) {
      SmartDashboard.putNumber("angle", getAngle());
      SmartDashboard.putNumber("leftDistanceMeters", leftDistanceMeters);
      SmartDashboard.putNumber("rightDistanceMeters", rightDistanceMeters);
      SmartDashboard.putString("currentPose", currentPose.toString());
    }
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  /** Drive the robot based on voltage values. */
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    m_leftMotor.setVoltage(leftVolts);
    m_rightMotor.setVoltage(rightVolts);
    m_diffDrive.feed();
  }

  /** @return The current wheel speeds in meters per second. */
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    double leftMetersPerSecond = m_leftEncoder.getRate();
    double rightMetersPerSecond = m_rightEncoder.getRate();
    return new DifferentialDriveWheelSpeeds(leftMetersPerSecond, rightMetersPerSecond);
  }

  /**
   * Returns the currently estimated odometry pose of the robot.
   *
   * @return robot pose in radians and meters.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  /**
   * Resets the odometry to the specified pose
   *
   * @param pose The pose to which to set the odometry, in radians and meters.
   */
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    Rotation2d gyroAngleRadians = Rotation2d.fromDegrees(-getAngle());
    m_odometry.resetPosition(gyroAngleRadians, 0.0, 0.0, pose);
  }
}

