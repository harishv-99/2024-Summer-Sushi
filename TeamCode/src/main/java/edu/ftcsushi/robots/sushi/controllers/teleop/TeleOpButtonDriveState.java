package edu.ftcsushi.robots.sushi.controllers.teleop;

import edu.ftcsushi.fsm.AbstractFsmState;
import edu.ftcsushi.gamepad.GamepadController;
import edu.ftcsushi.robots.sushi.Constants;
import edu.ftcsushi.robots.sushi.Robot;
import edu.ftcsushi.util.RoadRunner;

public class TeleOpButtonDriveState extends AbstractFsmState {
    Robot robot;
    GamepadController gp1;

    public TeleOpButtonDriveState(Robot robot) {
        this.robot = robot;
        gp1 = robot.getGamepad1();
    }

    @Override
    public void initState() {
    }

    @Override
    public void executeState() {
        if (gp1.getButton(Constants.BUTTON_NAME_MOVE).isDown()) {
            robot.driveTrainSubsystem.drive(RoadRunner.toPoseVelocity2d(0, 0.2, 0));
            robot.getTelemetry().addLine(Constants.BUTTON_NAME_MOVE);
        }

        if (gp1.getButton(Constants.BUTTON_NAME_STOP).isDown())
            robot.driveTrainSubsystem.drive(RoadRunner.toPoseVelocity2d(0,0,0));

        robot.getTelemetry().addData("Voltage", robot.driveTrainSubsystem.getVoltage());
        robot.getTelemetry().addData("Current", robot.driveTrainSubsystem.getCurrent());

        if (robot.driveTrainSubsystem.hasRunIntoObstacle())
            robot.driveTrainSubsystem.drive(RoadRunner.toPoseVelocity2d(0,-0.2,0));

        // Check for transitions out of the state
        if (hasStickMoved())
            getContainer().transitionToState(Constants.TELEOP_STICK_DRIVE_STATE);
    }

    @Override
    public void exitState() {

    }

    private boolean hasStickMoved() {
        return (gp1.getInterval(Constants.INTERVAL_NAME_LATERAL).getValue() != 0 ||
                gp1.getInterval(Constants.INTERVAL_NAME_AXIAL).getValue() != 0 ||
                gp1.getInterval(Constants.INTERVAL_NAME_ANGULAR).getValue() != 0);
    }
}
