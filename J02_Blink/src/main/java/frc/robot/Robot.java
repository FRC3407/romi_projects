// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.romi.OnBoardIO.ChannelMode;
import edu.wpi.first.wpilibj.romi.OnBoardIO;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private final OnBoardIO onboardIO = new OnBoardIO(ChannelMode.OUTPUT, ChannelMode.OUTPUT);
  private final Timer timer = new Timer();
  private boolean lightsOn = false;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items
   * like diagnostics hat you want ran during disabled, autonomous, teleoperated
   * and test.
   */
  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    onboardIO.setGreenLed(false);
    onboardIO.setRedLed(false);
    onboardIO.setYellowLed(false);
    lightsOn = false;
    timer.reset();
    timer.start();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    if (timer.hasElapsed(0.5)) {
      lightsOn = !lightsOn;
      onboardIO.setRedLed(lightsOn);
      timer.reset();
      timer.start();
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    onboardIO.setGreenLed(false);
    onboardIO.setRedLed(false);
    onboardIO.setYellowLed(false);
    lightsOn = false;
    timer.reset();
    timer.start();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if (timer.hasElapsed(0.5)) {
      lightsOn = !lightsOn;
      onboardIO.setGreenLed(lightsOn);
      timer.reset();
      timer.start();
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    onboardIO.setGreenLed(false);
    onboardIO.setRedLed(false);
    onboardIO.setYellowLed(false);
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
    onboardIO.setGreenLed(false);
    onboardIO.setRedLed(false);
    onboardIO.setYellowLed(true);
    lightsOn = true;
    timer.reset();
    timer.start();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    if (timer.hasElapsed(0.5)) {
      lightsOn = !lightsOn;
      onboardIO.setYellowLed(lightsOn);
      timer.reset();
      timer.start();
    }
  }
}
