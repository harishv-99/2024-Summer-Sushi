package edu.ftcsushi.robots.sushi_old;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;


public class DriveTrain {
    private final DcMotor motorFrontLeft;
    private final DcMotor motorFrontRight;
    private final DcMotor motorBackLeft;
    private final DcMotor motorBackRight;

    private final IMU imu;


    private DriveTrain(DriveTrainBuilder builder) {

        this.motorFrontLeft = builder.motorFrontLeft;
        this.motorFrontRight = builder.motorFrontRight;
        this.motorBackLeft = builder.motorBackLeft;
        this.motorBackRight = builder.motorBackRight;
        this.imu = builder.imu;
    }


    public IMU getImu() {
        return imu;
    }


    public void setDrivePower(double x, double y, double rx) {
        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        motorFrontLeft.setPower(frontLeftPower);
        motorBackLeft.setPower(backLeftPower);
        motorFrontRight.setPower(frontRightPower);
        motorBackRight.setPower(backRightPower);
    }


    public void setDrivePower(DriveTrainGamepadInterpreter interpreter) {
        DrivePowerValues powerValues = interpreter.getDrivePowerValues();
        setDrivePower(powerValues.getDrivePowerX(), powerValues.getDrivePowerY(),
                powerValues.getDrivePowerRx());
    }

    // ------------------------------------------------------------------------

    /**
     * DriveTrainBuilder will allow you to construct and initialize a drive train
     */
    public static class DriveTrainBuilder {

        HardwareMap hardwareMap;

        private DcMotor motorFrontLeft;
        private DcMotor motorFrontRight;
        private DcMotor motorBackLeft;
        private DcMotor motorBackRight;
        private IMU imu;

        public DriveTrainBuilder(HardwareMap hardwareMap,
                                 String strFrontLeft,
                                 String strFrontRight,
                                 String strBackLeft,
                                 String strBackRight) {

            this.hardwareMap = hardwareMap;

            motorFrontLeft = hardwareMap.dcMotor.get(strFrontLeft);
            motorFrontRight = hardwareMap.dcMotor.get(strFrontRight);
            motorBackLeft = hardwareMap.dcMotor.get(strBackLeft);
            motorBackRight = hardwareMap.dcMotor.get(strBackRight);

//            motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        public DriveTrainBuilder reverseMotorFrontLeft() {
            motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            return this;
        }

        public DriveTrainBuilder reverseMotorFrontRight() {
            motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
            return this;
        }

        public DriveTrainBuilder reverseMotorBackLeft() {
            motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            return this;
        }

        public DriveTrainBuilder reverseMotorBackRight() {
            motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
            return this;
        }


        public DriveTrainBuilder useImu(String strImu,
                                         RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection,
                                         RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection) {

            this.imu = hardwareMap.get(IMU.class, strImu);

            IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                    logoFacingDirection,
                    usbFacingDirection));
            imu.initialize(parameters);

            return this;
        }

        public DriveTrain build() {
            return new DriveTrain(this);
        }
    }
}
