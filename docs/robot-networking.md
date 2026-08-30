# Robot Networking: Programming Without Losing Internet

**Problem:** Our robot's brain is a **REV Control Hub**, which broadcasts its own
WiFi access point. To use the browser-based Program & Manage interface
(OnBotJava / Blocks) you normally join that WiFi — which means the laptop loses
its internet connection while connected to the robot.

This doc lists the ways other FTC teams solve that, and which ones actually fit
our Mac + "push files through the web interface" workflow.

## Why the internet drops

The REV Control Hub is an Android 7.1 device (Rockchip RK3328, Realtek RTL8821CU
WiFi) running the Robot Controller app. It broadcasts a WiFi access point (named
something like `TEAMNUMBER-RC`, or starting with `FTC-` / `FIRST-` depending on
SDK/OS version) and serves everything at:

- **AP IP address:** `192.168.43.1`
- **Program & Manage web interface (OnBotJava / Blocks):** `http://192.168.43.1:8080`

When the laptop joins that access point, it's on the robot's isolated
network with no path to the internet. That's the tradeoff we're hitting.

> **We are a Mac team.** That matters here: REV's own tool for reaching the
> web console over USB is **Windows-only** (see below), so our best *confirmed*
> option is a second WiFi adapter. A USB-C + ADB shortcut looks promising but is
> **not yet verified on our hardware** — it's flagged as such.

## Solutions

### ✅ Second WiFi adapter — confirmed, works on macOS  ← recommended for us

Add a cheap (~$15) USB WiFi dongle so the laptop has two WiFi interfaces:

- Built-in WiFi → the internet.
- USB WiFi dongle → the robot's access point.

macOS routes `192.168.43.x` traffic to the dongle and everything else to the
internet WiFi automatically, **as long as the robot interface has no default
gateway**. Set **System Settings ▸ Network ▸ Set Service Order** so the internet
WiFi is listed above the dongle. This keeps our exact browser/Blocks workflow and
relies only on standard OS networking — nothing FTC-specific has to cooperate.

### ✅ REV Hardware Client over USB-C — confirmed, but **Windows-only**

REV's official desktop app can open the Program & Manage (OnBotJava / Blocks)
console **over USB-C**, no robot WiFi needed. Per REV's docs: *"You are able to
connect to a Control Hub over Wi-Fi or directly through USB-C,"* then use the
**Program and Manage** tab. It also does OS/firmware updates, WiFi reconfig, and
log viewing.

**Catch:** the REV Hardware Client ships **for Windows only** (all installers are
`.exe`, Windows 10+). It is *not* available for macOS. This is a good option only
if someone has a Windows laptop or a Windows VM (Parallels). Download:
<https://docs.revrobotics.com/rev-hardware-client/>

### ⚠️ Raw ADB port-forward over USB-C — plausible, **UNVERIFIED on our hardware**

This *should* be the Mac equivalent of the Hardware Client's USB console (it is
almost certainly how that client tunnels port 8080 internally), but **no FTC/REV
documentation confirms it, and we have not tested it.** Do not rely on it until
we've proven it on the robot.

```sh
# macOS: install the platform tools once
brew install android-platform-tools

# with the hub connected over USB-C:
adb devices                       # confirm the hub is listed
adb forward tcp:8080 tcp:8080     # ATTEMPT to tunnel the hub's web server over USB
```

Then try **`http://localhost:8080`**. It only works if the Robot Controller's web
server is reachable on the device's loopback interface — which is the open
question. If `localhost:8080` doesn't load, this approach is out; fall back to the
second WiFi adapter. (Deploying **Android Studio** Java code over USB via ADB *is*
confirmed to work on macOS — but that's for Java, not our Blocks/web workflow.)

## Approaches that do NOT work

- **Hub joins another WiFi while still vending its own, funneling laptop
  internet through the robot.** Not supported: the Control Hub's soft-AP does no
  NAT / internet sharing, and the FTC SDK doesn't expose concurrent
  station+access-point mode. (It would also violate competition WiFi rules.)
- **Hub joins an existing network as a client with a reserved IP on the subnet.**
  Not supported: there is no station/client mode — the Control Hub is designed to
  *be* the access point.
- **DHCP / RNDIS ethernet-over-USB (the way an xTool laser vends a network).**
  The Control Hub's USB-C port exposes ADB, not a USB-ethernet gadget, so this
  exact mechanism isn't available. (Whether ADB port-forwarding reaches the web
  console — the section above — is the open question to test.)

## To verify on the hardware (resolves the open question)

The one unconfirmed claim is whether the web console is reachable over USB via
ADB. Test it directly:

1. `brew install android-platform-tools`.
2. Connect the Control Hub via USB-C, power it on.
3. `adb devices` — confirm the hub appears (this alone proves ADB-over-USB works).
4. `adb forward tcp:8080 tcp:8080`, then open `http://localhost:8080`.
   - **Loads the OnBotJava/Blocks console →** the ADB shortcut works; document it
     and promote it above the WiFi-adapter method.
   - **Does not load →** the RC web server isn't on loopback; drop this approach
     and use the second WiFi adapter.
5. Either way, confirm the laptop still has internet on its built-in WiFi.

> Note: on a managed/work laptop, installing the REV Hardware Client or
> `platform-tools` may be subject to MDM restrictions.

## Sources

- REV Robotics docs — connecting to the robot control console (Wi-Fi *or* USB-C
  via the Hardware Client), and the Hardware Client (Windows-only):
  <https://docs.revrobotics.com/duo-control/menu/control-hub-gs/connect-to-the-control-hub-robot-control-console.md>,
  <https://docs.revrobotics.com/rev-hardware-client/>
- Game Manual 0 — <https://gm0.org> (Using Android Studio; control-system internals)
- FIRST Tech Challenge docs — <https://ftc-docs.firstinspires.org>

_Note: the raw `adb forward` web-console shortcut is **not** documented by any of
these sources; it is an untested inference pending the hardware check above._
