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
