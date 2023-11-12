package frc.robot.commands;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import frc.robot.subsystems.Chassis;
import java.util.List;

import static frc.robot.Constants.*;

/**
 * Command that drives the robot through a list of {@link Translation2d} waypoints.
 */
public class ChassisWaypointCommand extends AbstractTrajectoryCommand {

  private final List<Translation2d> waypoints;
  private final Pose2d startPose;
  private final Pose2d endPose;

  /**
   * @param startPose initial robot pose before driving.
   * @param waypoints list of waypoints, measured in meters.
   * @param endPose   final robot pose after driving.
   * @param chassis   The {@link Chassis} subsystem.
   */
  public ChassisWaypointCommand(Pose2d startPose, List<Translation2d> waypoints, Pose2d endPose, Chassis chassis) {
    super(chassis);
    this.waypoints = waypoints;
    this.startPose = startPose;
    this.endPose = endPose;
    this.trajectory = generateTrajectory();
  }

  /**
   * Visit waypoints, ending in the starting position.
   * 
   * @param waypoints List of waypoints, measured in meters.
   * @param chassis   The {@link Chassis} subsystem.
   */
  public ChassisWaypointCommand(List<Translation2d> waypoints, Chassis chassis) {
    this(new Pose2d(0, 0, new Rotation2d(0)),
        waypoints,
        new Pose2d(0.0, 0, new Rotation2d(Math.PI)),
        chassis);
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

    Trajectory trajector = TrajectoryGenerator.generateTrajectory(
        this.startPose,
        this.waypoints,
        this.endPose,
        config);

    return new WrappedTrajectory(trajector);
  }
}