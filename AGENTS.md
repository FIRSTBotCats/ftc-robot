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

## Quick reference for agents

- ✅ Fix a typo in the README → commit to `main` is OK.
- ✅ Change robot code / OpModes → new branch, PR, someone else reviews.
- ❌ Rewriting or force-pushing `main` → stop and ask a project owner first.
- ❓ Unsure whether a change counts as "documentation"? Treat it as
  substantive: branch + PR.

_When in doubt, prefer a branch and a pull request over committing to `main`._
