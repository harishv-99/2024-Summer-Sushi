package edu.ftcsushi.robots.sushi.controllers;

import edu.ftcsushi.fsm.AbstractFsmContainer;
import edu.ftcsushi.gamepad.GamepadController;
import edu.ftcsushi.gamepad.GamepadKeys;
import edu.ftcsushi.robots.sushi.Constants;
import edu.ftcsushi.robots.sushi.Robot;
import edu.ftcsushi.robots.sushi.controllers.teleop.TeleOpButtonDriveState;
import edu.ftcsushi.robots.sushi.controllers.teleop.TeleOpStickDriveState;


public class TeleOpController extends AbstractFsmContainer {

    Robot robot;
    GamepadController gp1;
    public TeleOpController(Robot robot) {
        this.robot = robot;
        gp1 = robot.getGamepad1();

        createGamepadControls();

        // Add all the states
        TeleOpStickDriveState stickDriveState = new TeleOpStickDriveState(robot);
        addState(stickDriveState);

        TeleOpButtonDriveState buttonDriveState = new TeleOpButtonDriveState(robot);
        addState(buttonDriveState);

        // Initialize the container after adding all the states.
        initContainer();
    }

    public void createGamepadControls() {
        // Setup input from gamepad 1
        gp1.createIntervalDoubleUnit(Constants.INTERVAL_NAME_LATERAL,
                GamepadKeys.Stick.LEFT_STICK_X);
        gp1.createIntervalDoubleUnit(Constants.INTERVAL_NAME_AXIAL,
                GamepadKeys.Stick.LEFT_STICK_Y);
        gp1.createIntervalDoubleUnitUsingTriggers(Constants.INTERVAL_NAME_ANGULAR);

        gp1.createButton(Constants.BUTTON_NAME_MOVE, GamepadKeys.Button.SQUARE);
        gp1.createButton(Constants.BUTTON_NAME_STOP, GamepadKeys.Button.CIRCLE);
    }
}
