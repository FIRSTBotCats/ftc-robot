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

    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    Servo s1;
    AprilTagProcessor a;
    VisionPortal v;
    boolean b = false;

    @Override
    public void runOpMode() {
        m1 = hardwareMap.get(DcMotor.class, "left");
        m2 = hardwareMap.get(DcMotor.class, "right");
        m3 = hardwareMap.get(DcMotor.class, "shooter");
        s1 = hardwareMap.get(Servo.class, "loader");

        a = AprilTagProcessor.easyCreateWithDefaults();
        v = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), a);

        waitForStart();

        while (opModeIsActive()) {
            m1.setPower(-gamepad1.left_stick_y);
            m2.setPower(-gamepad1.right_stick_y);

            List<AprilTagDetection> list1 = a.getDetections();
            int x = 0;
            for (AprilTagDetection d : list1) {
                if (d.id == 20) {
                    x = 1;
                }
            }

            if (gamepad1.a && x == 1) {
                m3.setPower(1);
            } else {
                m3.setPower(0);
            }

            if (gamepad1.b && !b) {
                s1.setPosition(0.6);
                b = true;
            }
            if (!gamepad1.b) {
                s1.setPosition(0.2);
                b = false;
            }

            telemetry.addData("x", x);
            telemetry.update();
        }
    }
}
