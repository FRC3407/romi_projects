package frc.robot.util;

import static frc.robot.Constants.*;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

/**
 * Wraps around an existing MotorController, but applies PID control on the
 * speed.
 */
public class PidMotorController implements MotorController {
    private final MotorController m_motor;
    private final SimpleMotorFeedforward m_feedforward;
    private final PIDController m_PIDController;
    private final DoubleSupplier m_encoder;
    private final double m_maxVelocity;
    private double currentSpeed;
    private int inverted = 1;

    public PidMotorController(
            MotorController motor,
            DoubleSupplier encoder) {
        m_motor = motor;
        m_encoder = encoder;

        m_maxVelocity = ROBOT_MAX_SPEED;

        m_feedforward = new SimpleMotorFeedforward(RAMSETE_FF_VOLTS,
                RAMSETE_FF_VOLT_SECS_PER_METER,
                RAMSETE_FF_VOLT_SECS_SQUARED_PER_METER);

        m_PIDController = new PIDController(CHASSIS_DRIVE_P, CHASSIS_DRIVE_I, CHASSIS_DRIVE_D);
    }

    /** @param relativeSpeed Speed in the range -1.0 to 1.0. */
    @Override
    public void set(double relativeSpeed) {
        double velocityMetersPerSecond = relativeSpeed * m_maxVelocity * inverted;
        double encoderMetersPerSecond = m_encoder.getAsDouble();
        double feedforwardVoltage = m_feedforward.calculate(velocityMetersPerSecond);
        double feedbackVoltage = m_PIDController.calculate(encoderMetersPerSecond, velocityMetersPerSecond);
        m_motor.setVoltage(feedforwardVoltage + feedbackVoltage);
        this.currentSpeed = relativeSpeed;
    }

    /** @Return Speed in the range -1.0 to 1.0. */
    @Override
    public double get() {
        return currentSpeed;
    }

    @Override
    public void setInverted(boolean isInverted) {
        inverted = isInverted ? -1 : 1;
    }

    @Override
    public boolean getInverted() {
        return inverted == -1;
    }

    @Override
    public void disable() {
        m_motor.disable();
    }

    @Override
    public void stopMotor() {
        set(0.0);
        m_motor.stopMotor();
    }

}
