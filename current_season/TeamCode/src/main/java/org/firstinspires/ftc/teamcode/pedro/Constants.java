package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.RevHubIMU;
import com.pedropathing.revhub.localizers.TwoWheelConfig;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Robot measurements for Pedro Pathing, the path-following library we use
 * for Autonomous.
 *
 * Terms:
 *   AutoTune: a webpage the robot serves (http://192.168.43.1:10158, or
 *     http://localhost:10158 over USB) that measures the robot and writes
 *     the values for this file. See current_season/README.md.
 *   Localizer: the code that works out where the robot is on the field, here
 *     from the two odometry pods plus the Control Hub's IMU (its built-in gyro).
 *   Foresight: Pedro's path-following algorithm. Its settings describe how fast
 *     the robot can move and how it brakes.
 *   Follower: the object Autonomous code uses to drive along paths.
 *
 * Pedro measures distance in INCHES and angles in RADIANS. Pedro has no
 * built-in default for these values, so anything marked "TODO: from AutoTune"
 * must be filled in before Pedro can drive the robot.
 *
 * Device names must match "2026-2027 Java Bot Config" (see docs/robot-hardware.md).
 */
public class Constants {

    // The four drive wheels. Same names and directions as subsystems/MecanumDrive.java;
    // AutoTune's "Mecanum" procedure confirmed the directions on 2026-11-20.
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("Front Left Wheel");
        c.backLeftName.set("Back Left Wheel");
        c.frontRightName.set("Front Right Wheel");
        c.backRightName.set("Back Right Wheel");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
        // Hold position when the driver lets go of the sticks (matches our TeleOp's BRAKE).
        c.manualBrakeMode.set(true);
    });

    // Two odometry pods + the Control Hub's IMU (Pedro's "Two Wheel" localizer).
    // The x pod measures forward/back; the y pod measures strafing.
    public static TwoWheelConfig localizerConfig = new TwoWheelConfig(c -> {
        c.xPodName.set("Back Left Odometry Pod");   // forward-facing, Control Hub encoder port 3
        c.yPodName.set("Back Right Odometry Pod");  // sideways, Expansion Hub encoder port 3
        c.imuName.set("imu");
        // The Control Hub is mounted on its side: REV logo facing LEFT, USB ports facing UP.
        c.imu.set(new RevHubIMU(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP)));

        // Measured by AutoTune's "Two Wheel" procedure on 2026-11-20.
        // Inches per encoder count. About 506 and 499 counts per inch, which
        // matches goBILDA's 4-Bar odometry pods (spec: about 505).
        c.forwardTicksToInches.set(0.001977436636992014);
        c.strafeTicksToInches.set(0.002003292244660016);
        // Where each pod is, in inches from the robot's center of rotation:
        // the forward pod is 6.2 in to the LEFT, the sideways pod 6.9 in BEHIND.
        c.xPodOffset.set(6.222045492441513);
        c.yPodOffset.set(-6.879513718753594);
        // Both FORWARD in Pedro's coordinates, where +y points LEFT. (OdometryPodTest
        // saw the sideways pod count down moving right; that's the same thing.)
        c.xPodDirection.set(Encoder.FORWARD);
        c.yPodDirection.set(Encoder.FORWARD);
    });

    // TODO: from AutoTune ("Foresight" procedure, run AFTER the Two Wheel values
    // above are filled in): paste the generated ForesightConfig here, then add:
    //
    //   public static Follower create(HardwareMap hardwareMap) {
    //       return new Follower(
    //               new TwoWheelLocalizer(hardwareMap, localizerConfig),
    //               new Mecanum(hardwareMap, drivetrainConfig),
    //               new Foresight(foresightConfig));
    //   }
}
