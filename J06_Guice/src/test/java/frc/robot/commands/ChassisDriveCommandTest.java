package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import frc.robot.subsystems.Chassis;

public class ChassisDriveCommandTest {

    @Test
    public void testExecute() {
        // Arrange
        double speed = 0.4;
        double rotation = -0.7;

        Chassis chassis = mock(Chassis.class);
        Supplier<Double> speedSupplier = new MockSupplier(speed);
        Supplier<Double> rotationSupplier = new MockSupplier(rotation);
        ChassisDriveCommand command = new ChassisDriveCommand(chassis, speedSupplier, rotationSupplier);

        // Act
        command.initialize();
        command.execute();

        // Assert
        verify(chassis).arcadeDrive(speed, rotation);
        assertFalse(command.isFinished());
    }

    public static class MockSupplier implements Supplier<Double> {
        private final double value;

        public MockSupplier(double d) {
            this.value = d;
        }

        public Double get() {
            return value;
        }
    }
}