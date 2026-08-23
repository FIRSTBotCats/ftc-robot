# 🐈 BotCats — Team Coding Conventions
1. **Pull before you start.** Always grab the team’s latest code first.
2. **Name things like a human.** A name should say what it *is*.
3. **Comment the *why*, not the *what*.**
4. **Small commits, good messages.** One change, test it, save it.
5. **Read all AI-generated code before you trust it.** You’re the human in charge.
6. **No mystery files.** `So_cool.java` is unclear. 🙅
---
### Naming: bad vs good
| ✗ Villain | ✓ Hero | Why |
|---|---|---|
| `int x` | `int armSpeed` | Tells you what it holds |
| `double d` | `double distanceCM` | Tells you the units too |
| `thingy()` | `stopArmAtTop()` | Tells you what it does |
| `My Code.java` | `ArmController.java` | What’s inside (no spaces!) |

Guidelines: no single letters (except loop counters like `i`, or geometry: x,y,z), say what it *is* or *does*.

---
### Comments: bad vs good
```java
// ✗ Useless — just repeats the code
armSpeed = 5;   // set armSpeed to 5

// ✓ Useful — explains the reason a human wouldn't guess
armSpeed = 5;   // faster than 5 tips the robot over
```
Good comment = the thing you’d *say out loud* to a teammate reading over your shoulder.

---
### Commit messages: bad vs good
| ✗ Bad | ✓ Good |
|---|---|
| `stuff` | `Slow down arm motor` |
| `asdf` | `Fix autonomous turn angle` |
| `fixed it` | `Stop arm at top so it won't over-extend` |

Finish this sentence: “If applied, this commit will ___.”

---
### The Team Loop
**⬇️ Pull → ✏️ Edit → 💾 Commit → ⬆️ Push**
- **Pull** = get teammates’ latest changes.
- **Push** = share yours with the team.
- **Branch** = a safe sandbox to try wild ideas without breaking the working robot.
---
### Working with Claude (AI as a teammate)
- 🎯 **Be specific.** “Make the arm stop at the top” is better than “fix it.”
- 🔍 **Read what it writes.** Does it make sense?
- 🧪 **Test on the robot** before you trust it.
- ❓ **Ask “why did you do that?”** if you don’t understand.
- 🧑‍✈️ **You’re in charge.** Claude is the intern. A grown-up is signed in.