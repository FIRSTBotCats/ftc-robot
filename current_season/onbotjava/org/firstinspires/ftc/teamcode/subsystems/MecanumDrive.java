package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * The four mecanum wheels.
 *
 * This class only knows how to move the robot. It does NOT read the gamepad;
 * the OpMode decides what forward/strafe/turn should be and passes them in.
 * That way TeleOp and Autonomous can share the exact same drive code.
 */
public class MecanumDrive {

    // Names must match the active robot configuration exactly
    // ("2026-2027 Java Bot Config"). The Blocks code uses the older
    // "2026-2027 Gobilda Mecanum Starter Bot" config, which has different names.
    // "WHEEL" is in each name so they aren't confused with the odometry pods,
    // which also have positional names like "Back Left Odometry Pod".
    private static final String FRONT_LEFT_WHEEL_NAME = "Front Left Wheel";
    private static final String FRONT_RIGHT_WHEEL_NAME = "Front Right Wheel";
    private static final String BACK_LEFT_WHEEL_NAME = "Back Left Wheel";
    private static final String BACK_RIGHT_WHEEL_NAME = "Back Right Wheel";

    // Slow mode divides every wheel power by this, for lining up shots.
    private static final double SLOW_MODE_DIVISOR = 2.0;

    private final DcMotorEx frontLeftWheel;
    private final DcMotorEx frontRightWheel;
    private final DcMotorEx backLeftWheel;
    private final DcMotorEx backRightWheel;

    private boolean slowMode = false;

    public MecanumDrive(HardwareMap hardwareMap) {
        frontLeftWheel = hardwareMap.get(DcMotorEx.class, FRONT_LEFT_WHEEL_NAME);
        frontRightWheel = hardwareMap.get(DcMotorEx.class, FRONT_RIGHT_WHEEL_NAME);
        backLeftWheel = hardwareMap.get(DcMotorEx.class, BACK_LEFT_WHEEL_NAME);
        backRightWheel = hardwareMap.get(DcMotorEx.class, BACK_RIGHT_WHEEL_NAME);

        // The left motors are mounted facing the other way, so reverse them
        // to make positive power drive every wheel forward.
        frontLeftWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightWheel.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightWheel.setDirection(DcMotorSimple.Direction.FORWARD);

        for (DcMotorEx wheel : new DcMotorEx[] {frontLeftWheel, frontRightWheel, backLeftWheel, backRightWheel}) {
            wheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // BRAKE stops the robot quickly instead of coasting when the stick is released.
            wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // TODO: The Blocks code tuned ONLY the back-left wheel's PIDF. It was
        // probably meant for the launcher motor. It's copied here so this port
        // drives the same as Blocks. Test removing it and check whether the
        // robot still drives straight.
        backLeftWheel.setVelocityPIDFCoefficients(10, 0, 0, 13.5);
    }

    /**
     * Drive the robot. Each value is from -1 to 1.
     *
     * @param forward positive = forward
     * @param strafe  positive = right
     * @param turn    positive = turn clockwise
     */
    public void drive(double forward, double strafe, double turn) {
        // If the three inputs add up to more than 1, scale them all down
        // together so the robot keeps the direction the driver asked for.
        double denominator = Math.max(1.0, Math.abs(forward) + Math.abs(strafe) + Math.abs(turn));
        double speedDivisor = slowMode ? SLOW_MODE_DIVISOR : 1.0;
        double scale = denominator * speedDivisor;

        frontLeftWheel.setPower((forward + strafe + turn) / scale);
        frontRightWheel.setPower((forward - strafe - turn) / scale);
        backLeftWheel.setPower((forward - strafe + turn) / scale);
        backRightWheel.setPower((forward + strafe - turn) / scale);
    }

    public void stop() {
        drive(0, 0, 0);
    }

    public void toggleSlowMode() {
        slowMode = !slowMode;
    }

    public boolean isSlowMode() {
        return slowMode;
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Drive", slowMode ? "SLOW" : "full speed");
    }
}
