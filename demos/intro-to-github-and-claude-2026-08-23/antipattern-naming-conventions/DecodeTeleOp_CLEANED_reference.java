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

/**
 * Driver-controlled TeleOp for the DECODE robot.
 * Tank drive + a camera that shoots artifacts only when we see the
 * correct AprilTag on the goal, plus a servo that loads one ball at a time.
 */
@TeleOp
public class DecodeTeleOp extends LinearOpMode {

    // The AprilTag ID on the goal we're allowed to score in.
    private static final int TARGET_TAG_ID = 20;

    // Servo positions for the ball loader (found by testing on the real robot).
    private static final double LOADER_OPEN = 0.6;   // pushes a ball into the shooter
    private static final double LOADER_HOME = 0.2;   // resting position, ready to reload

    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private DcMotor shooterMotor;
    private Servo loaderServo;

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    // Remembers if the loader is already pushed out, so one press = one ball.
    private boolean loaderIsOpen = false;

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
            // Tank drive: each stick controls one side. Sticks are negated
            // because pushing forward reads as a negative number.
            leftDrive.setPower(-gamepad1.left_stick_y);
            rightDrive.setPower(-gamepad1.right_stick_y);

            boolean seesTargetGoal = isTargetTagVisible();

            // Safety rule: only spin the shooter when the driver holds A
            // AND the camera confirms we're aimed at the right goal.
            if (gamepad1.a && seesTargetGoal) {
                shooterMotor.setPower(1);
            } else {
                shooterMotor.setPower(0);
            }

            // Hold B to load one ball; release to reset for the next one.
            if (gamepad1.b && !loaderIsOpen) {
                loaderServo.setPosition(LOADER_OPEN);
                loaderIsOpen = true;
            }
            if (!gamepad1.b) {
                loaderServo.setPosition(LOADER_HOME);
                loaderIsOpen = false;
            }

            telemetry.addData("Aimed at target goal?", seesTargetGoal);
            telemetry.update();
        }
    }

    /** Returns true if our target AprilTag is currently in view of the camera. */
    private boolean isTargetTagVisible() {
        List<AprilTagDetection> detections = aprilTag.getDetections();
        for (AprilTagDetection tag : detections) {
            if (tag.id == TARGET_TAG_ID) {
                return true;
            }
        }
        return false;
    }
}
