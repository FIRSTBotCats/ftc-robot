# antipattern-naming-conventions — Demo Files

These two files exist for the **Claude Code live demo** (see `../claude-demo.md`). They’re a matched “before and after” pair of the *same* FTC DECODE TeleOp — tank drive, an AprilTag camera that only lets the shooter fire when it sees the goal tag, and a servo that loads one ball at a time.

| File | Role | Show the boys? |
|---|---|---|
| **`Teleop2.java`** | The **villain**. Real FTC SDK code written the way beginners write it: `m1`/`m2`/`m3` motors, `s1` servo, `a`/`v` for the whole vision system, magic number `20`, and zero comments. | ✅ Yes — this is what you clean up live with Claude. |
| **`DecodeTeleOp_CLEANED_reference.java`** | The **hero**. The same code with clear names, named constants (`TARGET_TAG_ID`, `LOADER_OPEN`), an extracted `isTargetTagVisible()` method, and “why” comments. | 🙈 No — for *your* eyes during the dry run, so you know what good looks like. Showing it first spoils the reveal. |

## How to use them

1. During the demo, open `Teleop2.java` and let the boys squint at `m3` and `if (d.id == 20)`.
2. Ask Claude to clean it up (exact prompt in `../claude-demo.md`).
3. Compare Claude’s output to `DecodeTeleOp_CLEANED_reference.java`. **It won’t match exactly — that’s expected.** The reference just shows the *kind* of improvement to aim for.

## Before you show this live

- **Compile against your team’s actual FTC SDK version first.** The vision API (`org.firstinspires.ftc.vision.apriltag`) and hardware classes are real, but details drift between seasons.
- **Tag ID `20` is a placeholder,** not a confirmed DECODE goal ID. Swap in the real one from your game manual if you want it exact — or leave it, since the whole point is that a bare `20` means nothing without a name.
- These are teaching artifacts, **not** the code that runs your competition robot.
