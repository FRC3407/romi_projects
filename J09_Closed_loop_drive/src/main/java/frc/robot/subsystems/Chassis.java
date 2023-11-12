// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.*;

import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.romi.RomiMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.PidMotorController;

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
  private final DifferentialDrive m_diffDrive;

  private DoubleLogEntry leftMotorLog;
  private DoubleLogEntry rightMotorLog;

  /** Creates a new RomiDrivetrain. */
  public Chassis() {
    m_leftEncoder.setDistancePerPulse((Math.PI * ROMI_WHEEL_DIAMETER_METER) / ROMI_COUNTS_PER_REVOLUTION);
    m_rightEncoder.setDistancePerPulse((Math.PI * ROMI_WHEEL_DIAMETER_METER) / ROMI_COUNTS_PER_REVOLUTION);
    resetEncoders();

    // Invert right side since motor is flipped
    m_rightMotor.setInverted(true);

    // Create the DifferentialDrive based on PID feedback
    m_diffDrive = new DifferentialDrive(
        new PidMotorController(m_leftMotor, m_leftEncoder::getRate),
        new PidMotorController(m_rightMotor, m_rightEncoder::getRate));

    leftMotorLog = new DoubleLogEntry(DataLogManager.getLog(), "/chassis/leftMotors");
    rightMotorLog = new DoubleLogEntry(DataLogManager.getLog(), "/chassis/rightMotors");
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

  @Override
  public void periodic() {
    if (LOGGING) {
      leftMotorLog.append(m_leftEncoder.getRate());
      rightMotorLog.append(m_rightEncoder.getRate());
    }
  }

  @Override
  public void simulationPeriodic() {
    if (LOGGING) {
      leftMotorLog.append(m_leftEncoder.getRate());
      rightMotorLog.append(m_rightEncoder.getRate());
    }
  }
}
