package frc.robot.commands.autos;

import java.util.List;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.TheGreatBalancingAct;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.swerve.SwerveDrivetrain;


public class Balance extends SequentialCommandGroup {
    public Balance(SwerveAutoBuilder autoBuilder,  SwerveDrivetrain swerve, Shooter shoot, Wrist wrist) {
        List<PathPlannerTrajectory> pathGroup = PathPlannerAutos.getPathGroup("Balance");
        
        addCommands(
            Commands.sequence(
                autoBuilder.resetPose(pathGroup.get(0)),
                wrist.motionMagicCommand(WristConstants.kWristHigh)),
                shoot.outtakeHigh(),
                Commands.waitSeconds(1),
                shoot.setPower(0),
                wrist.motionMagicCommand(WristConstants.kWristStow),
                autoBuilder.followPathWithEvents(pathGroup.get(0)),
                autoBuilder.followPathWithEvents(pathGroup.get(1)),
                new TheGreatBalancingAct(swerve)
            );
    }
    
}
