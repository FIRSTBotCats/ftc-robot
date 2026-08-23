# GitHub Organization Setup — BotCats

Do this before (or at the start of) the meeting. A free **Organization** is the right choice: unlike a personal repo, an Org can have **multiple Owners**, so all three boys — plus you — genuinely co-own the team’s code, and it survives after you step away.

---

## Age & account notes (read first)

- **GitHub requires users to be 13+.** Your boys are at the line — make sure their parents used correct birthdates at signup, or the accounts can be flagged.
- **Anthropic’s consumer terms require 18+.** So for Claude Code, a **coach or parent stays signed in and supervises**; the boys drive under that adult account. Give parents a heads-up so nobody’s surprised.
- Have everyone confirm their GitHub **username** before the meeting so invites go fast.

---

## Step 1 — Create the Organization (you, ~2 min)

1. Sign in to your GitHub account.
2. Top-right **+** menu → **New organization**.
3. Choose the **Free** plan.
4. Org name: e.g. `botcats-robotics` (must be unique; add your team number if taken, e.g. `botcats-12345`).
5. Skip/decline any paid upsells.

## Step 2 — Invite the boys (~3 min)

1. Org page → **People** → **Invite member**.
2. Enter each boy’s GitHub username or email → send.
3. They accept the email/notification invite. (Do this live if any haven’t — it’s a nice “you’re on the team” moment.)

## Step 3 — Make everyone an Owner (~2 min)

1. **People** → click each member → set **Role: Owner**.
2. Now all four of you can manage repos, settings, and members.
   - *Note:* Owner is powerful. Fine for a 3-person learning team; just explain “Owner means you can change anything, so be careful.”

## Step 4 — Create the first repo (~2 min)

1. Org page → **Repositories** → **New repository**.
2. Name: `robot-code` (or your season’s name).
3. **Private** is the safe default for a student team.
4. Check **Add a README** so the repo isn’t empty.
5. Create.

## Step 5 — First commit as a team (~5 min)

Easiest for day one — edit right in the browser:

1. Open the repo → click `README.md` → pencil ✏️ to edit.
2. Add a line: `# BotCats Robot Code` and a sentence about the team.
3. Scroll down → commit message like `Add team intro to README` → **Commit changes**.
4. 🎉 That’s the team’s first commit. Everyone can see it under the repo’s **commits**.

*(Later, when they’re ready for the real loop, install Git locally and `git clone` the repo — but the browser edit is the perfect zero-setup first win.)*

---

## Quick reference: the loop they’ll use

```
git clone <repo-url>     # once, to get a copy
git pull                 # before you start — get the latest
# ...edit code...
git add -A               # stage your changes
git commit -m "message"  # save a checkpoint
git push                 # share with the team
```

---

## Suggested repo starter files (optional, nice to have)

- `README.md` — what the robot does, how to run the code.
- `.gitignore` — so build junk doesn’t get committed (GitHub offers templates when you create the repo).
- Drop in `team-code-rules.md` (in the project root) so the standards live *in* the repo.
