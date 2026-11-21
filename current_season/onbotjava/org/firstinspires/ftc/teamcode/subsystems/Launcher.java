package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * The flywheel that shoots game pieces.
 */
public class Launcher {

    // Must match "2026-2027 Java Bot Config". (The old Blocks config calls this
    // motor "Outake Motor".)
    private static final String MOTOR_NAME = "Launcher Motor";

    // Flywheel speed in encoder ticks per second. Tuned at qualifiers.
    public static final double SHOOTING_VELOCITY = 1040;

    // Full power backwards, for clearing a jammed game piece.
    private static final double REVERSE_POWER = -1.0;

    public enum State { STOPPED, SPINNING, REVERSING }

    private final DcMotorEx motor;
    private State state = State.STOPPED;

    public Launcher(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, MOTOR_NAME);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // FLOAT lets the heavy flywheel coast down instead of slamming to a stop.
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void spinUp() {
        motor.setVelocity(SHOOTING_VELOCITY);
        state = State.SPINNING;
    }

    public void reverse() {
        motor.setPower(REVERSE_POWER);
        state = State.REVERSING;
    }

    public void stop() {
        motor.setPower(0);
        state = State.STOPPED;
    }

    public State getState() {
        return state;
    }

    public double getVelocity() {
        return motor.getVelocity();
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Launcher", state);
        telemetry.addData("Velocity", "%.0f / %.0f", getVelocity(), SHOOTING_VELOCITY);
    }
}
