package edu.ftcsushi.robots.sushi.controllers.misc;

import edu.ftcsushi.fw.robotbase.statehistory.AbstractRobotStateWriter;
import edu.ftcsushi.fw.robotbase.statehistory.RobotStateEntry;
import edu.ftcsushi.fw.robotbase.statehistory.RobotStateHistory;
import edu.ftcsushi.fw.robotbase.statehistory.componentstate.PoseEntry;
import edu.ftcsushi.robots.sushi.Robot;

public class RobotStateWriter extends AbstractRobotStateWriter {

    final Robot robot;
    final RobotStateHistory<Robot.Components> robotStateHistory;

    public RobotStateWriter(Robot robot) {
        super(robot.getPeriodicRunner());

        this.robot = robot;
        this.robotStateHistory = robot.getRobotStateHistory();
    }

    @Override
    public void onPeriodic() {
        // Create a new state entry
        RobotStateEntry<Robot.Components> stateEntry = new RobotStateEntry<>();

        // Add chassis state
        robot.driveTrainSubsystem.updatePoseEstimate();
        PoseEntry chassisPose = new PoseEntry(
                robot.driveTrainSubsystem.getPose());
        stateEntry.addComponentStateEntry(Robot.Components.CHASSIS, chassisPose);
        robotStateHistory.addRobotStateEntry(stateEntry, robot.getTimePeriodicStartNanoseconds());
    }
}
