package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * The "Middle Stage" servo that pushes a game piece up into the launcher.
 *
 * feed() pushes, and update() brings the servo back after FEED_SECONDS.
 * The OpMode must call update() every loop.
 */
public class Feeder {

    private static final String SERVO_NAME = "Middle Stage";

    private static final double REST_POSITION = 0.6;
    private static final double FEED_POSITION = 0.85;

    // How long to hold the servo in the feed position.
    private static final double FEED_SECONDS = 2.0;

    private final Servo servo;
    private final ElapsedTime feedTimer = new ElapsedTime();
    private boolean feeding = false;

    public Feeder(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, SERVO_NAME);
        // Don't move the servo here: TeleOp didn't move it during init in Blocks.
        // Autonomous calls rest() itself.
    }

    public void rest() {
        servo.setPosition(REST_POSITION);
        feeding = false;
    }

    /** Push a game piece into the launcher. Pressing again restarts the timer. */
    public void feed() {
        servo.setPosition(FEED_POSITION);
        feedTimer.reset();
        feeding = true;
    }

    /** Call once per loop so the servo returns to rest on time. */
    public void update() {
        if (feeding && feedTimer.seconds() >= FEED_SECONDS) {
            rest();
        }
    }

    public boolean isFeeding() {
        return feeding;
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Feeder", feeding ? "Feeding" : "Rest");
    }
}
