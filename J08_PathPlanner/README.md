# Path Following with PathPlanner

This project runs experiments with [PathPlanner](https://pathplanner.dev/) and the [Ramsete Command](https://pathplanner.dev/pplib-build-an-auto.html#ramsete-differential).  The PathPlanner application can be downloaded from the Microsoft Store or the Apple App Store.

If you specify PathPlanner's project as being your WPILib robotics directory, PathPlanner will manage its trajectories as JSON files under `src/main/deploy/pathplanner`.

![PathPlanner App](./img/PathPlanner.png)

## Trajectories

Trajectories can be created and edited within the PathPlanner app.  The can also be constructed manually in code.

Here's an manually constructed example with two waypoints:
```java
    Pose2d currentPose = chassis.getPose();

    Pose2d startPos = new Pose2d(currentPose.getTranslation(), new Rotation2d());
    Pose2d endPos = new Pose2d(currentPose.getTranslation().plus(new Translation2d(2.0, 0.0)), new Rotation2d());

    List<Translation2d> bezierPoints = PathPlannerPath.bezierFromPoses(startPos, endPos);
    PathPlannerPath path = new PathPlannerPath(
        bezierPoints,
        new PathConstraints(
            ROBOT_MAX_SPEED, ROBOT_MAX_ACCELLERATION,
            ROBOT_ANGULAR_MAX_SPEED, ROBOT_MAX_ANGULAR_ACCELLERATION),
        new GoalEndState(0.0, currentPose.getRotation()));
    path.preventFlipping = true;

    Command followCommand = AutoBuilder.followPath(path);
```
