# 💾 Why We Back Up (a true story from qualifiers week)

> **The rule:** the robot must never have the *only* copy of our code.
> Code that's only on the robot isn't saved. It's just *not lost yet*.

---

## What happened the week of qualifiers (Nov 14–15, 2026)

This isn't a made-up scary story. It's all in our own GitHub history.

**🗓️ Friday, Nov 14:** someone left a half-finished Java file on the robot.
OnBotJava builds **every** Java file together, so one broken file made **all**
our Java OpModes disappear from the Driver Station. Not just that one file. All
of them.

*This time it didn't hurt us, because we competed with our **Blocks** code
and Blocks programs keep working when Java breaks. Now that our competition
code is moving to Java, the same mistake would take down our match code.*

**🗓️ Saturday, Nov 15, morning of qualifiers:** we fixed the TeleOp right on
the robot, adding the D-pad nudge, launcher reverse and intake eject. We also
wrote a brand-new Autonomous. For most of that day, **the code we competed with
existed in exactly one place: the Control Hub.** If the hub had been zapped by
static, dropped, or reset, that code would have been gone.


**🐛 There was also a hidden bug in it.** The fix added "press Cross to stop the
OpMode." The robot treats **Cross and A as the same button** (Cross is A on a
PlayStation-style controller). So the button meant to clear a jam could also
end TeleOp in the middle of a match. Nobody caught it, because it never went 
through GitHub, so there was no pull request and nonsecond pair of eyes.

---

## Ways code on the robot gets lost

- 💥 **Hub reset or reflash.** Sometimes we need it to fix a problem or update
  the robot software. It wipes everything.
- ⚡ **Static, a crash, a dead hub.** Robots get bumped, dropped and zapped.
- 👯 **Two people editing the same file in the browser.** Whoever saves last
  wins, and the other person's work is silently gone.
- ⏪ **No undo history.** Blocks and OnBotJava can't take you back to "the
  version from Tuesday that worked." GitHub can.
- 🤷 **"Which version is the good one?"** Without history, nobody knows what
  changed or who changed it.

---

## How to back up (it takes about one minute)

Plug the robot into a laptop with the USB-C cable, turn the robot on, then run:

```sh
./scripts/backup-hub.sh
```

That's it. The script:

1. Copies **every** Blocks program and Java file off the robot into
   `hub_backup/`
2. Saves them on a new branch (your own work isn't touched)
3. Pushes to GitHub and opens a **pull request**. Ask a teammate to look it over
   and merge it.

It tells you exactly what changed on the robot since the last backup:

```
- new on robot: blocks/Auto 2.blk
- changed:      onbotjava/org/firstinspires/ftc/teamcode/CompetitionTeleOp.java
- deleted from robot: blocks/Old test.blk
```

**No internet (like at an event)?** Run `./scripts/backup-hub.sh --no-push`.
Now the code is in **two places** (robot + laptop). Push it to GitHub as soon
as you're back on WiFi; the script prints the command.

---

## When to back up

| When | Why |
|---|---|
| **End of every meeting** | Whatever we did today is in two places before we go home. |
| **Before leaving any event** | Fixes made at the competition are the most important code we have and the easiest to lose. |
| **Before a hub reset, reflash or update** | Obviously! |
| **Before a big change** | So we can always go back to the version that worked. |

We take turns being the **💾 Repo Keeper**: the person who makes sure the
backup happens before everyone leaves.

---

## `hub_backup/` vs `current_season/`

- **`hub_backup/`** = an exact copy of **what's on the robot right now**. The
  script writes it. **Don't edit files in it.**
- **`current_season/`** = code we write and review on GitHub, then upload to
  the robot.

If the two don't match, someone changed the robot without updating GitHub, or
GitHub has code that hasn't been uploaded yet. Either way, it's worth asking
about.

---

## The big idea

Professional software engineers back up and use version control **every day**.
It's not because they expect disaster. It's because it's cheap, and losing
work is expensive. A one-minute backup beats rebuilding a whole autonomous the
night before a competition.

**⬇️ Pull → ✏️ Edit → 🤖 Test on robot → 💾 Back up → ⬆️ Push**
