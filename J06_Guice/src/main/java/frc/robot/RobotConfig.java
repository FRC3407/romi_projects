package frc.robot;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.romi.RomiMotor;

import static frc.robot.Constants.*;

/**
 * Configures all low-level objects so they may be injected into higher-level
 * objects. Probably, this class will only be used in {@code RobotContainer}
 * when object instances are being created.
 */
public class RobotConfig extends AbstractModule {

    private static RobotConfig robotConfigInstance = null;
    private static Injector injectorInstance = null;

    private static synchronized Injector getInjectorInstance() {
        if (injectorInstance == null) {
            robotConfigInstance = new RobotConfig();
            injectorInstance = Guice.createInjector(robotConfigInstance);
        }
        return injectorInstance;
    }

    /**
     * Create a new instance of the given class, where all dependencies will be
     * injected by Guice.
     */
    public static <T> T getInstance(Class<T> type) {
        final long startTime = System.currentTimeMillis();
        try {
            T object = getInjectorInstance().getInstance(type);

            return object;
        } finally {
            if (DEBUG) {
                System.out.printf("RobotConfig.getInstance(%s) : config time = %,d ms \n",
                        type.getSimpleName(), (System.currentTimeMillis() - startTime));
            }
        }
    }

    private RobotConfig() {
    }

    /**
     * Define all Guice bindings for this robot.
     */
    @Override
    protected void configure() {

        // Game Controllers
        bind(XboxController.class)
                .toInstance(new XboxController(0));

        // Chassis motors
        bind(RomiMotor.class)
                .annotatedWith(Names.named("leftMotor"))
                .toInstance(new RomiMotor(LEFT_MOTOR_CHANNEL));
        bind(RomiMotor.class)
                .annotatedWith(Names.named("rightMotor"))
                .toInstance(new RomiMotor(RIGHT_MOTOR_CHANNEL));

        // Chassis encoders
        bind(Encoder.class)
                .annotatedWith(Names.named("leftEncoder"))
                .toInstance(new Encoder(LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B));
        bind(Encoder.class)
                .annotatedWith(Names.named("rightEncoder"))
                .toInstance(new Encoder(RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B));

        configurePreferences();
    }

    /**
     * Bind values from {@link Preferences} into Guice, so they can be injected via
     * annotation.
     */
    private void configurePreferences() {
        final NetworkTable prefsTable = NetworkTableInstance.getDefault().getTable("Preferences");
        for (String key : Preferences.getKeys()) {
            if (".type".equals(key)) continue;
            NetworkTableEntry entry = prefsTable.getEntry(key);
            String prefKey = "config." + key;
            switch (entry.getType()) {
                case kString:
                    bind(String.class)
                            .annotatedWith(Names.named(prefKey))
                            .toInstance(entry.getString(null));
                    break;
                case kBoolean:
                    bind(Boolean.class)
                            .annotatedWith(Names.named(prefKey))
                            .toInstance(Boolean.valueOf(entry.getBoolean(false)));
                    break;
                case kInteger:
                    bind(Integer.class)
                            .annotatedWith(Names.named(prefKey))
                            .toInstance(Integer.valueOf((int) entry.getInteger(0)));
                    bind(Long.class)
                            .annotatedWith(Names.named(prefKey))
                            .toInstance(Long.valueOf(entry.getInteger(0)));
                    break;
                case kFloat:
                    bind(Float.class)
                            .annotatedWith(Names.named(prefKey))
                            .toInstance(Float.valueOf(entry.getFloat(0.0f)));
                    break;
                case kDouble:
                    bind(Double.class)
                            .annotatedWith(Names.named(prefKey))
                            .toInstance(Double.valueOf(entry.getDouble(0.0)));
                    break;
                default:
                    System.out.println("configurePreferences: unknown type for " + key);
            }
        }
    }
}
