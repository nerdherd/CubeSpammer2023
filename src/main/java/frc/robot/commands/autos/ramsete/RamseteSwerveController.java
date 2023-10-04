
package frc.robot.commands.autos.ramsete;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import java.util.List;

import com.pathplanner.lib.PathPlannerTrajectory;

/**
 * Implements logic to convert a set of desired waypoints (ie, a trajectory) and the current
 * estimate of where the robot is at (ie, the estimated Pose) into motion commands for a drivetrain.
 * The Ramaste controller is used to smoothly move the robot from where it thinks it is to where it
 * thinks it ought to be.
 */
public class RamseteSwerveController {
    private Trajectory trajectory;

    private RamseteController ramsete;

    private Timer timer = new Timer();

    boolean isRunning = false;

    Trajectory.State desiredDtState;

    public RamseteSwerveController(Trajectory trajectory, RamseteController controller) {
        this.trajectory = trajectory;
        this.ramsete = controller;
        //trajectory.
    }

    public RamseteSwerveController(Trajectory trajectory) {
        this(trajectory, new RamseteController());
    }

    public void setNewTrajectory( Trajectory trajectory) // to be done: alot reset need to do
    {
        stopPath();
        this.trajectory = trajectory;
        //trajectory.
    }

    /**
     * @return The starting (initial) pose of the currently-active trajectory
     */
    public Pose2d getInitialPose() {
        return trajectory.getInitialPose();
    }

    /** Starts the controller running. Call this at the start of autonomous */
    public void startPath() {
        timer.reset();
        timer.start();
        isRunning = true;
    }

    /** Stops the controller from generating commands */
    public void stopPath() {
        isRunning = false;
        timer.reset();
    }

    /**
     * Given the current estimate of the robot's position, calculate drivetrain speed commands which
     * will best-execute the active trajectory. Be sure to call `startPath()` prior to calling this
     * method.
     *
     * @param curEstPose Current estimate of drivetrain pose on the field
     * @return The commanded drivetrain motion
     */
    public ChassisSpeeds calculateSpeeds(Pose2d curEstPose) {
        if (isRunning) {
            double elapsed = timer.get();
            desiredDtState = trajectory.sample(elapsed);

            // to be checked?? if the time is expired for this trajectory???
        } else {
            desiredDtState = new Trajectory.State();
        }

        return ramsete.calculate(curEstPose, desiredDtState);
    }

    /**
     * @return The position which the auto controller is attempting to move the drivetrain to right
     *     now.
     */
    public Pose2d getCurPose2d() {
        return desiredDtState.poseMeters;
    }

    public Trajectory.State getCurrentState()
    {
        return desiredDtState;
    }
}
