#!/bin/bash
#
# backup-hub.sh — copy ALL the robot's code off the Control Hub and save it to GitHub.
#
# Plug the Control Hub into the laptop with a USB-C cable, turn the robot on,
# run this script. It copies every Blocks program and every OnBotJava file from
# the robot into hub_backup/, commits that on a new branch, pushes it, and opens
# a pull request. Your own checkout and your own branch are never touched.
#
# Run it at the end of every meeting, and before leaving any event.
#
# Usage:
#   ./scripts/backup-hub.sh              # back up, push, and open a pull request
#   ./scripts/backup-hub.sh --no-push    # no internet? save on this laptop only,
#                                        #   then push later (the script tells you how)
#   ./scripts/backup-hub.sh --no-pr      # push the branch but don't open a PR
#
# Why this matters: see docs/why-we-back-up.md

set -uo pipefail

# Where the Robot Controller app keeps the code on the hub.
HUB_BLOCKS_DIR=/sdcard/FIRST/blocks
HUB_JAVA_DIR=/sdcard/FIRST/java/src

# Where the copy goes in this repo. The script REPLACES this folder each time, so
# it is always an exact mirror of the robot (files deleted on the robot are
# deleted here too; git history still has the old versions).
BACKUP_DIR=hub_backup

PUSH=true
OPEN_PR=true

# --- pretty output (same style as connect-robot.sh) ---------------------------
if [ -t 1 ]; then
  BOLD=$'\033[1m'; GREEN=$'\033[32m'; YELLOW=$'\033[33m'; RED=$'\033[31m'; RESET=$'\033[0m'
else
  BOLD=""; GREEN=""; YELLOW=""; RED=""; RESET=""
fi

say()  { printf '%s\n' "$*"; }
ok()   { printf '%s✓%s %s\n' "$GREEN" "$RESET" "$*"; }
warn() { printf '%s!%s %s\n' "$YELLOW" "$RESET" "$*"; }
die()  { printf '%s✗ %s%s\n' "$RED" "$*" "$RESET" >&2; exit 1; }

# --- arguments ----------------------------------------------------------------
while [ $# -gt 0 ]; do
  case "$1" in
    --no-push) PUSH=false; OPEN_PR=false ;;
    --no-pr)   OPEN_PR=false ;;
    -h|--help)
      awk 'NR > 2 { if ($0 !~ /^#/) exit; sub(/^# ?/, ""); print }' "$0"
      exit 0
      ;;
    *) die "Unknown option: $1 (try --help)" ;;
  esac
  shift
done

# --- find the repo --------------------------------------------------------------
REPO="$(git -C "$(dirname "$0")" rev-parse --show-toplevel 2>/dev/null)" \
  || die "This script has to live inside the ftc-robot git repo."

# --- team rule: no last names in commits ----------------------------------------
# The commit made below is signed with your git name. Warn (don't block) if it
# looks like a full last name, since this repo is public and has student info rules.
GIT_NAME="$(git -C "$REPO" config user.name || true)"
[ -n "$GIT_NAME" ] || die "Git doesn't know your name yet. Set it (first name + last initial):
    git config user.name \"Jane D.\""
if printf '%s' "$GIT_NAME" | grep -Eq '^[^ ]+ +[^ .]{2,}'; then
  warn "Your git name is \"$GIT_NAME\". Team rule: no last names on GitHub."
  warn "Fix it for this repo (use your last INITIAL):  git config user.name \"${GIT_NAME%% *} X.\""
fi

# --- find adb (same search as connect-robot.sh) ----------------------------------
find_adb() {
  if command -v adb >/dev/null 2>&1; then
    command -v adb
    return 0
  fi
  local candidate
  for candidate in \
    /opt/homebrew/bin/adb \
    /usr/local/bin/adb \
    "${ANDROID_HOME:-}/platform-tools/adb" \
    "${ANDROID_SDK_ROOT:-}/platform-tools/adb" \
    "$HOME/Library/Android/sdk/platform-tools/adb"
  do
    if [ -x "$candidate" ]; then
      printf '%s\n' "$candidate"
      return 0
    fi
  done
  return 1
}

ADB="$(find_adb)" || die "Couldn't find adb. Install it with:
    brew install --cask android-platform-tools
  Then run this script again."

# --- find the Control Hub -------------------------------------------------------
hub_serial() {
  "$ADB" devices -l 2>/dev/null \
    | awk '$2 == "device" && /Control_Hub/ { print $1; exit }'
}
any_serial() {
  "$ADB" devices -l 2>/dev/null | awk '$2 == "device" { print $1; exit }'
}

