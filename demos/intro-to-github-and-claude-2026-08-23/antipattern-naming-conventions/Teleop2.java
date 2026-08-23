package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;

@TeleOp
public class Teleop2 extends LinearOpMode {

    // AprilTag ID on the goal — the shooter only fires when the camera sees this tag.
    static final int GOAL_TAG_ID = 20;

    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor shooterMotor;
    Servo loaderServo;
    AprilTagProcessor aprilTag;
    VisionPortal visionPortal;
    boolean loaderOpen = false;

    @Override
    public void runOpMode() {
        leftDrive = hardwareMap.get(DcMotor.class, "left");
        rightDrive = hardwareMap.get(DcMotor.class, "right");
        shooterMotor = hardwareMap.get(DcMotor.class, "shooter");
        loaderServo = hardwareMap.get(Servo.class, "loader");

        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), aprilTag);

        waitForStart();

        while (opModeIsActive()) {
            leftDrive.setPower(-gamepad1.left_stick_y);
            rightDrive.setPower(-gamepad1.right_stick_y);

            List<AprilTagDetection> detections = aprilTag.getDetections();
            boolean goalTagVisible = false;
            for (AprilTagDetection detection : detections) {
                if (detection.id == GOAL_TAG_ID) {
                    goalTagVisible = true;
                }
            }

            if (gamepad1.a && goalTagVisible) {
                shooterMotor.setPower(1);
            } else {
                shooterMotor.setPower(0);
            }

            if (gamepad1.b && !loaderOpen) {
                loaderServo.setPosition(0.6);
                loaderOpen = true;
            }
            if (!gamepad1.b) {
                loaderServo.setPosition(0.2);
                loaderOpen = false;
            }

            telemetry.addData("goalTagVisible", goalTagVisible);
            telemetry.update();
        }
    }
}
