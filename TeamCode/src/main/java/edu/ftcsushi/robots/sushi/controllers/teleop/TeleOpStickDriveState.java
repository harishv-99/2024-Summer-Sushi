package edu.ftcsushi.robots.sushi.controllers.teleop;

import edu.ftcsushi.fsm.AbstractFsmState;
import edu.ftcsushi.gamepad.GamepadController;
import edu.ftcsushi.robots.sushi.Constants;
import edu.ftcsushi.robots.sushi.Robot;
import edu.ftcsushi.util.RoadRunner;

public class TeleOpStickDriveState extends AbstractFsmState {
    Robot robot;
    GamepadController gp1;

    public TeleOpStickDriveState(Robot robot) {
        this.robot = robot;
        gp1 = robot.getGamepad1();
    }

    @Override
    public void initState() {

    }

    @Override
    public void executeState() {
        // Get the input from the operator
        double axial = gp1.getInterval(Constants.INTERVAL_NAME_AXIAL).getValue();
        double lateral = gp1.getInterval(Constants.INTERVAL_NAME_LATERAL).getValue();
        double angular = gp1.getInterval(Constants.INTERVAL_NAME_ANGULAR).getValue();

        // Give more control to the user by reducing the impact of small changes
        axial = Math.copySign(Math.pow(axial, 4), axial);
        lateral = Math.copySign(Math.pow(lateral, 4), lateral);
        angular = Math.copySign(Math.pow(angular, 4), angular);

        robot.driveTrainSubsystem.drive(RoadRunner.toPoseVelocity2d(lateral, axial, angular));

        robot.getTelemetry().addData("Voltage", robot.driveTrainSubsystem.getVoltage());
        robot.getTelemetry().addData("Current", robot.driveTrainSubsystem.getCurrent());

        // Transition to button state if button is pushed.
        if (hasButtonBeenPressed())
            getContainer().transitionToState(Constants.TELEOP_BUTTON_DRIVE_STATE);
    }

    @Override
    public void exitState() {

    }

    private boolean hasButtonBeenPressed() {
        return (gp1.getButton(Constants.BUTTON_NAME_MOVE).hasChangedState() ||
                gp1.getButton(Constants.BUTTON_NAME_STOP).hasChangedState());
    }
}
