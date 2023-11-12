// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

import static frc.robot.Constants.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure ofthe robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
@Singleton
public class RobotContainer {

  @Inject
  private Chassis chassis;

  @Inject
  private XboxController xboxController;

  private Command autonomousCommand = null;

  @Inject
  public void init() {
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.
   */
  private void configureButtonBindings() {
    chassis.setDefaultCommand(new ChassisDriveCommand(chassis,
        () -> xboxController.getRawAxis(1),
        () -> xboxController.getRawAxis(0) * -1));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autonomousCommand;
  }
}
