// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.*;

import java.util.Optional;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.FollowPathRamsete;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.romi.RomiGyro;
import edu.wpi.first.wpilibj.romi.RomiMotor;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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

  // Kinimatics class translates chassis speeds to wheel speeds
  private final DifferentialDriveKinematics m_kinematics;

  // Configuration for path replanning
  private final ReplanningConfig m_replanningConfig;

  // Also show a field diagram
  private final Field2d m_field2d = new Field2d();

  /** Creates a new RomiDrivetrain. */
  public Chassis() {
    m_leftEncoder.setDistancePerPulse((Math.PI * ROMI_WHEEL_DIAMETER_METER) / ROMI_COUNTS_PER_REVOLUTION);
    m_rightEncoder.setDistancePerPulse((Math.PI * ROMI_WHEEL_DIAMETER_METER) / ROMI_COUNTS_PER_REVOLUTION);
    resetEncoders();
    resetGyro();

    // Invert right side since motor is flipped
    m_rightMotor.setInverted(true);

    m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(m_gyro.getAngle()), 0.0, 0.0);
    m_kinematics = new DifferentialDriveKinematics(RAMSETE_TRACK_WIDTH_METERS);
    m_replanningConfig = new ReplanningConfig();

    AutoBuilder.configureRamsete(
        this::getPose,
        this::resetPose,
        this::getCurrentSpeeds,
        this::drive,
        m_replanningConfig,
        this::isRedAlliance,
        this);
  }

  public void drive(ChassisSpeeds chassisSpeeds) {
    DifferentialDriveWheelSpeeds wheelSpeeds = m_kinematics.toWheelSpeeds(chassisSpeeds);
    tankDriveVolts(RAMSETE_MAX_VOLTAGE * wheelSpeeds.leftMetersPerSecond / ROBOT_MAX_SPEED,
        RAMSETE_MAX_VOLTAGE * wheelSpeeds.rightMetersPerSecond / ROBOT_MAX_SPEED);
  }

  public void stop() {
    tankDriveVolts(0, 0);
  }

  private ChassisSpeeds getCurrentSpeeds() {
    return m_kinematics.toChassisSpeeds(getWheelSpeeds());
  }

  private boolean isRedAlliance() {
    Optional<Alliance> alliance = DriverStation.getAlliance();
    return (alliance.isPresent())
        ? (alliance.get() == DriverStation.Alliance.Red)
        : false;
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
  public void resetPose(Pose2d pose) {
    resetEncoders();
    Rotation2d gyroAngleRadians = Rotation2d.fromDegrees(-getAngle());
    m_odometry.resetPosition(gyroAngleRadians, 0.0, 0.0, pose);
  }
}
