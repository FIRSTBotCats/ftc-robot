# Current season code (Android Studio)

This folder is our robot code for the 2026-2027 season. It started as a port of
the 2026-11-15 qualifiers Blocks OpModes (`past_seasons/2026-2027/competition/`).
An **OpMode** is one program the robot can run, picked on the Driver Station:
**TeleOp** OpModes are driver-controlled, and **Autonomous** OpModes run on their own.

**Terms used in this file:**

- **Android Studio:** the program we use to write and build the robot code on a
  laptop. (Before 2026-11-20 we used **OnBotJava**, the robot's built-in web
  editor. We switched because Pedro Pathing only works with Android Studio.)
- **Pedro Pathing:** a library (ready-made code from another team) that drives
  the robot along smooth paths in Autonomous. Its setup is in `pedro/`.
- **Gradle:** the build tool Android Studio uses to turn our code into an app.
- **APK:** the app file Gradle builds. Installing it replaces the robot's
  Robot Controller app with one that contains our code.
- **AutoTune:** a webpage the robot serves that measures the robot for Pedro
  Pathing and writes the values for `pedro/Constants.java`.

## How the code is organized

This folder is the FTC SDK project (FTC's code that runs the robot), taken from
Pedro Pathing's "Quickstart" with our code added. We only edit `TeamCode/`.

```
current_season/
├── FtcRobotController/        FTC's code. Don't edit.
├── TeamCode/src/main/java/org/firstinspires/ftc/teamcode/
│   ├── CompetitionTeleOp.java     driver controls: which button does what
│   ├── CompetitionAuto.java       autonomous routine
│   ├── OdometryPodTest.java       checks the odometry pods and IMU wiring
│   ├── subsystems/                one class per mechanism on the robot
│   │   ├── MecanumDrive.java      the four drive wheels
│   │   ├── Launcher.java          the flywheel ("Launcher Motor")
│   │   ├── Feeder.java            the "Middle Stage" servo that feeds the launcher
│   │   └── Intake.java            intake motor + two intake servos
│   └── pedro/                     Pedro Pathing setup
│       ├── Constants.java         our robot's measurements, from AutoTune
│       ├── Tuning.java            which AutoTune procedures appear, and in what order
│       └── procedures/            Pedro's tuning code. Don't edit.
└── build.gradle, gradlew, ...     Gradle build files. Don't edit without asking.
```

**Subsystems** each own one mechanism. A subsystem:

- finds its motors and servos by name, and sets them up, in its constructor
- has methods named for what the mechanism does (`launcher.spinUp()`,
  `intake.eject()`, `feeder.feed()`)
- keeps its numbers (speeds, positions, times) as named constants at the top
  of the file, so tuning only ever touches that one file
- never reads the gamepad

**OpModes** are thin. They create the subsystems they need, then connect
gamepad buttons, or an autonomous script, to subsystem methods. Many OpModes
can share the same subsystems.

### Why split it up this way

- **People can work in parallel.** The launcher tuner edits `Launcher.java`,
  the drive team edits `MecanumDrive.java`, and their branches don't collide.
- **Auto and TeleOp share code.** Fix the drive once and both get the fix.
- **Each mechanism can be tested on its own.** You can write a small test
  OpMode that only creates one subsystem.

## One-time setup on your laptop

1. Install **Android Studio** (Mac or Windows) from developer.android.com.
2. Open it once and let it finish installing the Android SDK (the Android
   tools it needs).
3. In Android Studio, **File → Open**, and pick this `current_season` folder,
   not `botcats`. Wait for the "Gradle sync" at the bottom to finish. The first
   time it downloads a lot, so it can take several minutes.

## Building and putting the code on the robot

1. Turn the robot on and plug the Control Hub into your laptop with the USB-C
   cable (see `docs/robot-networking.md`).
2. In Android Studio, pick **TeamCode** in the run menu at the top, then press
   the green **Run ▶** button. Android Studio builds the APK and installs it on
   the robot.
3. On the Driver Station, make sure **"2026-2027 Java Bot Config"** is the
   active robot configuration, then pick your OpMode.

If the code has an error, the build fails on your laptop and **nothing goes to
the robot**, so a broken file can no longer break the robot's OpModes. Fix the
errors Android Studio shows, then press Run again.

**Don't put Java files in OnBotJava anymore.** Their class names would clash
with the same classes in our app. Blocks OpModes still work.

## Tuning Pedro Pathing (AutoTune)

`pedro/Constants.java` must hold measurements of *our* robot before Pedro can
drive it. AutoTune takes those measurements:

1. Put the latest code on the robot (above).
2. From the `botcats` folder, run `./scripts/connect-robot.sh --autotune`.
   It opens the AutoTune page at `http://localhost:10158`. (On the robot's
   WiFi, the address is `http://192.168.43.1:10158`.)
3. Run the procedures in the order listed in `pedro/Tuning.java`. After each
   one, copy the Java it generates into `pedro/Constants.java`.

Pedro measures distance in **inches** and angles in **radians**, with (0, 0) at
the field's bottom-left corner. See `docs/robot-hardware.md`.

## Adding a new mechanism (camera, sensor, ...)

1. Add the device to the robot configuration on the Driver Station.
2. Create `subsystems/YourThing.java`. Copy the pattern from `Intake.java`:
   names and constants at the top, `hardwareMap.get(...)` in the constructor,
   action methods, and `addTelemetry(...)`.
3. Write a small test OpMode for it (for example `YourThingTest.java`) and
   get it working on its own first.
4. Only then use it from `CompetitionTeleOp` / `CompetitionAuto`.
