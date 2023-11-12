// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.util.Config.cleanAllPreferences;
import static frc.robot.util.Config.loadConfiguration;
import static frc.robot.util.Config.printPreferences;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotBase;

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

    public static final double ROMI_COUNTS_PER_REVOLUTION = 1440.0;
    public static final double ROMI_WHEEL_DIAMETER_METER = 0.07;

    /** Maximum robot speed in meters per second. */
    public static final double ROBOT_MAX_SPEED = 0.8;
    /** Maximum robot accelleration in meters per second squared. */
    public static final double ROBOT_MAX_ACCELLERATION = 0.8;

    /** Static gain, in volts. */
    public static final double RAMSETE_FF_VOLTS = 0.929;
    /** Velocity gain, in volts per (meters per second). */
    public static final double RAMSETE_FF_VOLT_SECS_PER_METER = 6.33;
    /** Acceleration gain, in volts per (meters per second squared). */
    public static final double RAMSETE_FF_VOLT_SECS_SQUARED_PER_METER = 0.0389;

    public static final double CHASSIS_DRIVE_P = 0.001;
    public static final double CHASSIS_DRIVE_I = 0.0;
    public static final double CHASSIS_DRIVE_D = 0.0;

    /** DEBUG enables extra logging and Shuffleboard widgets. */
    public static boolean DEBUG = false;

    /** LOGGING enables {@code DataLogManager} logging. */
    public static boolean LOGGING = false;
    public static boolean LOGGING_NT = false;
    public static boolean LOGGING_DS = true;
    public static String LOG_DIR = "";

    public static void init(String... fileNames) {
        cleanAllPreferences();
        loadConfiguration(fileNames);
        printPreferences(System.out);

        DEBUG = Preferences.getBoolean("DEBUG", false);
        LOGGING = Preferences.getBoolean("LOGGING", LOGGING);
        LOG_DIR = Preferences.getString("LOG_DIR", RobotBase.isSimulation() ? "logs" : "");
        LOGGING_NT = Preferences.getBoolean("LOGGING_NT", LOGGING_NT);
        LOGGING_DS = Preferences.getBoolean("LOGGING_DS", LOGGING_DS);
    }
}
