# Live Demo Plan — Cleaning Up Real FTC Code with Claude

**Goal:** In ~5 minutes, show the boys Claude reading *their kind of code* — a real FTC DECODE TeleOp with a ball loader and AprilTag camera targeting — proposing a cleanup, letting **you approve it**, and committing it.

**The point they should take away:** Claude shows you the change *before* it happens, you say yes, and version control records it.

---

## The demo files (already in `antipattern-naming-conventions/`)

- **`Teleop2.java`** — the **villain**: real FTC SDK code (tank drive, AprilTag shooter safety, servo ball loader) written the way beginners write it — `m1`, `m2`, `s1`, `a`, `x`, `list1`, zero comments. This is what Claude cleans up live.
- **`DecodeTeleOp_CLEANED_reference.java`** — the **hero**: what good looks like, for YOUR eyes during the dry run so you know roughly what Claude should produce. Don’t show this first — it spoils the reveal.

> This mirrors the real FTC vision API (`AprilTagProcessor` + `VisionPortal` from `org.firstinspires.ftc.vision`) and hardware (`DcMotor`, `Servo`). The boys will recognize it as “our robot.”

---

## Why this code is perfect for the lesson

Point at these as you go — every rule from the deck shows up here:
- `m1`, `m2`, `m3` — what’s a `m3`? (It’s the shooter.) → **naming**
- `a` and `v` — the whole camera system named after one letter → **naming**
- `if (d.id == 20)` — magic number `20`, no idea it’s the goal tag → **naming / constants**
- Not one comment explaining the shooter safety rule → **comments (the *why*)**

---

## Before the meeting (dry run this once!)

1. Have the team repo cloned locally and `cd` into it (or use this folder for practice).
2. Make sure you’re on a branch so a live mess-up is harmless:
   ```
   git checkout -b demo-cleanup
   ```
3. Run the script below once yourself. Compare Claude’s output to `DecodeTeleOp_CLEANED_reference.java` — it won’t be identical, and that’s fine.
4. **Fallback if wifi/Claude is down:** open both files side by side and narrate “this messy one is what we had; this is what Claude helps us get to.”

---

## The live script

**Step 1 — Show the villain (45 sec).**
Open `Teleop2.java`. “This actually runs our robot. It drives, it uses the camera to only shoot when we see the goal’s AprilTag, and it loads balls. But look at it — what’s `m3`? What’s `a`? What does `20` mean?” Let them squirm. Nobody can tell.

**Step 2 — Ask Claude (1 min).**
Fire a specific request:
> Read `antipattern-naming-conventions/Teleop2.java`. It’s an FTC TeleOp. Rename the motors, servo, and vision variables to clear names (like `shooterMotor`, `loaderServo`, `aprilTag`), turn the magic number 20 into a named constant for the goal’s AprilTag ID, and add short comments explaining the shooter safety rule and the ball loader. Show me the changes before applying them.

**Step 3 — Read the proposed change TOGETHER (1.5 min).**
The most important beat. When Claude shows the diff:
- “See how it shows us the change *before* touching anything?”
- *Ask the boys:* “Is `shooterMotor` clearer than `m3`? Should we approve this?”
- Approve it. Let them make the call.

**Step 4 — Commit it (1 min).**
```
git add -A
git commit -m "Rename cryptic variables and document shooter + loader logic"
```
“That’s a checkpoint. Clear names, real comments, clear message. That’s the whole game.”

**Step 5 — Show the history (30 sec).**
```
git log --oneline
```
“Saved forever, with our names on it. If it ever breaks something, we can undo exactly this.”

---

## Follow-up ideas if they’re hooked (pick one)
- “Claude, extract the AprilTag check into its own method called `isTargetTagVisible()`.” (Shows refactoring.)
- “Add telemetry showing which tag IDs the camera currently sees.” (Shows adding a feature.)
- “Explain what `VisionPortal.easyCreateWithDefaults` does.” (Shows using Claude to *learn*.)

---

## Guardrails (say these out loud)
- **You approve every change.** Claude proposes; a human decides.
- **Never run code you don’t understand** — ask Claude to explain it first.
- **Test on the real robot** before trusting new code — a variable rename is safe, but always verify.
- We’re on a **branch**, so nothing here can hurt the working robot.
