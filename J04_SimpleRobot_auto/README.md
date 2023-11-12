# Simple Robot with simple autonomous

This is a trivial Romi program that does not use the Command pattern.

While in teleop mode, the robot is controlled by the xbox controller in arcade mode.

While in autonomous, the robot drives forward, rotates arround, and then drives back, and then stops.

Autonomous is implemented as a [state machine](https://refactoring.guru/design-patterns/state), which is an extremely useful softare pattern.  A state machine goes through a series of _states_.  It can only be in one state at a time.  When programming a finite state machine, we define two things for each state:
* What the robot should be doing in that state.  For instance it can be driving, turning, shooting, waiting, etc.
* When to transition to a new state, and what that new state should be. For instance, switch states after 5 seconds, or switch to a different state if it bumps into something.

State machines are especially useful in FRC robots.  The state processing only takes a fraction of a second, and we can run many of them simultaneously.

![State Machine](./img/romi_state_machine.drawio.svg)