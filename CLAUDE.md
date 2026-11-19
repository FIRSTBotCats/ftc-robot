# Claude — Repository Guidelines

The full contribution and **Git Rules** for this repository live in
[`AGENTS.md`](./AGENTS.md). Read and follow that file.

**Git Rules summary (mandatory):**

1. **No force-pushing to `main`** without prior consent of the project owners.
2. **No commits directly to `main` except documentation.**
3. **All other substantive changes** go in a **new branch** via a **pull
   request**, reviewed by **someone other than the committer**.

**No PII (mandatory):** Never commit personal email addresses, phone numbers, or
student last names. First names + last initials and GitHub usernames are OK. Last
names of public figures / guest lecturers may be OK — **warn the user and confirm
before committing** them.

**Robot code backups (mandatory):** The robot must never have the only copy of
our code. After code is changed on the Control Hub, back it up with
`./scripts/backup-hub.sh` (or remind the user to, e.g. at the end of a meeting
or before leaving an event). Never hand-edit `hub_backup/`; the script replaces
it on every run. Details: `AGENTS.md` and `docs/why-we-back-up.md`.
