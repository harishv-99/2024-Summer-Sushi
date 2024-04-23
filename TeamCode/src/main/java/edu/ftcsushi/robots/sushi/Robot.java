package edu.ftcsushi.robots.sushi;

import edu.ftcsushi.RobotBase;
import edu.ftcsushi.robots.sushi.controllers.TeleOpController;
import edu.ftcsushi.robots.sushi.subsystems.DriveTrainSubsystem;

public class Robot extends RobotBase {
    public DriveTrainSubsystem driveTrainSubsystem;
    TeleOpController teleOpController;

    @Override
    protected void initRobot() {
        driveTrainSubsystem = new DriveTrainSubsystem(getHardwareMap(),
                getTelemetry());
    }

    @Override
    protected void onPeriodicRobot() {

    }

    @Override
    protected void exitRobot() {

    }

    @Override
    protected void initAutonomous() {

    }

    @Override
    protected void onPeriodicAutonomous() {

    }

    @Override
    protected void exitAutonomous() {

    }

    @Override
    protected void initTeleOp() {
        // Create a new teleOpController and initialize it.
        teleOpController = new TeleOpController(this);
    }

    @Override
    protected void onPeriodicTeleOp() {
        getTelemetry().addData("State", teleOpController.getCurrentState().getName());
        getTelemetry().update();

        teleOpController.executeContainer();
    }

    @Override
    protected void exitTeleOp() {

    }
}
