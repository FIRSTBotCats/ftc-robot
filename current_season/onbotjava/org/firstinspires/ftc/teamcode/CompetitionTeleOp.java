package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;

/**
 * Java port of the 2026-11-15 qualifiers Blocks TeleOp.
 *
 * This file is only the "driver controls": it maps gamepad buttons to the
 * subsystems. The details of how each mechanism works are in its own
 * class in the subsystems folder.
 *
 * Controls (gamepad 1):
 *   Left stick       drive forward/back and strafe
 *   Right stick X    turn
 *   D-pad            slow nudge (half speed) forward/back/left/right
 *   Right bumper     toggle slow mode
 *   Right trigger    launcher on/off
 *   A                launcher reverse on/off (clear a jam)
 *   B                feed one game piece into the launcher
 *   Left trigger     intake on/off
 *   Left bumper      intake reverse (eject) on/off
 */
@TeleOp(name = CompetitionTeleOp.NAME, group = "Competition")
public class CompetitionTeleOp extends LinearOpMode {

    // CompetitionAuto uses this name to start this TeleOp automatically when auto ends.
    public static final String NAME = "Qualifiers TeleOp (Java)";

    // D-pad driving speed, for small precise moves.
    private static final double DPAD_SPEED = 0.5;

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap);
        Launcher launcher = new Launcher(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        Feeder feeder = new Feeder(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();
        if (!opModeIsActive()) {
            return;
        }

        // Start the flywheel right away so it's at speed for the first shot.
        launcher.spinUp();

        while (opModeIsActive()) {
            // ---- Driving ----
            double forward = -gamepad1.left_stick_y; // pushing the stick forward gives a negative y
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            if (gamepad1.dpad_up) {
                forward = DPAD_SPEED;
            }
            if (gamepad1.dpad_down) {
                forward = -DPAD_SPEED;
            }
            if (gamepad1.dpad_left) {
                strafe = -DPAD_SPEED;
            }
            if (gamepad1.dpad_right) {
                strafe = DPAD_SPEED;
            }

            if (gamepad1.rightBumperWasPressed()) {
                drive.toggleSlowMode();
            }
            drive.drive(forward, strafe, turn);

            // ---- Launcher ----
            if (gamepad1.rightTriggerWasPressed()) {
                launcher.toggleSpin();
            }
            if (gamepad1.aWasPressed()) {
                launcher.toggleReverse();
            }

            // ---- Feeder ----
            if (gamepad1.bWasPressed()) {
                feeder.feed();
            }
            feeder.update();

            // ---- Intake ----
            if (gamepad1.leftTriggerWasPressed()) {
                intake.toggleIntake();
            }
            if (gamepad1.leftBumperWasPressed()) {
                intake.toggleEject();
            }

            // ---- Driver Station display ----
            drive.addTelemetry(telemetry);
            launcher.addTelemetry(telemetry);
            feeder.addTelemetry(telemetry);
            intake.addTelemetry(telemetry);
            telemetry.update();
        }
    }
}
