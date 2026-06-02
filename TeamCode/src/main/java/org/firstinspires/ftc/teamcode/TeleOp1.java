/* Copyright (c) 2025 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
/*
 * This OpMode illustrates how to program your robot to drive field relative.  This means
 * that the robot drives the direction you push the joystick regardless of the current orientation
 * of the robot.
 *
 * This OpMode assumes that you have four mecanum wheels each on its own motor named:
 *   front_left_motor, front_right_motor, back_left_motor, back_right_motor
 *
 *   and that the left motors are flipped such that when they turn clockwise the wheel moves backwards
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 *
 */
@TeleOp(name = "Basic Drive", group = "Robot")
@Disabled
public class Drive extends OpMode {
    public static double nominalVoltage = 14;


    public static DcMotor flywheel1;
    public static DcMotor flywheel2;
    public static Servo hood;
    public static int hoodMinPos = 0.1;
    public static int hoodMaxPos = 0.5;
    public static int flywheelMode = 0;
    public static Gamepad previousGamepad = new Gamepad();

    

    @Override
    public void init() {
        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
        
        hood = hardwareMap.get(Servo.class, "hood");
        hood.setPosition(hoodMinPos);

        // Reverse one of the flywheels so that they spin in opposite directions
        // Reverse flywheel1 if the flywheel spin the wrong direction
        flywheel2.setDirection(DcMotor.Direction.REVERSE);

        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flywheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void loop() {
        drive.update(gamepad1, previousGamepad, nominalVoltage / voltage);



        
        if (gamepad1.left_bumper && !previousGamepad.left_bumper){
            flywheelMode += 1;
        }
        if (flywheelMode % 3 == 0){
            flywheel1.setPower(0);
            flywheel2.setPower(0);
            hood.setPosition(hoodMinPos);
        } else if (flywheelMode % 3 == 1){
            flywheel1.setPower(0.5);
            flywheel2.setPower(0.5);
        } else if (flywheelMode % 3 == 2){
            flywheel1.setPower(0.9);
            flywheel2.setPower(0.9);
            hood.setPosition(hoodMaxPos);
        }
        previousGamepad.copy(gamepad1);
    }
}
