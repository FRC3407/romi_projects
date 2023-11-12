// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.math.util.Units.degreesToRadians;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.ChassisDriveCommand;
import frc.robot.subsystems.Chassis;;

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

  private Command autonomousCommand = null;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.
   */
  private void configureButtonBindings() {
    chassis.setDefaultCommand(new ChassisDriveCommand(
            () -> xboxController.getRawAxis(1),
            () -> xboxController.getRawAxis(0),
            chassis));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // autonomousCommand = new ChassisPathWeaverCommand("eight.wpilib.json",
    // chassis);

    // autonomousCommand = new ChassisPathWeaverCommand("eight.wpilib.json", chassis);

    // autonomousCommand = new ChassisWaypointCommand(List.of(
    // new Translation2d(0.5, 0.25),
    // new Translation2d(1.0, 0),
    // new Translation2d(0.5, -0.25)), chassis);
    //     new Translation2d(0.5, 0.25),
    //     new Translation2d(1.0, 0),
    //     new Translation2d(0.5, -0.25)), chassis);

    // autonomousCommand = new ChassisWaypointCommand(
    // new Pose2d(0, 0, new Rotation2d(0)),
    // List.of(
    // new Translation2d(0.7, 0.3),
    // new Translation2d(1.3, -0.3)),
    // new Pose2d(2.0, 0.0, new Rotation2d(0.0)),
    // chassis);
    //     new Pose2d(0, 0, new Rotation2d(0)),
    //     List.of(
    //         new Translation2d(0.7, 0.3),
    //         new Translation2d(1.3, -0.3)),
    //     new Pose2d(2.0, 0.0, new Rotation2d(0.0)),
    //     chassis);

    autonomousCommand = new ChassisPoseListCommand(
            List.of(
                    new Pose2d(0.7, 0.3, new Rotation2d(degreesToRadians(0))),
                    new Pose2d(1.3, -0.3, new Rotation2d(degreesToRadians(0))),
                    new Pose2d(1.5, 0.0, new Rotation2d(degreesToRadians(90)))),
            chassis);
    // autonomousCommand = new ChassisPoseListCommand(
    //     List.of(
    //         new Pose2d(0.7, 0.3, new Rotation2d(degreesToRadians(0))),
    //         new Pose2d(1.3, -0.3, new Rotation2d(degreesToRadians(0))),
    //         new Pose2d(1.5, 0.0, new Rotation2d(degreesToRadians(90)))),
    //     chassis);

    // autonomousCommand = new ChassisPoseListCommand(
    // List.of(
    // new Pose2d(0.0, inchesToMeters(24), new Rotation2d(degreesToRadians(0))),
    // new Pose2d(inchesToMeters(36), inchesToMeters(36), new
    // Rotation2d(degreesToRadians(0))),
    // new Pose2d(1.5, 0.0, new Rotation2d(degreesToRadians(90)))),
    // chassis);
    //     List.of(
    //         new Pose2d(0.0, inchesToMeters(24), new Rotation2d(degreesToRadians(0))),
    //         new Pose2d(inchesToMeters(36), inchesToMeters(36), new Rotation2d(degreesToRadians(0))),
    //         new Pose2d(1.5, 0.0, new Rotation2d(degreesToRadians(90)))),
    //     chassis);

    // List<Pose2d> poseWaypoints = List.of(
    // new Pose2d(0.7, 0.0, new Rotation2d(degreesToRadians(0))),
    // new Pose2d(1.3, 0.0, new Rotation2d(degreesToRadians(45))));
    // autonomousCommand = new ChassisPoseListCommand(poseWaypoints, chassis);
    // autonomousCommand = new ChassisWaypointCommand(
    //     new Pose2d(0, 0, new Rotation2d(0)),
    //     List.of(new Translation2d(0.7, 0.3)),
    //     new Pose2d(1.2, 0.0, new Rotation2d(-45)),
    //     chassis);

    Pose2d startPose = new Pose2d(0, 0, new Rotation2d(0));
    List<Translation2d> waypoints = List.of(new Translation2d(0.7, 0.3));
    Pose2d endPose = new Pose2d(1.2, 0.0, new Rotation2d(-45));
    autonomousCommand = new ChassisWaypointCommand(startPose, waypoints, endPose, chassis);
    // autonomousCommand = new ChassisWaypointCommand(
    //     new Pose2d(0, 0, new Rotation2d(0)),
    //     List.of(new Translation2d(0.7, 0.3)),
    //     new Pose2d(1.2, 0.0, new Rotation2d(-45)),
    //     chassis);

    autonomousCommand = new ChassisWaypointCommand(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(
                    new Translation2d(0.6, -0.3),
                    new Translation2d(1.0, 0.0),
                    new Translation2d(0.6, 0.3),
                    new Translation2d(0.1, 0.3)
            ),
            new Pose2d(0.0, 0.3, new Rotation2d(180)),
            chassis);

    return (new InstantCommand(() -> {
      chassis.resetEncoders();
      chassis.resetGyro();
    }))
            .andThen(autonomousCommand)
            .andThen(() -> chassis.stop());
  }
}

