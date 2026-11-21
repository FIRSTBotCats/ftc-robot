package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.localizers.TwoWheelLocalizer;
import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;

import org.firstinspires.ftc.teamcode.pedro.procedures.ForesightTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.MecanumTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.Tests;
import org.firstinspires.ftc.teamcode.pedro.procedures.TwoWheelTuner;

/**
 * The tuning procedures shown on the AutoTune webpage
 * (http://192.168.43.1:10158, or http://localhost:10158 over USB).
 * Each @Tuner method adds one procedure to the page.
 *
 * Run them in this order, pasting each one's generated Java into Constants.java
 * before starting the next:
 *   1. Mecanum     checks the wheel motor directions
 *   2. Two Wheel   measures the odometry pods (push the robot 48 in forward,
 *                  48 in left, then turn it about 180 degrees)
 *   3. Foresight   drives the robot to measure speed and braking. Needs about
 *                  6 feet of clear floor, and the Two Wheel values filled in.
 *   4. Tests       drive around and check the position readings
 */
public class Tuning {

    @Tuner
    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }

    @Tuner
    public static Procedure twoWheelTuner() {
        return new TwoWheelTuner();
    }

    @Tuner
    public static Procedure foresightTuner() {
        return new ForesightTuner(
                hardwareMap -> new TwoWheelLocalizer(hardwareMap, Constants.localizerConfig),
                hardwareMap -> new Mecanum(hardwareMap, Constants.drivetrainConfig));
    }

    // Driving, localization, and path-following tests (Line, Curve, Hold, ...).
    @Tuner
    public static Procedure tests() {
        return new Tests(
                hardwareMap -> new Mecanum(hardwareMap, Constants.drivetrainConfig),
                hardwareMap -> new TwoWheelLocalizer(hardwareMap, Constants.localizerConfig),
                () -> new Foresight(Constants.foresightConfig));
    }
}
