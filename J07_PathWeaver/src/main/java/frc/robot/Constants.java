// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.util.Config.cleanAllPreferences;
import static frc.robot.util.Config.loadConfiguration;
import static frc.robot.util.Config.printPreferences;
import edu.wpi.first.wpilibj.Preferences;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {

    /** DEBUG enables extra logging and Shuffleboard widgets. */
    public static boolean DEBUG = false;

    public static final double ROMI_COUNTS_PER_REVOLUTION = 1440.0;
    public static final double ROMI_WHEEL_DIAMETER_INCH = 2.75591; // 70 mm
    public static final double ROMI_WHEEL_DIAMETER_METER = 0.07;

    /** Maximum robot speed in meters per second. */
    public static final double ROBOT_MAX_SPEED = 0.8;
    /** Maximum robot accelleration in meters per second squared. */
    public static final double ROBOT_MAX_ACCELLERATION = 0.8;

    public static final double RAMSETE_CONTROLLER_B = 2.0;
    public static final double RAMSETE_CONTROLLER_ZETA = 0.7;
    public static final double RAMSETE_PID_P = 0.085;
    public static final double RAMSETE_PID_I = 0.0;
    public static final double RAMSETE_PID_D = 0.0;
    public static final double RAMSETE_FF_VOLTS = 0.929;
    public static final double RAMSETE_FF_VOLT_SECS_PER_METER = 6.33;
    public static final double RAMSETE_FF_VOLT_SECS_SQUARED_PER_METER = 0.0389;
    public static final double RAMSETE_TRACK_WIDTH_METERS = 0.142072613;
    public static final double RAMSETE_MAX_VOLTAGE = 10.0;

    public static void init(String... fileNames) {
        cleanAllPreferences();
        loadConfiguration(fileNames);
        printPreferences(System.out);

        DEBUG = Preferences.getBoolean("DEBUG", false);
    }
}
