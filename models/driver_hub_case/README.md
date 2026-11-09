# BotCats #30521: REV Driver Hub TPU Case

A drop-resistant TPU case for the REV Driver Hub (REV-31-1596). Its main job is to keep the USB plugs and cables from hitting the floor when the hub is dropped.

## Files

| File | What it is |
| --- | --- |
| `botcats_driver_hub_case.scad` | Parametric source. Edit this, then export a new STL. |
| `botcats_driver_hub_case.stl` | Ready-to-slice STL using the default parameters. |
| `preview_front.png`, `preview_back.png` | Renders of the default design. |

## Design summary

- **Hub size (REV spec):** 133.35 x 85.73 x 25.4 mm (5.25 x 3.375 x 1.0 in), 278 g.
- **Case size:** about 144 x 148 x 32 mm including the cat ears. This fits the A1 mini's 180 mm bed.
- **Port bay:** the USB edge (the "top" edge, +Y) opens into a recessed bay 112 mm wide and 32 mm deep. The plug bodies sit below the rim, so the bay walls and the cable comb hit the floor first.
- **Cable comb:** keyhole slots at the far end of the bay. Each slot mouth is narrower than the cable, so the cable snaps in. A yanked cable pulls on the case instead of the USB port.
- **Screen lip:** covers 4 mm of the bezel on three sides and stands about 1.5 mm above the glass. The underside has a 45 degree chamfer, so it prints without supports. The lip is open across the bay.
- **Corner bumpers and cat ears:** the corners are thicker. Each ear has a 5 mm hole for a lanyard or a zip tie to the driver station.
- **Back:** hex vents (optional) and "BOTCATS #30521" debossed under the bay. The text reads correctly from behind.

## Assumptions to verify on the real hub

REV does not publish exact port positions, so these were assumed:

1. The three USB-A ports and the USB-C port are all on one long edge, roughly centered.
2. The power button and Ethernet port are either on that edge, inside the 112 mm bay, or not needed during matches.
3. The hub's corner radius is at least 4 mm. A rounder hub just leaves small air gaps at the corners, which is fine.

Hold the hub against `preview_front.png` and check before printing. If something is in the wrong place, adjust the parameters below.

## Adjusting cable slot positions

Open the `.scad` file in OpenSCAD (free, openscad.org) or any text editor and find the `slots` list:

```
slots = [
    [ 24.7, 12.7, 5.0, 3.6],   // gamepad 1
    [ 52.7, 12.7, 5.0, 3.6],   // gamepad 2
    [ 80.7, 12.7, 5.0, 3.6],   // gamepad 3
    [108.7, 12.7, 5.0, 3.6],   // charger (USB-C)
];
```

Each row is one slot: `[x_from_left, z_from_back, seat_d, mouth_w]`.

| Field | Meaning | How to measure |
| --- | --- | --- |
| `x_from_left` | Distance from the hub's left end to the cable centre | Screen facing you, ports on top. Measure from the left end of the hub to the middle of each port with calipers or a ruler. |
| `z_from_back` | Distance from the hub's back face to the cable centre | The hub is 25.4 mm thick, so 12.7 is the middle. Measure from the back face to the centre of the port opening. |
| `seat_d` | Round seat the cable sits in | Cable outside diameter plus about 0.5 mm. |
| `mouth_w` | Opening the cable snaps through | Cable outside diameter minus 0.5 to 1 mm. Smaller holds tighter. |

Tips:

- Cables can bend inside the bay, so a slot doesn't have to line up exactly with its port. Lining it up keeps the cable straight and reduces strain on the port.
- Add or delete rows to change the number of slots.
- Keep every slot inside the bay. With the defaults, that means `x_from_left` between about 11 and 122.
- If the ports are off-center, move the whole bay with `bay_x` (positive moves it right), then adjust the slots to match.
- If a plug sticks out more than about 28 mm from the hub, increase `bay_d` so the comb clears it.

## Adding extra cutouts (power button, Ethernet, SD card)

The `cutouts` list is empty by default. Each row is `[face, pos, z_from_back, w, h]`:

