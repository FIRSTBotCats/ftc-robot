package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;

/**
 * Checks that both odometry pods and the IMU are wired and configured.
 *
 * Needs the "2026-2027 Java Bot Config" robot configuration.
 *
 * How to test:
 *   1. Drive slowly FORWARD with the left stick. The LEFT pod (forward-facing)
 *      count should change; the RIGHT pod (sideways) should barely move.
 *   2. Drive slowly SIDEWAYS (strafe). Now the RIGHT pod should change and
 *      the LEFT pod should barely move.
 *   3. Turn with the right stick. The heading should change. Turning left
 *      (counter-clockwise) should make the heading go UP.
 *   Write down whether each count goes up (+) or down (-) for forward and
 *   right. We'll need that to write the real odometry code.
 *
 * Buttons:
 *   X   reset both pod counts to 0
 *   Y   reset heading to 0
 */
@TeleOp(name = "Odometry Pod Test", group = "Test")
public class OdometryPodTest extends LinearOpMode {

    // Names must match "2026-2027 Java Bot Config".
    private static final String LEFT_POD_NAME = "Back Left Odometry Pod";    // forward-facing, Control Hub port 3
    private static final String RIGHT_POD_NAME = "Back Right Odometry Pod";  // sideways, Expansion Hub port 3
    private static final String IMU_NAME = "imu";

    // TODO: Set these to match how the Control Hub is mounted on the robot.
    // LOGO = which way the REV logo on the hub faces; USB = which way its USB ports face.
    // If the hub lies flat with the logo up, only USB_DIRECTION matters for heading.
    // If PITCH or ROLL below are far from 0 while the robot sits flat, these are wrong.
    private static final RevHubOrientationOnRobot.LogoFacingDirection LOGO_DIRECTION =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
    private static final RevHubOrientationOnRobot.UsbFacingDirection USB_DIRECTION =
            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

    // Drive gently so the pod counts are easy to watch.
    private static final double DRIVE_SPEED = 0.3;

    private DcMotor leftPod;
    private DcMotor rightPod;

    @Override
    public void runOpMode() {
        // The pods plug into motor ENCODER ports, so the SDK reads them as
        // "motors". We only ever read their counts, never set power: a real
        // motor could be plugged into the power side of those ports.
        leftPod = hardwareMap.get(DcMotor.class, LEFT_POD_NAME);
        rightPod = hardwareMap.get(DcMotor.class, RIGHT_POD_NAME);
        resetPods();

        IMU imu = hardwareMap.get(IMU.class, IMU_NAME);
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(LOGO_DIRECTION, USB_DIRECTION)));
        imu.resetYaw();

        MecanumDrive drive = new MecanumDrive(hardwareMap);

        telemetry.addLine("Ready. Press START, then drive slowly and watch the counts.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double forward = -gamepad1.left_stick_y * DRIVE_SPEED;
            double strafe = gamepad1.left_stick_x * DRIVE_SPEED;
            double turn = gamepad1.right_stick_x * DRIVE_SPEED;
            drive.drive(forward, strafe, turn);

            if (gamepad1.xWasPressed()) {
                resetPods();
            }
            if (gamepad1.yWasPressed()) {
                imu.resetYaw();
            }

            YawPitchRollAngles angles = imu.getRobotYawPitchRollAngles();

            telemetry.addData("Left pod (forward)", leftPod.getCurrentPosition());
            telemetry.addData("Right pod (sideways)", rightPod.getCurrentPosition());
            telemetry.addData("Heading", "%.1f deg", angles.getYaw(AngleUnit.DEGREES));
            telemetry.addData("Pitch / Roll", "%.1f deg / %.1f deg (should be near 0)",
                    angles.getPitch(AngleUnit.DEGREES), angles.getRoll(AngleUnit.DEGREES));
            telemetry.addLine();
            telemetry.addLine("X = reset pods, Y = reset heading");
            telemetry.update();
        }

        drive.stop();
    }

    private void resetPods() {
        leftPod.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightPod.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Leave reset mode so the counts start updating again.
        leftPod.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightPod.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
