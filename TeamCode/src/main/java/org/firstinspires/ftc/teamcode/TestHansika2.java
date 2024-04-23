package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import edu.ftcsushi.RobotBase;
import edu.ftcsushi.robots.sushi.Robot;

@TeleOp
public class TestHansika2 extends LinearOpMode {
    Robot robot;

    public void runOpMode() {
        robot = new Robot();
        robot.runOpMode(this, RobotBase.OpModeType.TELEOP);
    }
}