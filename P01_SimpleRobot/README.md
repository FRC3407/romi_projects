# Simple Robot for Python

This is a trivial Romi program that does not use the Command pattern, written in Python and using [RobotPy](https://robotpy.readthedocs.io/).

It drives the robot from the Xbox Controller.


----

## RobotPy environment setup

These programs run on version 3 of [Python](https://www.python.org/). On Windows, Python can be installed from the Microsoft Store.  You can check the version available to you with `python --version` .

Install RobotPy on your computer with [pip](https://en.wikipedia.org/wiki/Pip_(package_manager)):
* Windows:  `pip3 install robotpy robotpy-romi robotpy-commands-v2 wpilib`
* Linux/macOS:  `pip3 install robotpy robotpy-romi robotpy-commands-v2 wpilib`


## Running a RobotPy program on the Romi

To run the program you will need to explicitly use the ws-client option:
* Windows:  `python robot.py sim`
* Linux/macOS:  `python robot.py sim`

By default the WPILib simulation GUI will be displayed. To disable the display you can add the `--nogui` option.


## RobotPy References
* [RobotPy documentation](https://robotpy.readthedocs.io/)
* [RobotPy Github repository](https://github.com/robotpy)
* Chief Delphi
    * [RobotPy ROMI example](https://www.chiefdelphi.com/t/robotpy-romi-example-available/391632)
    * [Romi Python Help?](https://www.chiefdelphi.com/t/romi-python-help/437947)

