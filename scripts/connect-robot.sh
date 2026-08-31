#!/bin/bash
#
# connect-robot.sh — open the robot's Program & Manage console over USB-C.
#
# Plug the Control Hub into the laptop with a USB-C cable, turn the robot on,
# run this script. It tunnels the hub's web console to your own computer, so
# your laptop keeps its normal internet the whole time (no joining robot WiFi).
#
# Usage:
#   ./scripts/connect-robot.sh              # connect and open the console
#   ./scripts/connect-robot.sh --no-open    # connect but don't open a browser
#   ./scripts/connect-robot.sh --stop       # tear the tunnel down
#   ./scripts/connect-robot.sh --port 8081  # use a different local port
#
# Background and other options (WiFi dongle, REV Hardware Client for Windows):
# see docs/robot-networking.md.

set -uo pipefail

# The hub always serves its console on 8080; only the laptop-side port varies.
HUB_PORT=8080
LOCAL_PORT=8080
OPEN_BROWSER=true
STOP_ONLY=false

# How long to wait for the hub to show up before giving troubleshooting advice.
WAIT_SECONDS=20

# --- pretty output ------------------------------------------------------------
# Colors only when writing to a real terminal, so piped output stays clean.
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
    --no-open) OPEN_BROWSER=false ;;
    --stop)    STOP_ONLY=true ;;
    --port)
      [ $# -ge 2 ] || die "--port needs a number, e.g. --port 8081"
      LOCAL_PORT="$2"
      shift
      ;;
    -h|--help)
      # Print this file's header comment as the help text, stopping at the first
      # line that isn't a comment.
      awk 'NR > 2 { if ($0 !~ /^#/) exit; sub(/^# ?/, ""); print }' "$0"
      exit 0
      ;;
    *) die "Unknown option: $1 (try --help)" ;;
  esac
  shift
done

case "$LOCAL_PORT" in
  ''|*[!0-9]*) die "Port must be a number, got: $LOCAL_PORT" ;;
esac

# --- find adb -----------------------------------------------------------------
# adb may not be on PATH yet in a fresh shell after `brew install`, so check the
# usual install locations too before telling the student to install anything.
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
  (that's the --cask, not a plain 'brew install'). Then run this script again."

# --- stop mode ----------------------------------------------------------------
if [ "$STOP_ONLY" = true ]; then
  "$ADB" forward --remove "tcp:$LOCAL_PORT" 2>/dev/null
  ok "Tunnel on port $LOCAL_PORT closed. (The robot itself is untouched.)"
  exit 0
fi

# --- find the Control Hub -----------------------------------------------------
# `adb devices -l` prints one line per device; the Control Hub identifies itself
# with model:Control_Hub_v1_0, which lets us pick it out from phones or tablets
# that happen to be plugged in.
hub_serial() {
  "$ADB" devices -l 2>/dev/null \
    | awk '$2 == "device" && /Control_Hub/ { print $1; exit }'
}

# Any authorized device, used as a fallback so a newer hub model still works.
any_serial() {
  "$ADB" devices -l 2>/dev/null | awk '$2 == "device" { print $1; exit }'
}

say "${BOLD}Looking for the Control Hub over USB…${RESET}"

SERIAL=""
waited=0
while [ "$waited" -lt "$WAIT_SECONDS" ]; do
  SERIAL="$(hub_serial)"
  [ -n "$SERIAL" ] && break

  # A device stuck in "unauthorized" or "offline" won't accept a forward, and no
  # amount of waiting fixes it — surface it instead of spinning silently.
  state_line="$("$ADB" devices 2>/dev/null | awk 'NR > 1 && NF == 2 && $2 != "device" { print; exit }')"
  if [ -n "$state_line" ]; then
    warn "A device is connected but not ready: $state_line"
    break
  fi

  sleep 1
  waited=$((waited + 1))
done

if [ -z "$SERIAL" ]; then
  SERIAL="$(any_serial)"
  if [ -n "$SERIAL" ]; then
    warn "Found a device that doesn't report itself as a Control Hub; trying it anyway."
  fi
fi

if [ -z "$SERIAL" ]; then
  # Restart the adb server once: it sometimes holds a stale view of USB after a
  # cable was unplugged mid-session.
  warn "No hub yet — restarting adb and trying once more…"
  "$ADB" kill-server >/dev/null 2>&1
  "$ADB" start-server >/dev/null 2>&1
  sleep 2
  SERIAL="$(hub_serial)"
  [ -n "$SERIAL" ] || SERIAL="$(any_serial)"
fi

if [ -z "$SERIAL" ]; then
  say ""
  die "Still can't see the Control Hub. Check these, in order:

  1. Is the robot ON? The hub's computer runs off the 12V battery, NOT off the
     USB cable. No battery, no connection.
  2. Is the USB-C cable a REAL data cable, pushed all the way in? The hub's
     socket is recessed, so a normal cable can look plugged in without actually
     touching the data pins. Use the cable REV shipped with the hub.
  3. Try the other USB-C port on the laptop, or a different cable.

  Then run this script again. More help: docs/robot-networking.md"
fi

ok "Found the hub: $SERIAL"

# --- set up the tunnel --------------------------------------------------------
# Remove any tunnel we left behind on this port first, so re-running the script
# after unplugging the cable doesn't fail on a stale forward.
"$ADB" -s "$SERIAL" forward --remove "tcp:$LOCAL_PORT" 2>/dev/null

if ! "$ADB" -s "$SERIAL" forward "tcp:$LOCAL_PORT" "tcp:$HUB_PORT" >/dev/null; then
  die "Couldn't open the tunnel on port $LOCAL_PORT. Something else may be using
  that port — try:  ./scripts/connect-robot.sh --port 8081"
fi

URL="http://localhost:$LOCAL_PORT"
ok "Tunnel open: $URL  →  robot's port $HUB_PORT"

# --- confirm the console actually answers -------------------------------------
# A forward can succeed while the Robot Controller app is still booting, so poll
# the console rather than sending the student to a blank page.
if command -v curl >/dev/null 2>&1; then
  tries=0
  until curl -fsS --max-time 2 -o /dev/null "$URL"; do
    tries=$((tries + 1))
    if [ "$tries" -ge 8 ]; then
      warn "The tunnel is open but the console isn't answering yet."
      warn "The robot app may still be starting up — wait a few seconds, then reload $URL."
      break
    fi
    sleep 2
  done
  [ "$tries" -lt 8 ] && ok "Console is responding."
fi

# --- open the browser ---------------------------------------------------------
if [ "$OPEN_BROWSER" = true ] && command -v open >/dev/null 2>&1; then
  open "$URL"
  say ""
  say "${BOLD}Opening $URL in your browser.${RESET}"
else
  say ""
  say "${BOLD}Open this in your browser: $URL${RESET}"
fi

say "Your laptop still has normal internet on WiFi — the robot is on the cable."
say "When you're done:  ./scripts/connect-robot.sh --stop"
