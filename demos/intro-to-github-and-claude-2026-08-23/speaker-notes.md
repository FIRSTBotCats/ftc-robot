# BotCats — Speaker Notes / Run of Show

**Target:** ~50 minutes. Deck is `presentation.html` (open in a browser, press `F` for fullscreen, arrow keys to advance).

**Golden rule for the room:** three 13-year-olds get bored fast. Keep each slide under ~90 seconds, ask questions instead of lecturing, and lean into the jokes. When energy dips, jump to the demo early.

**Running gag:** `Jeff_is_cool.java` is the recurring villain. Bring it back every time you can. (If one of the boys is named Jeff, even better — but keep it affectionate.)

---

## Timing budget

| Section | Slides | Time |
|---|---|---|
| Hook + the problem | 1–3 | 6 min |
| What VC/Git/GitHub is | 4–6 | 8 min |
| Workflow + branches | 7–8 | 6 min |
| Why it matters for FTC | 9–10 | 5 min |
| Best practices | 11–14 | 8 min |
| AI / Claude Code | 15–17 | 7 min |
| Live demo | 18 | 5 min |
| Rules + close | 19–20 | 3 min |
| Buffer / Q&A | — | 2 min |

---

## Per-slide script

**1 — Title.** “Today you go from Blocks to being a real software team. By the end you’ll each own part of the BotCats code.” Keep it 20 seconds.

**2 — The villain.** Read the bad code out loud, deadpan. “Someone — not naming names — wrote a file called `Jeff_is_cool.java`.” Big laugh moment. *Ask:* “If I open this in three months, do I have any idea what `x` is?” (No.) That pain is the whole reason for today.

**3 — final_final_v2.** *Ask:* “Anyone ever saved a file like `essay_final_FINAL_real.docx`?” Everyone has. “This is how NOT to save code. There’s a better way.” Don’t over-explain — this is the setup.

**4 — Save points.** Anchor everything to video games. *Ask:* “What do you do right before a boss fight?” (Save.) Version control = save points for code. Mess up → load your last good save.

**5 — Git vs GitHub.** The one vocabulary slide. Say it twice: “Git is the save button on your computer. GitHub is the group chat for your code.” Don’t go deeper than this.

**6 — Commits.** “Every save has a sticky note saying what you did.” Read the bad messages (“asdf”) for a laugh, then the good ones. *Ask:* “Which one helps you at 11pm the night before competition?”

**7 — The loop.** Physically point at each step. “Four words: Pull, Edit, Commit, Push.” Repeat the golden rule — **pull before you start** — because forgetting it is the #1 thing that’ll bite them.

**8 — Branches.** “You want to try a crazy new autonomous. On a branch, you can break EVERYTHING and the working robot is untouched.” This maps to their real fear: bricking the bot before a match.

**9 — Competition.** This is the “why should I care” slide — slow down here. Tell a story: “Imagine it’s 5 minutes to your match and someone’s change broke the drive code. With version control, one command puts it back.” That’s the sell.

**10 — The Org.** “This isn’t my account you’re borrowing. It’s a team Organization and you’re all owners.” Read the yellow box out loud — the 18+ Claude note matters so they know why a grown-up is logged in. Frame it as “you drive, we ride shotgun,” not “you’re not allowed.”

**11 — Read > write.** “You type a line once. You read it a hundred times.” This reframes best practices as being kind to teammates and future-you, not “rules from the coach.”

**12 — Naming.** Villain returns. Good-vs-bad side by side. *Ask:* “What does `armSpeed` do? What does `x` do?” One’s obvious, one’s a mystery. That’s the point.

**13 — Comments.** Key idea, say it clearly: “Don’t write a comment that just repeats the code. Write WHY.” The tip-over example lands with robot kids.

**14 — Small commits.** “Change one thing, test it, save it. If it breaks, you know exactly what broke it.” Contrast with a giant “I changed everything” commit = debugging nightmare.

**15 — Blocks to AI.** Validate what they already know. “You already understand loops and if/else from Blocks. You don’t need to memorize every semicolon — Claude helps with the spelling, you bring the ideas.”

**16 — What is Claude Code.** “It’s like an intern who’s read a million programs but has zero common sense about YOUR robot.” Read the box: **you’re the engineer in charge**, always ask what something does before running it.

**17 — Working with it.** Hit the four habits fast. Emphasize “always read what it writes” and “test on the robot.” *Ask:* “If Claude writes something and you don’t understand it, what do you do?” (Ask it to explain.)

**18 — DEMO.** Switch to the terminal. Follow `claude-demo.md`. Narrate every step: “Watch — I’m asking it to rename our villain variable. See how it shows me the change BEFORE doing it? I approve it. Now we commit.” Keep it to one small, obviously-good change.

**19 — The rules.** Read them together, fast, like a team chant. This is the takeaway slide — consider printing `team-code-rules.md` (in the project root) and handing it out here.

**20 — Close.** “Accounts out. We’re setting up the team right now.” Roll straight into `github-org-setup.md` while energy is high. Final Jeff callback.

---

## If you’re running long
Cut slides 11 and 14 (fold their points into 12 and 13). Never cut the demo — it’s the part they’ll remember.

## If they’re bored / restless
Jump to the demo immediately. Seeing code change itself on screen re-hooks them, then you can circle back to the “why.”
