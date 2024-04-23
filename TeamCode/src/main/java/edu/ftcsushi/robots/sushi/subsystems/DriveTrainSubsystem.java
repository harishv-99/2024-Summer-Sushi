package edu.ftcsushi.robots.sushi.subsystems;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import edu.ftcsushi.robots.sushi.Constants;

public class DriveTrainSubsystem {
    private DcMotorEx motorBackLeft;
    private DcMotorEx motorFrontLeft;
    private DcMotorEx motorBackRight;
    private DcMotorEx motorFrontRight;
    private IMU imu;

    private VoltageSensor voltageSensor;
    private Telemetry telemetry;

    public DriveTrainSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        // Configure the motors
        motorBackLeft = hardwareMap.get(DcMotorEx.class, Constants.MOTOR_NAME_BACK_LEFT);
        motorFrontLeft = hardwareMap.get(DcMotorEx.class, Constants.MOTOR_NAME_FRONT_LEFT);
        motorBackRight = hardwareMap.get(DcMotorEx.class, Constants.MOTOR_NAME_BACK_RIGHT);
        motorFrontRight = hardwareMap.get(DcMotorEx.class, Constants.MOTOR_NAME_FRONT_RIGHT);

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Configure the IMU
        imu = hardwareMap.get(IMU.class, Constants.IMU_NAME);
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        imu.initialize(parameters);

        // Configure the voltage sensor
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");
        this.telemetry = telemetry;
    }

    public double getHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }


    public double getCurrent() {
        return motorFrontRight.getCurrent(CurrentUnit.MILLIAMPS);
    }

    public double getVoltage() {
        return voltageSensor.getVoltage();
    }

    public boolean hasRunIntoObstacle() {
        return (voltageSensor.getVoltage() < Constants.VOLTAGE_SENSOR_OBSTACLE_THRESHOLD);
    }

    public void drive(PoseVelocity2d powers) {
        double x = powers.linearVel.x;
        double y = powers.linearVel.y;
        double rx = powers.angVel;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        telemetry.addData("fl", frontLeftPower);
        telemetry.addData("bl", backLeftPower);
        telemetry.addData("fr", frontRightPower);
        telemetry.addData("br", backRightPower);

        motorFrontLeft.setPower(frontLeftPower);
        motorBackLeft.setPower(backLeftPower);
        motorFrontRight.setPower(frontRightPower);
        motorBackRight.setPower(backRightPower);
    }
}