- `face`: `"top"` (port edge), `"bottom"`, `"left"`, or `"right"`
- `pos`: for top and bottom, mm from the hub's left end to the cutout centre. For left and right, mm from the hub's bottom edge.
- `z_from_back`: mm from the hub's back face to the cutout centre
- `w`, `h`: cutout width along the edge and height through the thickness

Example, Ethernet on the bottom edge 40 mm from the left:

```
cutouts = [ ["bottom", 40, 12.7, 18, 16] ];
```

## Other useful parameters

| Parameter | Default | Purpose |
| --- | --- | --- |
| `clr` | 0.4 | Clearance per side. Raise to 0.5 if the hub is too hard to fit; lower to 0.3 if it's loose. |
| `bay_w` / `bay_x` / `bay_d` | 112 / 0 / 32 | Width, horizontal offset, and depth of the port bay |
| `lip_in` | 4.0 | How far the lip covers the screen bezel |
| `wall` / `back_t` | 3.2 / 2.4 | Wall and back thickness |
| `vents` | true | Hex vents on the back. Set to false to reduce stringing and retractions. |
| `cat_ears` | true | Cat ears with lanyard holes |
| `label` / `label2` | BOTCATS / #30521 | Back text |
| `show_hub` | false | Shows a ghost of the hub in the preview. Preview only; it is not exported. |

## Exporting a new STL

1. Open `botcats_driver_hub_case.scad` in OpenSCAD.
2. Edit the parameters. The Customizer panel (Window > Customizer) works for single numbers. Edit the `slots` and `cutouts` lists directly in the code.
3. Press F6 to render. This takes 10 to 30 seconds.
4. File > Export > Export as STL.

## Print settings (Bambu Lab A1 mini, Bambu TPU 90A)

### Filament preset: "Bambu TPU 90A @BBL A1M" (use the defaults)

| Setting | Value |
| --- | --- |
| Nozzle | 225 °C |
| Textured PEI plate | 30 °C |
| Max volumetric speed | 2.8 mm³/s |

At 2.8 mm³/s the slicer caps real print speed at roughly 30 mm/s, regardless of the process speeds.

### Process preset (based on 0.20mm Standard @BBL A1M)

Lower these so the numbers match what the printer will actually do:

| Setting | Value |
| --- | --- |
| Initial layer, initial layer infill | 20 mm/s |
| Outer wall | 25 mm/s |
| Inner wall, sparse infill, internal solid infill, top surface, gap infill | 30 mm/s |
| Travel | 700 mm/s (unchanged) |
| Walls | 3 to 4 |
| Infill | 15 to 20% gyroid |
| Supports | None |
| Orientation | Back flat on the bed, screen side up (as exported) |

Save both presets under team names so they don't have to be re-entered.

## Feeding TPU on the A1 mini (most important)

1. **Never feed TPU through the AMS lite.** Bambu advises against flexible filament in the AMS lite. Use the external spool and select it as the filament source in Bambu Studio.
2. **Keep the feed path short and straight.** 90A is soft and buckles easily. Put the spool above the printer, take the PTFE tube off the toolhead inlet, and let the filament drop straight in. The spool should turn with almost no resistance.
3. **Dry the filament.** 50 to 55 °C for 6 to 8 hours before printing, even for a new spool. Store it sealed with desiccant, or print straight from a dry box.
4. **Unload by hand.** The cutter cannot unload TPU.

## Troubleshooting log

**Failure (Nov 2026):** the first case print came out lacy and stringy, with thin, gappy lines and fuzzy hairs through every layer.

**Diagnosis:** severe under-extrusion. The slicer was not the cause: the default filament preset already limits flow to 2.8 mm³/s. The likely causes were filament starved by drag in the feed path, plus moisture (the hairy fuzz).

**Fix, in order:**

1. Feed from above, straight into the toolhead, with no AMS lite and no long tube.
2. Dry the spool.
3. Use the settings above.
4. Print a 20 mm test cube before the full case.

**How to tell it's working:** first-layer lines are solid and slightly glossy, and the extruder doesn't click. If lines come out thin or broken, stop the print; the filament is still being starved.

The full case takes noticeably longer at TPU speeds. That's expected.

## Fitting the hub

Peel the screen lip back and push the hub in, port edge first, into the bay side. Then stretch the lip over the remaining three edges. Snap each cable into its comb slot from the front.
