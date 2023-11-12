package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.romi.RomiMotor;

public class Robot extends TimedRobot {

  private final RomiMotor m_leftMotor = new RomiMotor(0);
  private final RomiMotor m_rightMotor = new RomiMotor(1);
  private final XboxController xboxController = new XboxController(0);

  /**
   * This function is run once when the robot is first started up.
   */
  @Override
  public void robotInit() {
  }

  /**
   * This function is run 50 times a second whenever the robot is enabled.
   */
  @Override
  public void robotPeriodic() {
  }

  /** This function is run once whenever the robot is disabled. */
  @Override
  public void disabledInit() {
  }

  /**
   * This function is run once whenever the robot is enabled in Autonomous mode.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is run 50 times a second while the robot is enabled in
   * Authonomous mode.
   */
  @Override
  public void autonomousPeriodic() {
  }

  /**
   * This function is run once whenever the robot is enabled in Teleoperated mode.
   */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is run 50 times a second while the robot is enabled in
   * Teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {
    double speedLeft = xboxController.getRawAxis(1);
    double speedRight = xboxController.getRawAxis(3) * -1;
    m_leftMotor.set(speedLeft);
    m_rightMotor.set(speedRight);
  }

  /** This function is run once whenever the robot is enabled in Test mode. */
  @Override
  public void testInit() {
  }

  /**
   * This function is run 50 times a second while the robot is enabled in
   * Test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
