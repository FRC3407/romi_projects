import os
import wpilib
import wpilib.drive
import commands2
import romi

class MyRobot(commands2.TimedCommandRobot):
    def robotInit(self):
        self.leftMotor = wpilib.Spark(0)
        self.rightMotor = wpilib.Spark(1)
        self.rightMotor.setInverted(True)
        self.drive = wpilib.drive.DifferentialDrive(self.leftMotor, self.rightMotor)
        self.controller = wpilib.Joystick(0)

    def autonomousInit(self):
        pass

    def autonomousPeriodic(self):
        pass

    def teleopInit(self):
        pass

    def teleopPeriodic(self):
        speed = self.controller.getRawAxis(1)
        rotation = self.controller.getRawAxis(0)
        self.drive.arcadeDrive(speed, rotation)


if __name__ == "__main__":
    # If your ROMI isn't at the default address, set that here
    os.environ["HALSIMWS_HOST"] = "10.0.0.2"
    os.environ["HALSIMWS_PORT"] = "3300"

    wpilib.run(MyRobot)
