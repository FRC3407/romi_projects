// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.romi.RomiMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@Singleton
public class Chassis extends SubsystemBase {
  private static final double kCountsPerRevolution = 1440.0;
  private static final double kWheelDiameterInch = 2.75591; // 70 mm

  private final RomiMotor m_leftMotor;
  private final RomiMotor m_rightMotor;
  private final Encoder m_leftEncoder;
  private final Encoder m_rightEncoder;

  private final DifferentialDrive m_diffDrive;

  @Inject
  public Chassis(@Named("leftMotor") RomiMotor leftMotor,
      @Named("rightMotor") RomiMotor rightMotor,
      @Named("leftEncoder") Encoder leftEncoder,
      @Named("rightEncoder") Encoder rightEncoder) {
    m_leftMotor = leftMotor;
    m_rightMotor = rightMotor;
    m_leftEncoder = leftEncoder;
    m_rightEncoder = rightEncoder;
    m_diffDrive = new DifferentialDrive(m_leftMotor, m_rightMotor);

    // Use inches as unit for encoder distances
    m_leftEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
    m_rightEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
    resetEncoders();

    // Invert right side since motor is flipped
    m_rightMotor.setInverted(true);
  }

  public void arcadeDrive(double xaxisSpeed, double zaxisRotate) {
    m_diffDrive.arcadeDrive(xaxisSpeed, zaxisRotate);
  }

  public void resetEncoders() {
    m_leftEncoder.reset();
    m_rightEncoder.reset();
  }

  public double getLeftDistanceInch() {
    return m_leftEncoder.getDistance();
  }

  public double getRightDistanceInch() {
    return m_rightEncoder.getDistance();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
