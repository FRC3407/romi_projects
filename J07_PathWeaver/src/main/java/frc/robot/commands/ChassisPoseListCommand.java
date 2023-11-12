package frc.robot.commands;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import frc.robot.subsystems.Chassis;
import java.util.List;

import static frc.robot.Constants.*;

/**
 * Command that drives the robot through a list of {@link Pose2d} waypoints.
 */
public class ChassisPoseListCommand extends AbstractTrajectoryCommand {

  private final List<Pose2d> poseWaypoints;

  /**
   * @param poses   list of {@code Pose2d} waypoints, in meters and radians.
   * @param chassis The {@link Chassis} subsystem.
   */
  public ChassisPoseListCommand(List<Pose2d> poseWaypoints, Chassis chassis) {
    super(chassis);
    this.poseWaypoints = poseWaypoints;
    this.trajectory = generateTrajectory();
  }

  @Override
  protected Trajectory generateTrajectory() {
    DifferentialDriveKinematics driveKinematics = new DifferentialDriveKinematics(RAMSETE_TRACK_WIDTH_METERS);

    DifferentialDriveVoltageConstraint autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
        new SimpleMotorFeedforward(RAMSETE_FF_VOLTS, RAMSETE_FF_VOLT_SECS_PER_METER,
            RAMSETE_FF_VOLT_SECS_SQUARED_PER_METER),
        driveKinematics,
        RAMSETE_MAX_VOLTAGE);

    TrajectoryConfig config = new TrajectoryConfig(ROBOT_MAX_SPEED, ROBOT_MAX_ACCELLERATION)
        .setKinematics(driveKinematics)
        .addConstraint(autoVoltageConstraint);

    return TrajectoryGenerator.generateTrajectory(
        this.poseWaypoints,
        config);
  }
}