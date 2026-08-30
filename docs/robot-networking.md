# Robot Networking: Programming Without Losing Internet

**Problem:** Our robot's brain is a **REV Control Hub**, which broadcasts its own
WiFi access point. To use the browser-based Program & Manage interface
(OnBotJava / Blocks) you normally join that WiFi — which means the laptop loses
its internet connection while connected to the robot.

This doc lists the ways other FTC teams solve that, ranked by fit for our
"push files through the web interface" workflow.

## Why the internet drops

The REV Control Hub is an Android 7.1 device (Rockchip RK3328, Realtek RTL8821CU
WiFi) running the Robot Controller app. It broadcasts a WiFi access point named
`TEAMNUMBER-RC` and serves everything at:

- **AP IP address:** `192.168.43.1`
- **Program & Manage web interface (OnBotJava / Blocks):** `http://192.168.43.1:8080`

When the laptop joins the `-RC` access point, it's on the robot's isolated
network with no path to the internet. That's the tradeoff we're hitting.

The key insight: the fix most teams use is **not WiFi at all** — it's the
**USB-C cable + ADB (Android Debug Bridge)**. The laptop reaches the robot over
USB while its WiFi stays connected to the internet.

## Recommended solutions (ranked)

### 1. REV Hardware Client over USB-C  ← best fit

The official REV desktop app (available for macOS). Plug the Control Hub into the
laptop with a USB-C cable and:

- It auto-connects ADB to the hub.
- Its **"Program & Manage"** button opens the OnBotJava / Blocks web interface
  **over the cable** — the laptop's WiFi/internet is never touched.
- Bonus: it also handles Control Hub OS / firmware updates, WiFi reconfiguration,
  and log viewing.

This is the cleanest match for our current web-interface workflow.

### 2. Raw ADB port-forward (manual equivalent)

Same result as the REV Hardware Client, without the GUI. With `adb` installed:

```sh
# macOS: install the platform tools once
brew install android-platform-tools

# with the hub connected over USB-C:
adb devices                       # confirm the hub is listed
adb forward tcp:8080 tcp:8080     # tunnel the hub's web server over USB
```

Then open **`http://localhost:8080`** in the browser. The laptop keeps its normal
WiFi internet the whole time.

### 3. Second WiFi adapter (fully wireless, keeps internet)

If we'd rather not use a cable, add a cheap (~$15) USB WiFi dongle:

- Built-in WiFi → the internet.
- USB WiFi dongle → the robot's `TEAMNUMBER-RC` access point.

macOS routes `192.168.43.x` traffic to the dongle and everything else to the
internet WiFi automatically, **as long as the robot interface has no default
gateway**. Set **System Settings ▸ Network ▸ Set Service Order** so the internet
WiFi is listed above the dongle.

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
  exact mechanism isn't available — but ADB (solutions 1 and 2 above) achieves
  the same outcome.

## To test once the hardware is connected

1. `brew install android-platform-tools` (or install the REV Hardware Client).
2. Connect the Control Hub via USB-C.
3. `adb devices` — confirm the hub appears.
4. `adb forward tcp:8080 tcp:8080`, then open `http://localhost:8080`.
5. Confirm the laptop still has internet on its WiFi while programming.

> Note: on a managed/work laptop, installing the REV Hardware Client or
> `platform-tools` may be subject to MDM restrictions.

## Sources

- Game Manual 0 — <https://gm0.org> (Using Android Studio; control-system internals)
- REV Robotics docs — <https://docs.revrobotics.com/rev-hardware-client/>
- FIRST Tech Challenge docs — <https://ftc-docs.firstinspires.org>