say "${BOLD}Looking for the Control Hub over USB…${RESET}"
SERIAL=""
for _ in 1 2 3 4 5 6 7 8 9 10; do
  SERIAL="$(hub_serial)"
  [ -n "$SERIAL" ] && break
  sleep 1
done
[ -n "$SERIAL" ] || SERIAL="$(any_serial)"
[ -n "$SERIAL" ] || die "Can't see the Control Hub. Is the robot ON (12V battery), and is the
  USB-C data cable pushed all the way in? See docs/robot-networking.md, or try
  ./scripts/connect-robot.sh first — it has more troubleshooting help."
ok "Found the hub: $SERIAL"

hub() { "$ADB" -s "$SERIAL" "$@"; }
hub_has_dir() { [ "$(hub shell "[ -d '$1' ] && echo yes" 2>/dev/null | tr -d '\r')" = "yes" ]; }

# --- copy the code off the robot --------------------------------------------------
STAGE="$(mktemp -d "${TMPDIR:-/tmp}/hub-backup.XXXXXX")"
WORKTREE=""
BRANCH=""
COMMITTED=false
cleanup() {
  if [ -n "$WORKTREE" ] && [ -d "$WORKTREE" ]; then
    git -C "$REPO" worktree remove --force "$WORKTREE" >/dev/null 2>&1
  fi
  # Don't leave an empty branch behind if we stopped before committing.
  if [ -n "$BRANCH" ] && [ "$COMMITTED" = false ]; then
    git -C "$REPO" branch -q -D "$BRANCH" >/dev/null 2>&1
  fi
  rm -rf "$STAGE"
}
trap cleanup EXIT

mkdir -p "$STAGE/pull" "$STAGE/mirror/blocks" "$STAGE/mirror/onbotjava"

if hub_has_dir "$HUB_BLOCKS_DIR"; then
  hub pull "$HUB_BLOCKS_DIR" "$STAGE/pull/" >/dev/null \
    || die "Copying the Blocks programs off the robot failed. Check the cable and try again."
  # Keep only the .blk files: the hub also stores generated .js next to them.
  find "$STAGE/pull/blocks" -maxdepth 1 -type f -name '*.blk' -exec cp -p {} "$STAGE/mirror/blocks/" \;
else
  warn "No Blocks folder on the robot ($HUB_BLOCKS_DIR)."
fi

if hub_has_dir "$HUB_JAVA_DIR"; then
  hub pull "$HUB_JAVA_DIR" "$STAGE/pull/" >/dev/null \
    || die "Copying the OnBotJava files off the robot failed. Check the cable and try again."
  cp -Rp "$STAGE/pull/src/." "$STAGE/mirror/onbotjava/"
else
  warn "No OnBotJava folder on the robot ($HUB_JAVA_DIR)."
fi

BLK_COUNT="$(find "$STAGE/mirror/blocks" -type f | wc -l | tr -d ' ')"
JAVA_COUNT="$(find "$STAGE/mirror/onbotjava" -type f -name '*.java' | wc -l | tr -d ' ')"

# A robot with no code at all is almost certainly freshly reset or the wrong
# device. Stop rather than make a backup that "deletes" everything.
if [ "$BLK_COUNT" -eq 0 ] && [ "$JAVA_COUNT" -eq 0 ]; then
  die "Found no code at all on this device. If the hub was just reset, DON'T
  back it up — the last good backup is still on GitHub in $BACKUP_DIR/."
fi
ok "Copied $BLK_COUNT Blocks programs and $JAVA_COUNT Java files off the robot."

RC_VERSION="$(hub shell dumpsys package com.qualcomm.ftcrobotcontroller 2>/dev/null \
  | tr -d '\r' | awk -F= '/versionName=/ { print $2; exit }')"

cat > "$STAGE/mirror/README.md" <<'EOF'
# Robot code backup (made by scripts/backup-hub.sh)

This folder is an exact copy of the code that was on the Control Hub when
`scripts/backup-hub.sh` last ran. Don't edit files here — the script replaces
this whole folder every time.

- `blocks/` — Blocks programs (`/sdcard/FIRST/blocks/` on the robot)
- `onbotjava/` — OnBotJava source (`/sdcard/FIRST/java/src/` on the robot)

