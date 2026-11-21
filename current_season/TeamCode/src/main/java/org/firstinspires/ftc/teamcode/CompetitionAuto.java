package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;

/**
 * Java port of the 2026-11-15 qualifiers Blocks Autonomous:
 * drive forward a little, strafe left, then stop.
 *
 * When this ends, the Driver Station automatically selects CompetitionTeleOp.
 */
@Autonomous(name = "Qualifiers Auto (Java)", group = "Competition",
        preselectTeleOp = CompetitionTeleOp.NAME)
public class CompetitionAuto extends LinearOpMode {

    private static final double AUTO_SPEED = 0.25;

    private MecanumDrive drive;

    @Override
    public void runOpMode() {
        drive = new MecanumDrive(hardwareMap);
        Feeder feeder = new Feeder(hardwareMap);
        feeder.rest();

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();
        if (!opModeIsActive()) {
            return;
        }

        driveFor(AUTO_SPEED, 0, 0, 0.25);  // forward
        driveFor(0, -AUTO_SPEED, 0, 0.8);  // strafe left
        drive.stop();

        // Hold still until the period ends so the robot can't drift.
        while (opModeIsActive()) {
            idle();
        }
    }

    /** Drive at the given powers for a fixed time. */
    private void driveFor(double forward, double strafe, double turn, double seconds) {
        drive.drive(forward, strafe, turn);
        sleep((long) (seconds * 1000));
    }
}
