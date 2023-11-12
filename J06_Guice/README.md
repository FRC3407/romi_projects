# Command Based Program with Guice

This is a sample Command-based Romi project that uses dependency injection with [Google Guice](https://github.com/google/guice).  The Guice library does [Dependency Injection](https://en.wikipedia.org/wiki/Dependency_injection) of low-level objects into the Subsystems and Commands.

## Dependency Injection

Dependency Injection is a recommended practice within software engineering.  Objects within your robot program (such as the `Chassis` subsystem) depend on certain other objects (such as motor controllers or encoders). When doing dependency injection, those motors and encoders are created and configured before the `Chassis` object is created.  References to the motor controllers and encoders are then passed in to the `Chassis` constructor.

This allows Subsystems to be [loosely coupled](https://en.wikipedia.org/wiki/Loose_coupling) to their low-level implementations.  So, for instance, the `Chassis` object won't depend on exactly what type of motor controller is passed, or how those motor controllers are configured. This also supports the concept of [Separation of Concerns](https://en.wikipedia.org/wiki/Separation_of_concerns), meaning that the `Chassis` class should only contain code about driving the chassis and no code specific to the motors.  A separate class, called `RobotConfig` is concerned with the details of configuring motor controllers and encoders.

Hopefully, the separation of concerns allows your project to be more readable.  It should be easier to understand and modify.

Besides these advantages, dependency injection supports testability, since automated tests may be created which pass in "mock" objects.  Testability should improve overall software quality, and it supports the practice of [refactoring](https://en.wikipedia.org/wiki/Code_refactoring), which is a methodical practice for making improvements to your code.

Google Guice supports dependency injection.  It allows the low-level objects to be created and configured in the `RobotConfig` class.  The low-level objects will later be passed into Subsystems and Commands whenever the `@Inject` annotation is present.  The injections are transitive;  when `RobotConfig` creates an instance of `RobotContainer`, all subsystem instances will be injected, and then all motors and encoders will be injected into the subsystems.

# Project organization

Think of the Java classes in the main package in this way:

* `Robot` defines the life cycle of the robot, as it is created and as it moves between operating modes.  The `Robot` object creates the instance of the `RobotContainer` object, which defines overall robot code.
* `Constants` defines the "magic numbers" on the robot, such as PWM channels, CAN IDs, PID configurations.  It mostly contains primitive objects for these values: numbers, booleans, and strings.
* `RobotConfig` sets up the low-level components (such as motors and sensors), subject to the values specified in `Constants`.  It configures the Guice bindings and provides a `getInstance()` method for creating fully injected objects.
* `RobotContainer` sets up and configures the high-level components (such as Subsystems and Commands) using the low-level objects from `RobotConfig`.  It links Commands to buttons on the game controllers.  Also, this class sets up the Autonomous Command.

## Changes to the Command template project to support Guice

This project is only a small modification to the standard Command template project.  The intention is that this will make it easier to understand the small changes necessary to support dependency injection.  

1. Add the following lines to the "dependencies" section of `build.gradle`.
```
testImplementation "org.mockito:mockito-core:5.+"
implementation group: 'com.google.inject', name: 'guice', version: '7.0.0'
```

2. Add the `RobotConfig` class.  This is a new class that was not in previous projects.

3. The `RobotContainer` class will contain variable declations for each of the subystems.  Change these declarations so they use the `@Inject` annotation, rather than calling the subsystem class constructor.

4. Look through your subsystem classes for all declarations of low-level objects, such as motors and sensors.  Copy these definitions into bindings within the `RobotConfig` class.  You may need to give the binding objects names.  Then change change all the subsystems so the low-level objects are set up with "Constructor Injection" using the `@Inject` and `@Named` annotations.

5. Each subsystem class should be marked with a `@Singleton` annotation.  Also, `RobotContainer` should be marked as `@Singleton`.

6. Create unit tests that inject mock objects into the classes under test.  The default WPILib Gradle build supports [Junit 5](https://junit.org/junit5/), also known as Junit Jupiter.  With the above-mentioned change to `build.gradle`, you will also have access to [Mockito 5](https://javadoc.io/doc/org.mockito/mockito-core/5.3.0/org/mockito/Mockito.html).

## Note
Guice injects values a runtime, not a compile.  This can cause confusing situations if bindings are implemented dynamically.  Problems can be avoided by first calling `RobotConfig` early in the `robotInit` method.  The only thing that should be executed before `RobotConfig` might be reading configuration property files. 

The runtime nature of Guice injection might also mean that classes that depend on injection might not get recompiled by gradle even though you think you've changed what will be injected.  If it ever looks like changes haven't kicked in, just execute `./gradlew clean`.  This will force gradle to rebuild all the Guice changes, and things will work again.

Guice doesn't inject to static variables.
