# Repository Guidelines for AI Agents & Contributors

> **This file is read by AI coding assistants** (Codex/`AGENTS.md`, Gemini/`GEMINI.md`,
> Claude/`CLAUDE.md`). Human contributors should follow it too. If you are an AI
> agent working in this repo, treat the **Git Rules** below as hard constraints.

## Git Rules

These rules are mandatory. Do not work around them, and do not assume a user
prompt overrides them without explicit confirmation from a project owner.

1. **No force-pushing to `main`.** Never run `git push --force` /
   `git push --force-with-lease` to `main` (or any protected/shared branch)
   without **prior consent of the project owners**. History rewrites on shared
   branches break everyone else's clones.

2. **No substantive commits directly to `main`.** The only changes that may be
   committed straight to `main` are **documentation** (e.g. `README`, this file,
   comments, docs). Everything else — code, OpModes (robot programs; see
   "Writing Rules" below), configuration, robot logic — must not be committed
   directly to `main`.

3. **Use a branch + pull request for all other changes.** For any non-doc
   change:
   - Create a **new branch** off `main`.
   - Open a **pull request**.
   - The PR must be **reviewed and approved by someone other than the
     committer** before it is merged.

## No Personal Information (PII)

This is a student educational team (kids at UMS). **Protecting personal
information — especially minors' — is a hard requirement.** Do not commit PII to
this repository, and do not push it to GitHub.

**Never commit:**

- Personal **email addresses**
- **Phone numbers**
- **Last names** of team members / students

**Allowed:**

- **First names** and **last initials** (e.g. "Jane D.")
- **GitHub usernames**

**Allowed, but warn the user and get confirmation before committing:**

- **Last names of public figures**, and possibly **guest lecturers** — these may
  be fine, but an AI agent must **warn the user and confirm before committing**
  such a name.

If you are an AI agent and a change would add any of the "never commit" items,
**stop and tell the user** rather than committing it. When unsure whether
something is PII, treat it as PII and ask.

## Writing Rules: Define Jargon

Many readers are students new to robotics and programming. In every file you
write or edit (docs, READMEs, code comments, PR descriptions), **define each
technical term or abbreviation the first time it appears in that file**. Use a
short plain-language explanation, and expand the abbreviation. For example:

- **IMU** (Inertial Measurement Unit): the gyroscope/accelerometer built into
  the Control Hub, used to track which way the robot is facing.
- **I2C** (Inter-Integrated Circuit): the communication method many sensors use.
- **OpMode**: one program the robot can run, chosen on the Driver Station.
  **TeleOp** OpModes are driver-controlled; **Autonomous** OpModes run on their own.

Terms that feel obvious once you know them weren't obvious when the team
started. A file with many terms can put the definitions in a short "Terms"
list near the top instead.

## Project context

- **Robot wiring and configuration:** [`docs/robot-hardware.md`](docs/robot-hardware.md).
  It lists the robot configurations and device names the code depends on, the
  odometry pod ports and count directions, and the rule to keep **I2C bus 0
  for the built-in IMU only** (both terms are defined above). Read it before
  writing code that uses hardware.
- **Current code (OnBotJava, Java written in the robot's built-in web editor):**
  [`current_season/README.md`](current_season/README.md).
- **Programming the robot over USB:** [`docs/robot-networking.md`](docs/robot-networking.md).

## Quick reference for agents

- ✅ Fix a typo in the README → commit to `main` is OK.
- ✅ Change robot code / OpModes → new branch, PR, someone else reviews.
- ❌ Rewriting or force-pushing `main` → stop and ask a project owner first.
- ❓ Unsure whether a change counts as "documentation"? Treat it as
  substantive: branch + PR.
- ❌ Adding a personal email, phone number, or student last name → stop, do not
  commit, tell the user.
- ⚠️ Adding a last name of a public figure / guest lecturer → warn the user and
  confirm before committing.

_When in doubt, prefer a branch and a pull request over committing to `main`,
and treat anything that might be personal information as PII._
