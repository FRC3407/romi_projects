package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.subsystems.Chassis;

import static frc.robot.Constants.*;

/**
 * Abstract parent class for all other path following commands.
 */
public abstract class AbstractTrajectoryCommand extends CommandBase {

  protected final Chassis chassis;
  protected Trajectory trajectory = null;
  protected RamseteCommand ramseteCommand = null;

  public AbstractTrajectoryCommand(Chassis chassis) {
    this.chassis = chassis;
    addRequirements(this.chassis);
  }

  protected abstract Trajectory generateTrajectory();

  protected RamseteCommand generateRamseteCommand() {
    this.ramseteCommand = new RamseteCommand(
        trajectory,
        this.chassis::getPose,
        new RamseteController(RAMSETE_CONTROLLER_B, RAMSETE_CONTROLLER_ZETA),
        new SimpleMotorFeedforward(RAMSETE_FF_VOLTS, RAMSETE_FF_VOLT_SECS_PER_METER,
            RAMSETE_FF_VOLT_SECS_SQUARED_PER_METER),
        new DifferentialDriveKinematics(RAMSETE_TRACK_WIDTH_METERS),
        this.chassis::getWheelSpeeds,
        new PIDController(RAMSETE_PID_P, RAMSETE_PID_I, RAMSETE_PID_D),
        new PIDController(RAMSETE_PID_P, RAMSETE_PID_I, RAMSETE_PID_D),
        this.chassis::tankDriveVolts,
        this.chassis);
    return this.ramseteCommand;
  }

  @Override
  public void initialize() {
    if (this.trajectory == null) {
      this.trajectory = generateTrajectory();
    }
    if (this.ramseteCommand == null) {
      this.ramseteCommand = generateRamseteCommand();
    }
    chassis.resetOdometry(trajectory.getInitialPose());
    ramseteCommand.initialize();

    if (DEBUG) {
      System.out.println("initializing " + this);
    }
  }

  @Override
  public void execute() {
    ramseteCommand.execute();
  }

  @Override
  public void end(boolean interrupted) {
    ramseteCommand.end(interrupted);
  }

  @Override
  public boolean isFinished() {
    return ramseteCommand.isFinished();
  }

  @Override
  public String toString() {
    return ((this.getClass().getSimpleName()) + "(" + this.trajectory + ")");
  }

  static class WrappedTrajectory extends Trajectory {

    private double totalTimeSeconds = -1.0;
    private final Trajectory innTrajectory;

    public WrappedTrajectory(Trajectory trajectory) {
      this(trajectory, -1.0);
    }

    public WrappedTrajectory(Trajectory trajectory, double seconds) {
      super(trajectory.getStates());
      innTrajectory = trajectory;
      totalTimeSeconds = seconds;
    }

    @Override
    public double getTotalTimeSeconds() {
      return totalTimeSeconds > 0 ? totalTimeSeconds : innTrajectory.getTotalTimeSeconds();
    }

    public void setTotalTimeSeconds(double seconds) {
      totalTimeSeconds = seconds;
    }

    @Override
    public String toString() {
      return "WrappedTrajectory(" + totalTimeSeconds + ")";
    }
  }
}