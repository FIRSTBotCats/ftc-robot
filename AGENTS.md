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
   comments, docs). Everything else — code, OpModes, configuration, robot
   logic — must not be committed directly to `main`.

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

## Robot Code Backups

**The robot must never have the only copy of our code.** Code edited directly
on the Control Hub (Blocks or OnBotJava in the browser) exists only on the hub
until it is backed up. A hub reset, reflash, or failure loses it. See
[`docs/why-we-back-up.md`](docs/why-we-back-up.md) for why.

- **Back up with `./scripts/backup-hub.sh`** (robot plugged in over USB-C). It
  mirrors every Blocks program and OnBotJava file into `hub_backup/`, commits on
  a new `hub-backup-<date>` branch without touching the current checkout,
  pushes, and opens a PR. With no internet, use `--no-push` and push later.
- **Remind the user to back up** when they mention editing code on the robot,
  finishing a meeting, or leaving an event, and before any hub reset, reflash,
  or software update.
- **Never hand-edit `hub_backup/`.** The script replaces that whole folder on
  every run. Code the team writes and reviews goes in `current_season/`.
- **`hub_backup/` shows what was on the robot at the last backup**, not
  necessarily what's on it now. If it differs from `current_season/`, point
  that out: someone changed the robot without updating GitHub, or GitHub has
  code that hasn't been uploaded yet.
- **Don't back up a freshly reset hub.** The script refuses when the hub has no
  code; don't work around that. The last good backup is still on GitHub.

## Quick reference for agents

- ✅ Fix a typo in the README → commit to `main` is OK.
- ✅ Change robot code / OpModes → new branch, PR, someone else reviews.
- ✅ Code was changed on the robot → run `./scripts/backup-hub.sh` (or remind
  the user to).
- ❌ Hand-editing files in `hub_backup/` → don't; edit `current_season/`
  instead.
- ❌ Rewriting or force-pushing `main` → stop and ask a project owner first.
- ❓ Unsure whether a change counts as "documentation"? Treat it as
  substantive: branch + PR.
- ❌ Adding a personal email, phone number, or student last name → stop, do not
  commit, tell the user.
- ⚠️ Adding a last name of a public figure / guest lecturer → warn the user and
  confirm before committing.

_When in doubt, prefer a branch and a pull request over committing to `main`,
and treat anything that might be personal information as PII._
