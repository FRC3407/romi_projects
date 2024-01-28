// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.Constants.*;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.ChassisDriveCommand;
import frc.robot.commands.ChassisPathPlannerCommand;
import frc.robot.subsystems.Chassis;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure ofthe robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private final Chassis chassis = new Chassis();

  private final XboxController xboxController = new XboxController(0);

  private SendableChooser<Command> autonomousChooser = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    configureButtonBindings();

    configureAutonomousCommands();

    SmartDashboard.putData("Auto Mode", autonomousChooser);
  }

  /**
   * Use this method to define your button->command mappings.
   */
  private void configureButtonBindings() {
    chassis.setDefaultCommand(new ChassisDriveCommand(chassis,
        () -> xboxController.getRawAxis(1),
        () -> xboxController.getRawAxis(0)));
  }

  /**
   * Use this method to set up all your autonomous commands.
   */
  private void configureAutonomousCommands() {
    PathConstraints pathConstraints = new PathConstraints(ROBOT_MAX_SPEED, ROBOT_MAX_ACCELLERATION);

    PathPlannerTrajectory loopTrajectory = PathPlanner.generatePath(
        pathConstraints,
        new PathPoint(new Translation2d(0.0, 0.0), Rotation2d.fromDegrees(0)), 
        new PathPoint(new Translation2d(0.6, -0.3), Rotation2d.fromDegrees(0)), 
        new PathPoint(new Translation2d(1.0, 0.0), Rotation2d.fromDegrees(90)), 
        new PathPoint(new Translation2d(0.6, 0.3), Rotation2d.fromDegrees(180)), 
        new PathPoint(new Translation2d(0.1, 0.3), Rotation2d.fromDegrees(180)), 
        new PathPoint(new Translation2d(0.0, 0.3), Rotation2d.fromDegrees(180)) 
    );
    autonomousChooser.setDefaultOption("Loop Trajectory", new ChassisPathPlannerCommand(loopTrajectory, chassis));

    PathPlannerTrajectory slalomTrajectory = PathPlanner.loadPath("Slalom", pathConstraints);
    autonomousChooser.addOption("Slalom Trajectory", new ChassisPathPlannerCommand(slalomTrajectory, chassis));

    PathPlannerTrajectory straightTrajectory = PathPlanner.loadPath("Straight", pathConstraints);
    autonomousChooser.addOption("Straight Trajectory", new ChassisPathPlannerCommand(straightTrajectory, chassis));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autonomousChooser.getSelected();
  }

  public void resetRobot() {
    chassis.resetGyro();
    chassis.resetEncoders();
  }
}
