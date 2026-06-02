
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
    // This declares the intake and transfer motors.
    public static DcMotorEx intake;
    public static DcMotorEx transfer;
    


    public Intake(HardwareMap hardwareMap) {
        // Basic mapping for the shooter, transfer, and intake
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");
        
        
        
        

        // intake and transfer don't use encoders, while flywheels do for velocity control.
        // If there aren't encoders, you should remove the RUN_USING_ENCODER and just use RUN_WITHOUT_ENCODER
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        

        // Sets motor zero power behavior
        // intake, transfer, and flywheels are FLOAT
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        
    }

    @Override
    public void update(Gamepad gamepad, Gamepad previousGamepad, double voltageMultiplier) {
        double intakePower = 0;
        double transferPower = -1;
        if (gamepad1.right_trigger > 0) {
            intakePower = 1;
        } else if (gamepad1.left_trigger > 0){
            intakePower = -1;
        } else{
            intakePower = 0;
            transferPower = 0;
        }
        if (gamepad1.right_bumper) {
            intakePower = 1;
            transferPower = 1;
        }
        intake.setPower(intakePower);
        transfer.setPower(transferPower);
    }
    private void intake() {

    }
}
