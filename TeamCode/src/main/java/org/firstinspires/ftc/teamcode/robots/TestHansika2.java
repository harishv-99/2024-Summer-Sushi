package org.firstinspires.ftc.teamcode.robots;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import edu.ftcsushi.fw.robotbase.RobotBase;
import edu.ftcsushi.robots.sushi.Robot;

@TeleOp
public class TestHansika2 extends LinearOpMode {
    Robot robot;

    public void runOpMode() {
        robot = new Robot(this, RobotBase.OpModeType.TELEOP,
                RobotBase.AllianceColor.RED,
                RobotBase.StartPosition.AWAY_FROM_AUDIENCE);
        robot.runOpMode();
    }
}