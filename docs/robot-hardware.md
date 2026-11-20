# Robot Hardware Notes

Facts about how our robot is wired and configured that the code depends on.
Keep this up to date when the wiring or robot configuration changes.

## Robot configurations

There are two configurations on the Control Hub. Pick the active one on the
Driver Station (**Configure Robot**). The active config's name is shown at the
top of the main screen.

| Config | Used by | Wheel names |
|---|---|---|
| **2026-2027 Java Bot Config** | OnBotJava code in `current_season/` | `Front Left Wheel`, `Back Left Wheel`, `Front Right Wheel`, `Back Right Wheel` |
| 2026-2027 Gobuilda Mecanum Starter Bot | Blocks OpModes from the 2026-11-15 qualifiers | `Front Left`, `Back Left`, `Front Right`, `Back Right` |

Code fails at **Init** with a "can't find device" error if it's run with the
wrong config active.

## 2026-2027 Java Bot Config

| Hub | Motor/encoder port 0 | Port 1 | Port 2 | Port 3 |
|---|---|---|---|---|
| Control Hub | Front Left Wheel | Back Left Wheel | Outake Motor (launcher) | Back Left Odometry Pod |
| Expansion Hub 2 | Front Right Wheel | Back Right Wheel | Intake Motor | Back Right Odometry Pod |

Other devices:

- **Control Hub servos:** 0 = Left Intake Servo (continuous), 1 = Middle Stage (feeder)
- **Expansion Hub servos:** 0 = Right Intake Servo (continuous)
- **Control Hub I2C bus 0:** `imu`, the hub's built-in gyro (BHI260AP)
- **Control Hub I2C bus 1:** `Range Sensor` (MR Range Sensor)
- **USB:** `Logitech Cam` webcam

## Odometry pods (installed 2026-11-19)

Two goBILDA odometry pods near the back wheels:

| Pod | Plugged into | Faces | Count direction |
|---|---|---|---|
| Back Left Odometry Pod | Control Hub **encoder** port 3 | Forward (measures forward/back) | Goes **up** driving forward |
| Back Right Odometry Pod | Expansion Hub **encoder** port 3 | Sideways (measures strafing) | Goes **down** strafing right, so **reverse it in code** |

- The pods use **motor encoder ports**, not the digital, analog or I2C ports.
  The SDK reads them as motors: `hardwareMap.get(DcMotor.class, ...)` then
  `getCurrentPosition()`. Never set power on them; a real motor could be on
  the power side of that port.
- Port 3 was chosen because ports 0 and 3 are the fast-counting encoder ports,
  and every other encoder port is used by a motor that needs its encoder.
- Turning the robot also moves the pods, because they're not at the robot's
  center. The odometry code has to subtract that out, using each pod's
  distance from the center (not measured yet).
- `OdometryPodTest` shows both counts and the IMU heading, for checking the
  wiring.

## ⚠️ Keep I2C bus 0 for the built-in IMU only

The Control Hub's built-in IMU shares **I2C bus 0** with whatever is plugged
into **I2C port 0**. On 2026-11-19 the MR Range Sensor on I2C port 0 blocked
the whole bus. The IMU stopped responding ("Error communicating with I2C
device(s) 'imu'") and read 0 for heading, pitch and roll. Unplugging the range
sensor from port 0 fixed the IMU. Moving the sensor to **I2C port 1 / bus 1**
made both work.

**Rule:** don't plug external I2C sensors into I2C port 0. Use ports 1–3, and
put each one on the matching bus number in the robot configuration.
