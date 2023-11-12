package frc.robot.commands;

import frc.robot.subsystems.Chassis;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** Default command to drive the Chassis. */
public class ChassisDriveCommand extends CommandBase {

  private final Chassis chassis;
  private final Supplier<Double> speedSupplier;
  private final Supplier<Double> rotateSupplier;

  /** Default command to drive the Chassis. */
  public ChassisDriveCommand(Chassis chassis, Supplier<Double> speed, Supplier<Double> rotation) {
    this.chassis = chassis;
    this.speedSupplier = speed;
    this.rotateSupplier = rotation;
    addRequirements(chassis);
  }

  @Override
  public void execute() {
    chassis.arcadeDrive(speedSupplier.get(), rotateSupplier.get());
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
