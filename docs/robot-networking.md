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

> **We are a Mac team.** The best option for us is **ADB port-forwarding over
> USB-C** — verified working on our own Control Hub (see below). It keeps the
> laptop's built-in WiFi on the internet the whole time. A second WiFi adapter is
> a solid no-USB alternative. REV's own desktop tool also does this over USB but
> is **Windows-only**.

## Solutions

### ✅ ADB port-forward over USB-C — confirmed on our hardware  ← recommended

Tunnel the Control Hub's web server to your Mac over the USB-C cable. Your WiFi
never touches the robot, so the laptop keeps full internet the entire time.
**Verified on our Control Hub v1.0 (Rockchip RK3328).**

```sh
# macOS: install the platform tools once (note: the CASK, not the formula)
brew install --cask android-platform-tools

# with the hub connected over USB-C and powered on (12V battery):
adb devices                       # confirm the hub is listed as "device"
adb forward tcp:8080 tcp:8080     # tunnel the hub's web server to localhost
```

Then open **`http://localhost:8080`** — the OnBotJava / Blocks console loads,
with the laptop still on its normal internet WiFi. Deploying Android Studio Java
code over the same USB connection works too (it's plain ADB).

**Gotcha — the cable matters twice:**

1. **Use a real data cable.** Many USB-C cables are charge-only and will not
   enumerate the hub at all.
2. **The hub's USB-C socket is recessed.** It only mates fully with a cable whose
   connector shell is slightly longer than standard (the cable REV ships is cut
   for this). A perfectly good data cable can *look* seated but not make the data
   contacts — if `adb devices` is empty, try the REV-provided cable before
   assuming a software problem.

If `adb devices` is empty: confirm the hub is powered from its **12V battery**
(the Android SoC boots off the battery, not off USB-C), the cable is a
fully-seated data cable per above, then `adb kill-server && adb start-server &&
adb devices`. A healthy connection looks like:

```
List of devices attached
f9c3da48d1816f2b  device  product:ch_v1_box model:Control_Hub_v1_0 device:rk3328_box transport_id:2
```

### ✅ Second WiFi adapter — confirmed, works on macOS  ← no-USB alternative

Add a cheap (~$15) USB WiFi dongle so the laptop has two WiFi interfaces:

- Built-in WiFi → the internet.
- USB WiFi dongle → the robot's access point.

macOS routes `192.168.43.x` traffic to the dongle and everything else to the
internet WiFi automatically, **as long as the robot interface has no default
gateway**. Set **System Settings ▸ Network ▸ Set Service Order** so the internet
WiFi is listed above the dongle. This keeps our exact browser/Blocks workflow and
relies only on standard OS networking — nothing FTC-specific has to cooperate.
Good when you'd rather not tether the laptop to the robot with a cable.

### ✅ REV Hardware Client over USB-C — confirmed, but **Windows-only**

REV's official desktop app can open the Program & Manage (OnBotJava / Blocks)
console **over USB-C**, no robot WiFi needed. Per REV's docs: *"You are able to
connect to a Control Hub over Wi-Fi or directly through USB-C,"* then use the
**Program and Manage** tab. It also does OS/firmware updates, WiFi reconfig, and
log viewing.

**Catch:** the REV Hardware Client ships **for Windows only** (all installers are
`.exe`, Windows 10+). It is *not* available for macOS — which is why the raw ADB
approach above is our Mac equivalent (it's the same USB tunnel the client uses
internally). Only relevant if someone has a Windows laptop or VM. Download:
<https://docs.revrobotics.com/rev-hardware-client/>

## Approaches that do NOT work

- **Hub joins another WiFi while still vending its own, funneling laptop
  internet through the robot.** Not supported: the Control Hub's soft-AP does no
  NAT / internet sharing, and the FTC SDK doesn't expose concurrent
  station+access-point mode. (It would also violate competition WiFi rules.)
- **Hub joins an existing network as a client with a reserved IP on the subnet.**
  Not supported: there is no station/client mode — the Control Hub is designed to
  *be* the access point.
- **DHCP / RNDIS ethernet-over-USB (the way an xTool laser vends a network).**
  The Control Hub's USB-C port exposes ADB, not a USB-ethernet gadget. The ADB
  port-forward above achieves the same end result (web console over USB) through
  a different mechanism.

## Quick start (recommended path)

1. `brew install --cask android-platform-tools`.
2. Connect the Control Hub via USB-C (real data cable, fully seated — see the
   recessed-socket note) and power it on from the 12V battery.
3. `adb devices` — confirm the hub appears as `device`.
4. `adb forward tcp:8080 tcp:8080`, then open `http://localhost:8080`.
5. The laptop keeps internet on its built-in WiFi throughout.

> Note: on a managed/work laptop, installing the REV Hardware Client or
> `platform-tools` may be subject to MDM restrictions.

## Sources

- REV Robotics docs — connecting to the robot control console (Wi-Fi *or* USB-C
  via the Hardware Client), and the Hardware Client (Windows-only):
  <https://docs.revrobotics.com/duo-control/menu/control-hub-gs/connect-to-the-control-hub-robot-control-console.md>,
  <https://docs.revrobotics.com/rev-hardware-client/>
- Game Manual 0 — <https://gm0.org> (Using Android Studio; control-system internals)
- FIRST Tech Challenge docs — <https://ftc-docs.firstinspires.org>
- **The ADB port-forward web-console shortcut is verified on our own Control Hub
  v1.0 (2026-08-30), not from vendor documentation.** It is the Mac equivalent of
  what the (Windows-only) REV Hardware Client does over USB.
