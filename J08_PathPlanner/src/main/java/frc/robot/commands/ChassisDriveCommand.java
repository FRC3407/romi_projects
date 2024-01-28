package frc.robot.commands;

import static frc.robot.Constants.*;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Chassis;

/** Default command to drive the Chassis. */
public class ChassisDriveCommand extends Command {

  private final Chassis chassis;
  private final Supplier<Double> leftSpeedSupplier;
  private final Supplier<Double> rightSpeedSupplier;

  /** Default command to drive the Chassis. */
  public ChassisDriveCommand(Chassis chassis, Supplier<Double> speed, Supplier<Double> rotation) {
    this.chassis = chassis;
    this.leftSpeedSupplier = speed;
    this.rightSpeedSupplier = rotation;
    addRequirements(chassis);
  }

  @Override
  public void execute() {
    chassis.tankDriveVolts(RAMSETE_MAX_VOLTAGE * leftSpeedSupplier.get(),
        RAMSETE_MAX_VOLTAGE * rightSpeedSupplier.get());
  }
}
