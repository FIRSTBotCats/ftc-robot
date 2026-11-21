# Robot Hardware Notes

Facts about how our robot is wired and configured that the code depends on.
Keep this up to date when the wiring or robot configuration changes.

**Terms used in this file:**

- **Control Hub / Expansion Hub:** the robot's two REV electronics boxes. The
  Control Hub is the robot's computer; the Expansion Hub adds more motor,
  servo and sensor ports.
- **Driver Station:** the handheld device the drivers use to pick and run
  programs, with the gamepads plugged into it.
- **Robot configuration ("config"):** a list saved on the Control Hub that gives
  each device a name, like "Front Left Wheel" on Control Hub motor port 0. Code
  finds devices by these names.
- **OpMode:** one program the robot can run, chosen on the Driver Station.
  **TeleOp** OpModes are driver-controlled; **Autonomous** OpModes run on
  their own. **Init** is the button that loads an OpMode before pressing Start.
- **Blocks / OnBotJava:** the two ways to program the robot in a web browser,
  with drag-and-drop blocks or with Java code. Our Java code is now built in
  **Android Studio** instead (see `current_season/README.md`).
- **Encoder:** a sensor that counts how far a shaft has turned.
- **Odometry pod:** a small unpowered wheel with an encoder that rolls along
  the floor, so the code can measure how far the robot has moved.
- **IMU (Inertial Measurement Unit):** the gyroscope and accelerometer built
  into the Control Hub. It measures which way the robot is facing (heading).
- **I2C (Inter-Integrated Circuit):** the communication method many sensors
  use. Several I2C devices can share one connection, called a **bus**.

## Robot configurations

There are two configurations on the Control Hub. Pick the active one on the
Driver Station (**Configure Robot**). The active config's name is shown at the
top of the Driver Station's main screen.

| Config | Used by | Wheel names | Launcher name |
|---|---|---|---|
| **2026-2027 Java Bot Config** | Java code in `current_season/` (Android Studio) | `Front Left Wheel`, `Back Left Wheel`, `Front Right Wheel`, `Back Right Wheel` | `Launcher Motor` |
| 2026-2027 Gobilda Mecanum Starter Bot | Blocks OpModes from the 2026-11-15 qualifiers | `Front Left`, `Back Left`, `Front Right`, `Back Right` | `Outake Motor` |

If the wrong config is active, the OpMode fails at **Init** with a "can't find
device" error.

## 2026-2027 Java Bot Config

| Hub | Motor/encoder port 0 | Port 1 | Port 2 | Port 3 |
|---|---|---|---|---|
| Control Hub | Front Left Wheel | Back Left Wheel | Launcher Motor | Back Left Odometry Pod |
| Expansion Hub 2 | Front Right Wheel | Back Right Wheel | Intake Motor | Back Right Odometry Pod |

Other devices:

- **Control Hub servos:** 0 = Left Intake Servo (continuous rotation), 1 = Middle Stage (feeder)
- **Expansion Hub servos:** 0 = Right Intake Servo (continuous rotation)
- **Control Hub I2C bus 0:** `imu`, the Control Hub's built-in IMU (BHI260AP chip)
- **Control Hub I2C bus 1:** `Range Sensor` (Modern Robotics, or "MR", Range Sensor)
- **USB:** `Logitech Cam` webcam

The **Control Hub is mounted on its side**: the REV logo faces the robot's
**left** and the USB ports face **up**. Code that uses the IMU must be told
this (see `LOGO_DIRECTION` / `USB_DIRECTION` in `OdometryPodTest.java`), or
the heading will be wrong.

## Odometry pods (installed 2026-11-19)

Two goBILDA **4-Bar** odometry pods near the back wheels (about 505 encoder counts per inch):

| Pod | Plugged into | Faces | Count direction |
|---|---|---|---|
| Back Left Odometry Pod | Control Hub **encoder** port 3 | Forward (measures forward/back) | Goes **up** driving forward |
| Back Right Odometry Pod | Expansion Hub **encoder** port 3 | Sideways (measures strafing) | Goes **down** strafing right (= **up** strafing left, which Pedro treats as FORWARD) |

- The pods use **motor encoder ports**, not the digital, analog or I2C ports.
  The code reads them as if they were motors: `hardwareMap.get(DcMotor.class, ...)`
  then `getCurrentPosition()`. Never set power on them; a real motor could be
  on the power side of that port.
- Port 3 was chosen because ports 0 and 3 are the fast-counting encoder ports,
  and every other encoder port is used by a motor that needs its encoder.
- Turning the robot also moves the pods, because they're not at the robot's
  center. The odometry code has to subtract that out, using each pod's
  distance from the center. Pedro Pathing's AutoTune measures these
  distances ("pod offsets") for us.
- The pods and the IMU together are what Pedro Pathing, the path-following
  library we use for Autonomous, needs to track the robot's position.
- `OdometryPodTest` shows both counts and the IMU heading, for checking the
  wiring.

## Pedro Pathing

Pedro Pathing drives the robot along paths in Autonomous. Its settings for our
robot are in `current_season/TeamCode/.../pedro/Constants.java`:

- **Drivetrain:** the four wheel names and directions (same as `MecanumDrive.java`).
- **Localizer** (Pedro's code for tracking where the robot is): Pedro's "Two Wheel"
  localizer, using the forward pod as the **x pod**, the sideways pod as the
  **y pod**, and the `imu` with the hub's LEFT/UP mounting.
- **Measured by AutoTune (2026-11-20):** pod ticks-per-inch and pod offsets (forward pod
  6.2 in left of center, sideways pod 6.9 in behind). Still to do: the Foresight
  (path-following) speed and braking values. See `current_season/README.md`.

**Pedro's field coordinates:** distances in **inches**, with (0, 0) at the
field's bottom-left corner and the field 144 × 144 in. **x** increases to the
right and **y** increases up the field. Heading is in **radians**: 0 faces
right, and counterclockwise is positive (π/2 = facing up the field). This is
different from the FTC SDK's own coordinates, which put (0, 0) at the center.

## ⚠️ Keep I2C bus 0 for the built-in IMU only

The Control Hub's built-in IMU shares **I2C bus 0** with whatever is plugged
into **I2C port 0**. On 2026-11-19 the MR Range Sensor on I2C port 0 blocked
the whole bus. The IMU stopped responding ("Error communicating with I2C
device(s) 'imu'") and read 0 for heading, pitch and roll. Unplugging the range
sensor from port 0 fixed the IMU. Moving the sensor to **I2C port 1 / bus 1**
made both work.

**Rule:** don't plug external I2C sensors into I2C port 0. Use ports 1–3, and
put each one on the matching bus number in the robot configuration.
