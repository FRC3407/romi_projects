package frc.robot.subsystems;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.romi.RomiMotor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChassisTest {

    RomiMotor leftMotor;
    RomiMotor rightMotor;
    Encoder leftEncoder;
    Encoder rightEncoder;

    @BeforeEach
    public void setup() {
        leftMotor = mock(RomiMotor.class);
        rightMotor = mock(RomiMotor.class);
        leftEncoder = mock(Encoder.class);
        rightEncoder = mock(Encoder.class);
    }

    @Test
    public void testDriveStraight() {
        // Arrange
        Chassis chassis = new Chassis(leftMotor, rightMotor, leftEncoder, rightEncoder);

        // Act
        chassis.arcadeDrive(1.0, 0.0);

        // Assert
        verify(leftMotor).set(1.0);
        verify(rightMotor).set(1.0);
    }

    @Test
    public void testTurn() {
        // Arrange
        Chassis chassis = new Chassis(leftMotor, rightMotor, leftEncoder, rightEncoder);

        // Act
        chassis.arcadeDrive(0.0, 1.0);

        // Assert
        verify(leftMotor).set(-1.0);
        verify(rightMotor).set(1.0);
    }
}