// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

/**
 * Wraps around another {@link MotorController}, but adds features to
 * characterize that motor. In particular, it allows us to the value
 * <code>kV</code> which is the volts per (meters per second) value.
 */
public class CharacterizingSpeedController implements MotorController, Sendable {

    final double MIN_SPEED = 0.1;
    final int MAX_DATA_POINTS = 50 * 30;
    final double MAX_VOLTAGE = 12.0;

    private List<Double> voltageList = new ArrayList<>(MAX_DATA_POINTS);
    private List<Double> velocityList = new ArrayList<>(MAX_DATA_POINTS);
    private boolean isRecording = false;
    private double intercept = 0.0;
    private double r2 = 0.0;
    private double kv = 0.0;
    private long previousTime = 0L;
    private double previousDistance = 0.0;
    private String label = "";

    private final MotorController baseMotorController;
    private final DoubleSupplier baseEncoder;

    public CharacterizingSpeedController(MotorController motorController, DoubleSupplier encoder) {
        baseMotorController = motorController;
        baseEncoder = encoder;
    }

    public void startRecording() {
        resetRecording();
        isRecording = true;
    }

    public void stopRecording() {
        if (voltageList.size() > 0) {
            LinearRegression regression = new LinearRegression(toArray(velocityList), toArray(voltageList));
            intercept = regression.intercept();
            r2 = regression.R2();
            kv = regression.slope();
            label = regression.toString();
            // System.out.println(regression);
        }
        resetRecording();
    }

    public void resetRecording() {
        voltageList.clear();
        velocityList.clear();
        previousTime = currentTimeMillis();
        previousDistance = baseEncoder.getAsDouble();
        isRecording = false;
    }

    // Rewrite this method if you have a better method of determining the current
    // time in milliseconds.
    private long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    // Rewrite this method if your base motor controller has a better way of
    // determining voltage.
    private double getCurrentVoltage(double speed) {
        return speed * MAX_VOLTAGE;
    }

    private double[] toArray(List<Double> list) {
        double[] a = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            a[i] = list.get(i);
        }
        return a;
    }

    public double getIntercept() {
        return intercept;
    }

    public double getR2() {
        return r2;
    }

    public double getKv() {
        return kv;
    }

    @Override
    public void set(double speed) {
        baseMotorController.set(speed);
        if (isRecording) {
            long currentTime = currentTimeMillis();
            double currentDistance = baseEncoder.getAsDouble();
            double velocity = (currentDistance - previousDistance) / (currentTime - previousTime);
            if (Math.abs(velocity) >= MIN_SPEED) {
                voltageList.add(getCurrentVoltage(speed));
                velocityList.add(velocity);
            }
            previousTime = currentTime;
            previousDistance = currentDistance;
        }
    }

    @Override
    public double get() {
        return baseMotorController.get();
    }

    @Override
    public void setInverted(boolean isInverted) {
        baseMotorController.setInverted(isInverted);
    }

    @Override
    public boolean getInverted() {
        return baseMotorController.getInverted();
    }

    @Override
    public void disable() {
        baseMotorController.disable();
    }

    @Override
    public void stopMotor() {
        baseMotorController.stopMotor();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.setActuator(true);
        builder.setSafeState(this::disable);
        builder.addDoubleProperty("Value", this::get, this::set);
        builder.addDoubleProperty("KV", this::getKv, null);
        builder.addDoubleProperty("R2", this::getR2, null);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.getClass().getSimpleName()).append('[');
        s.append(label);
        s.append(']');
        return s.toString();
    }
}
