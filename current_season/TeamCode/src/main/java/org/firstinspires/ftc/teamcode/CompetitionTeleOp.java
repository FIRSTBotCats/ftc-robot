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
 *   Right trigger    launcher: stop / spin, alternating
 *   A                launcher: stop / full reverse, alternating
 *   B                feed one game piece into the launcher
 *   Left trigger     intake: run / stop, alternating
 *   Left bumper      intake: eject / stop, alternating
 *
 * Every toggle button keeps its OWN on/off memory, exactly like the Blocks
 * version, so each button does what the driver is used to. For example,
 * pressing A always alternates between "stop" and "reverse", no matter what
 * the right trigger did in between.
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

        // Each toggle button's own memory. These match the Blocks variables
        // named in the comments, including their starting values.
        boolean spinButtonOn = true;     // Blocks "RightTrigger": starts on, because the launcher spins up at start
        boolean intakeButtonOn = false;  // Blocks "LeftTrigger"
        boolean ejectButtonOn = false;   // Blocks "LeftBumper"
        // Blocks never gave "A" a starting value, so the FIRST press of A took
        // the "turn off" branch and stopped the launcher. Starting this as true
        // keeps that behavior: the first A press stops, the second reverses.
        boolean reverseButtonOn = true;  // Blocks "A"

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
                spinButtonOn = !spinButtonOn;
                if (spinButtonOn) {
                    launcher.spinUp();
                } else {
                    launcher.stop();
                }
            }
            if (gamepad1.aWasPressed()) {
                reverseButtonOn = !reverseButtonOn;
                if (reverseButtonOn) {
                    launcher.reverse();
                } else {
                    launcher.stop();
                }
            }

            // ---- Feeder ----
            if (gamepad1.bWasPressed()) {
                feeder.feed();
            }
            feeder.update();

            // ---- Intake ----
            if (gamepad1.leftTriggerWasPressed()) {
                intakeButtonOn = !intakeButtonOn;
                if (intakeButtonOn) {
                    intake.intake();
                } else {
                    intake.stop();
                }
            }
            if (gamepad1.leftBumperWasPressed()) {
                ejectButtonOn = !ejectButtonOn;
                if (ejectButtonOn) {
                    intake.eject();
                } else {
                    intake.stop();
                }
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
