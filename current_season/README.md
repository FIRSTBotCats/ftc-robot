# Current season code (OnBotJava)

This is the Java version of our competition code. It started as a port of the
2026-11-15 qualifiers Blocks OpModes
(`past_seasons/2026-2027/competition/`).

## How the code is organized

```
onbotjava/org/firstinspires/ftc/teamcode/
├── CompetitionTeleOp.java     driver controls: which button does what
├── CompetitionAuto.java       autonomous routine
└── subsystems/                one class per mechanism on the robot
    ├── MecanumDrive.java      the four drive wheels
    ├── Launcher.java          the flywheel ("Outake Motor")
    ├── Feeder.java            the "Middle Stage" servo that feeds the launcher
    └── Intake.java            intake motor + two intake servos
```

**Subsystems** each own one mechanism. A subsystem:

- finds its motors and servos by name, and sets them up, in its constructor
- has methods named for what the mechanism does (`launcher.spinUp()`,
  `intake.toggleEject()`, `feeder.feed()`)
- keeps its numbers (speeds, positions, times) as named constants at the top
  of the file, so tuning only ever touches that one file
- never reads the gamepad

**OpModes** (TeleOp and Autonomous) are thin. They create the subsystems they
need, then connect gamepad buttons, or an autonomous script, to subsystem
methods. Many OpModes can share the same subsystems.

### Why split it up this way

- **People can work in parallel.** The launcher tuner edits `Launcher.java`,
  the drive team edits `MecanumDrive.java`, and their branches don't collide.
- **Auto and TeleOp share code.** Fix the drive once and both get the fix.
- **Each mechanism can be tested on its own.** You can write a small test
  OpMode that only creates one subsystem.

## Adding a new mechanism (camera, odometry pods, ...)

1. Add the device to the robot configuration on the Driver Station.
2. Create `subsystems/YourThing.java`. Copy the pattern from `Intake.java`:
   names and constants at the top, `hardwareMap.get(...)` in the constructor,
   action methods, and `addTelemetry(...)`.
3. Write a small test OpMode for it (for example `YourThingTest.java`) and
   get it working on its own first.
4. Only then use it from `CompetitionTeleOp` / `CompetitionAuto`.

## ⚠️ One broken file breaks ALL Java OpModes

OnBotJava compiles every `.java` file on the robot together. If any one file
has an error, **none** of the Java OpModes show up on the Driver Station.
(Blocks OpModes keep working.) This happened on 2026-11-14: a half-finished
file on the robot broke the whole build.

So:

- Click **Build Everything** in OnBotJava after every change and fix errors
  right away.
- Don't leave experimental or half-written files on the robot before a match.
- Keep the Blocks version of the competition code on the robot as a backup
  until the Java version has been tested.

## Getting this code onto the robot

The files must keep the same folders on the robot. In OnBotJava they go under
`org/firstinspires/ftc/teamcode/`, with the subsystem files in a
`subsystems` folder. After uploading, click **Build Everything**.
