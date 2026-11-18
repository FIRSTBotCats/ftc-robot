package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * The intake motor plus the two continuous-rotation servos that pull game
 * pieces into the robot. All three always run together.
 */
public class Intake {

    private static final String MOTOR_NAME = "Intake Motor";
    private static final String LEFT_SERVO_NAME = "Left Intake Servo";
    private static final String RIGHT_SERVO_NAME = "Right Intake Servo";

    private static final double INTAKE_POWER = 1.0;
    private static final double EJECT_POWER = -1.0;

    public enum State { OFF, INTAKING, EJECTING }

    private final DcMotor motor;
    private final CRServo leftServo;
    private final CRServo rightServo;
    private State state = State.OFF;

    public Intake(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, MOTOR_NAME);
        leftServo = hardwareMap.get(CRServo.class, LEFT_SERVO_NAME);
        rightServo = hardwareMap.get(CRServo.class, RIGHT_SERVO_NAME);

        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftServo.setDirection(DcMotorSimple.Direction.FORWARD);
        rightServo.setDirection(DcMotorSimple.Direction.FORWARD);

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // FLOAT lets the heavy flywheel coast down instead of slamming to a stop.
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    private void setAllPower(double power) {
        motor.setPower(power);
        leftServo.setPower(power);
        rightServo.setPower(power);
    }

    public void intake() {
        setAllPower(INTAKE_POWER);
        state = State.INTAKING;
    }

    public void eject() {
        setAllPower(EJECT_POWER);
        state = State.EJECTING;
    }

    public void stop() {
        setAllPower(0);
        state = State.OFF;
    }

    public State getState() {
        return state;
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Intake", state);
    }
}
