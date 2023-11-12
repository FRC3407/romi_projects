package frc.robot.commands;

import frc.robot.subsystems.Chassis;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** Default command to drive the Chassis with a Joystick / XboxController. */
public class ChassisDriveCommand extends CommandBase {

  private final Chassis chassis;
  private final Supplier<Double> speedSupplier;
  private final Supplier<Double> rotateSupplier;

  /** Default command to drive the Chassis. */
  public ChassisDriveCommand(Supplier<Double> speedSupplier, Supplier<Double> rotateSupplier, Chassis chassis) {
    this.chassis = chassis;
    this.speedSupplier = speedSupplier;
    this.rotateSupplier = rotateSupplier;
    addRequirements(chassis);
  }

  @Override
  public void execute() {
    double speed = speedSupplier.get();
    double rotation = rotateSupplier.get();
    chassis.arcadeDrive(speed, rotation);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}