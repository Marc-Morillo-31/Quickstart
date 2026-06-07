
package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class Drive {
    // This declares the drivetrain motors.
    public static DcMotorEx frontLeft;
    public static DcMotorEx frontRight;
    public static DcMotorEx backLeft;
    public static DcMotorEx backRight;
    public static Gamepad previousGamepad = new Gamepad();
    public static boolean robotCentric = true;
    // This declares the IMU needed to get the current direction the robot is facing
    IMU imu;

    public static double driveP = 0.02, driveI = 0, driveD = 0.001, driveF = 0;
    private PIDFController turnController = new PIDFController(driveP, driveI, driveD, driveF);

    public Drive(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        // We set the left motors in reverse which is needed for drive trains where the left
        // motors are opposite to the right ones.
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);

        
        // Set to RUN_WITHOUT_ENCODER for Pedro Pathing
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        // Sets motor zero power behavior
        // Drivetrain is BRAKE
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu = hardwareMap.get(IMU.class, "imu");
        // This needs to be changed to match the orientation on your robot
        // Realisically, it might not be these directions

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.UP;

        RevHubOrientationOnRobot orientationOnRobot = new
                RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
    }

    @Override
    public void update(Gamepad gamepad, Gamepad previousGamepad, double voltageMultiplier) {

        telemetry.addLine("Press A to reset Yaw (for field relative)");
        telemetry.addLine("Press Left Bumper to switch between robot centric and field relative");

        // If you press the A button, then you reset the Yaw to be zero from the way
        // the robot is currently pointing
        // This is useful if you are doing field relative and you want to reset the direction you are considering to be forward

        if (gamepad1.a) {
            imu.resetYaw();
        }


        if (gamepad1.left_bumper && !previousGamepad.left_bumper) {
            robotCentric = !robotCentric;
        }

        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        // frontLeftMotor.setPower(y + x + rx);
        // backLeftMotor.setPower(y - x + rx);
        // frontRightMotor.setPower(y - x - rx);
        // backRightMotor.setPower(y + x - rx);
        if (!robotCentric) {
            driveFieldRelative(y, x, rx);
        } else {
            drive(y, x, rx);
        }
    }

    // This routine drives the robot field relative
    private void driveFieldRelative(double y, double x, double rx) {
        // First, convert direction being asked to drive to polar coordinates
        double theta = Math.atan2(y, x);
        double r = Math.hypot(x, y);

        // Second, rotate angle by the angle the robot is pointing
        theta = AngleUnit.normalizeRadians(theta -
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        // Third, convert back to cartesian
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        // Finally, call the drive method with robot relative forward and right amounts
        drive(newForward, newRight, rx * 1.1);
    }


    public void drive(double y, double x, double rx) {
        turnController.setP(driveP);
        turnController.setI(driveI);
        turnController.setD(driveD);
        turnController.setF(driveF);
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;


        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }
}