To see what changed on the robot between two backups, look at this folder's
history on GitHub. To put an old version back on the robot, download the file
and upload it in the Blocks / OnBotJava editor.
EOF

# --- commit on a new branch, without touching the student's checkout ---------------
# Base the backup on the latest main from GitHub if we can reach it, so the PR
# only shows what changed on the robot. With no internet, use what we have.
if git -C "$REPO" fetch -q origin main 2>/dev/null; then
  BASE=origin/main
else
  warn "Can't reach GitHub — basing the backup on this laptop's copy of main."
  if git -C "$REPO" rev-parse -q --verify origin/main >/dev/null; then BASE=origin/main; else BASE=main; fi
fi

STAMP="$(date '+%Y-%m-%d-%H%M')"
BRANCH="hub-backup-$STAMP"
# Two backups in the same minute: add -2, -3, … instead of failing.
n=2
while git -C "$REPO" rev-parse -q --verify "refs/heads/$BRANCH" >/dev/null; do
  BRANCH="hub-backup-$STAMP-$n"
  n=$((n + 1))
done
WORKTREE="$STAGE/worktree"
if ! git -C "$REPO" worktree add -q -b "$BRANCH" "$WORKTREE" "$BASE"; then
  BRANCH=""  # never created, so nothing of ours to delete
  die "Couldn't create a backup branch."
fi

rm -rf "${WORKTREE:?}/$BACKUP_DIR"
cp -Rp "$STAGE/mirror" "$WORKTREE/$BACKUP_DIR"
git -C "$WORKTREE" add -A "$BACKUP_DIR"

if git -C "$WORKTREE" diff --cached --quiet; then
  ok "Nothing changed on the robot since the last backup on main. You're all set!"
  exit 0
fi

# Describe the changes in plain words, for the commit, the PR, and the screen.
CHANGES="$(git -C "$WORKTREE" diff --cached --name-status -- "$BACKUP_DIR" \
  | grep -v "$BACKUP_DIR/README.md" \
  | awk -F'\t' -v dir="$BACKUP_DIR/" '
      { f = $NF; sub(dir, "", f) }
      $1 == "A"    { print "- new on robot: " f; next }
      $1 == "M"    { print "- changed:      " f; next }
      $1 == "D"    { print "- deleted from robot: " f; next }
      $1 ~ /^R/    { print "- renamed:      " f; next }
                   { print "- " $1 ": " f }')"
[ -n "$CHANGES" ] || CHANGES="- (first backup into $BACKUP_DIR/)"

TITLE="Back up robot code from the Control Hub ($STAMP)"
BODY="Copied off the Control Hub by scripts/backup-hub.sh.

Blocks programs: $BLK_COUNT, OnBotJava files: $JAVA_COUNT
Robot Controller app version: ${RC_VERSION:-unknown}

What changed on the robot since the last backup on main:
$CHANGES"

git -C "$WORKTREE" commit -q -m "$TITLE" -m "$BODY" || die "git commit failed."
COMMITTED=true
ok "Saved on this laptop as branch $BRANCH."
say ""
say "$CHANGES"
say ""

# --- push and open the pull request ---------------------------------------------
if [ "$PUSH" = false ]; then
  warn "Not pushed yet. The code is safe on THIS laptop only."
  say "  When you have internet, run:  ${BOLD}git push -u origin $BRANCH${RESET}"
  say "  Then open a pull request on GitHub."
  exit 0
fi

if ! git -C "$WORKTREE" push -q -u origin "$BRANCH"; then
  warn "Couldn't push to GitHub (no internet?). The code is safe on THIS laptop only."
  say "  When you have internet, run:  ${BOLD}git push -u origin $BRANCH${RESET}"
  exit 1
fi
ok "Pushed to GitHub."

if [ "$OPEN_PR" = true ] && command -v gh >/dev/null 2>&1 && gh auth status >/dev/null 2>&1; then
  if PR_URL="$(cd "$WORKTREE" && gh pr create --base main --head "$BRANCH" \
                 --title "$TITLE" --body "$BODY" 2>/dev/null)"; then
    ok "Pull request opened: $PR_URL"
    say "Ask a teammate to look it over and merge it."
    exit 0
  fi
  warn "Couldn't open the pull request automatically."
fi

REMOTE_URL="$(git -C "$REPO" remote get-url origin | sed -E 's#^git@github.com:#https://github.com/#; s#\.git$##')"
say "Open the pull request here:  ${BOLD}$REMOTE_URL/compare/main...$BRANCH?expand=1${RESET}"
