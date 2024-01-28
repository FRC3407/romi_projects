// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPRamseteCommand;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import frc.robot.subsystems.Chassis;
import static frc.robot.Constants.*;

/**
 * Extends {@code PPRamseteCommand} to simplify making trajectory following
 * commands for the Romi.
 */
public class ChassisPathPlannerCommand extends PPRamseteCommand {

  private final Chassis chassis;

  public ChassisPathPlannerCommand(PathPlannerTrajectory trajectory, Chassis chassis) {
    super(
        trajectory,
        chassis::getPose,
        new RamseteController(RAMSETE_CONTROLLER_B, RAMSETE_CONTROLLER_ZETA),
        new SimpleMotorFeedforward(
            RAMSETE_FF_VOLTS,
            RAMSETE_FF_VOLT_SECS_PER_METER,
            RAMSETE_FF_VOLT_SECS_SQUARED_PER_METER),
        new DifferentialDriveKinematics(RAMSETE_TRACK_WIDTH_METERS),
        chassis::getWheelSpeeds,
        new PIDController(RAMSETE_PID_P, RAMSETE_PID_I, RAMSETE_PID_D),
        new PIDController(RAMSETE_PID_P, RAMSETE_PID_I, RAMSETE_PID_D),
        chassis::tankDriveVolts,
        true,
        chassis);
    this.chassis = chassis;
  }

  @Override
  public void initialize() {
    super.initialize();
    chassis.resetGyro();
    chassis.resetEncoders();
  }
}
