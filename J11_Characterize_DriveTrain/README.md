# Characterize Drive Train

This is a trivial Romi program that does not use the Command pattern.

The goal of this program is to calculate the value `kV` for your robot, measured in volts per meters per second.  This is the primary value necessary to calculate the feed-forward voltage for a [closed-loop velocity drive](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/introduction-to-feedforward.html).

The process for characterizing your drive train is to:
1. Enable teleop mode.
2. Drive the robot forward and backwards.  Don't go longer than 30 seconds
3. Disable teleop mode.  The values of `kV` will then be printed out to the console for both the left and right sides of the robot.

Although this was written for the Romi, it should be straight-forward to adapt it to any FRC robot.
