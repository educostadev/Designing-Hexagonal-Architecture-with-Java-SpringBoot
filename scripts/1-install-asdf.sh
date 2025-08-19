#!/usr/bin/env bash
# System-wide, non-interactive installer for asdf v0.18.0 on Debian/Ubuntu.
# - Installs prerequisites
# - Downloads the requested amd64 tarball via curl
# - Installs under /opt/asdf and wires profile & completions
# - Symlinks /usr/local/bin/asdf
#
# References:
# - asdf getting started & init: https://asdf-vm.com/guide/getting-started.html
# - Bash exit/ERR behavior: GNU Bash manual & common patterns

set -Eeuo pipefail

# Print a clear error and exit non-zero on any failure (works with functions/subshells thanks to -E)
trap 'rc=$?; echo "❌ Error (exit $rc) at line $LINENO" >&2; exit $rc' ERR

readonly ASDF_VERSION="v0.18.0"
readonly ASDF_URL="https://github.com/asdf-vm/asdf/releases/download/${ASDF_VERSION}/asdf-${ASDF_VERSION}-linux-amd64.tar.gz"
readonly INSTALL_DIR="/opt/asdf"
readonly BIN_SYMLINK="/usr/local/bin/asdf"
readonly PROFILE_SNIPPET="/etc/profile.d/asdf.sh"

# Root/sudo helper
if [ "${EUID:-$(id -u)}" -ne 0 ]; then
  SUDO="sudo"
else
  SUDO=""
fi

# Temp workspace with cleanup
TMPDIR="$(mktemp -d)"
cleanup() { rm -rf "$TMPDIR"; }
trap cleanup EXIT

require_linux_amd64() {
  if [[ "$(uname -s)" != "Linux" ]]; then
    echo "This installer targets Linux." >&2; exit 1
  fi
  case "$(uname -m)" in
    x86_64|amd64) ;; # ok
    *) echo "This tarball is for amd64/x86_64; got '$(uname -m)'." >&2; exit 1 ;;
  esac
}

apt_install_prereqs() {
  # Update first
  ${SUDO:+sudo} apt-get update -y

  # Ensure DEBIAN_FRONTEND reaches apt-get whether or not sudo is used.
  # (When using sudo, put the env var *inside* sudo.) :contentReference[oaicite:1]{index=1}
  if [ -n "${SUDO:-}" ]; then
    sudo env DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
      bash git curl ca-certificates tar coreutils
  else
    env DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
      bash git curl ca-certificates tar coreutils
  fi
}

download_and_extract() {
  local tarball="$TMPDIR/asdf.tgz" stage="$TMPDIR/stage"
  mkdir -p "$stage"

  echo "Downloading: $ASDF_URL"
  curl -fL --retry 3 --connect-timeout 15 -o "$tarball" "$ASDF_URL"

  tar -xzf "$tarball" -C "$stage"

  # Find root dir that contains a bin/ with asdf
  local extracted_root
  extracted_root="$(find "$stage" -maxdepth 2 -type f -name asdf -printf '%h\n' -quit || true)"
  if [[ -z "${extracted_root:-}" ]]; then
    echo "Could not locate 'asdf' executable in archive." >&2
    exit 1
  fi

  # Replace existing install atomically-ish
  $SUDO mkdir -p "$(dirname "$INSTALL_DIR")"
  if [[ -d "$INSTALL_DIR" ]]; then
    $SUDO rm -rf "${INSTALL_DIR}.bak" || true
    $SUDO mv "$INSTALL_DIR" "${INSTALL_DIR}.bak" || true
  fi
  $SUDO mkdir -p "$INSTALL_DIR"
  $SUDO cp -a "$extracted_root"/. "$INSTALL_DIR"/
  $SUDO chown -R root:root "$INSTALL_DIR"
}

symlink_binary() {
  local candidate=""
  if [[ -x "$INSTALL_DIR/bin/asdf" ]]; then
    candidate="$INSTALL_DIR/bin/asdf"
  elif [[ -x "$INSTALL_DIR/asdf" ]]; then
    candidate="$INSTALL_DIR/asdf"
  fi
  if [[ -z "$candidate" ]]; then
    echo "No runnable asdf binary under $INSTALL_DIR." >&2
    exit 1
  fi
  $SUDO ln -sf "$candidate" "$BIN_SYMLINK"
}

write_profile_snippet() {
  # Per asdf docs: ensure PATH (bin+shims), source asdf.sh; add completions. :contentReference[oaicite:2]{index=2}
  $SUDO tee "$PROFILE_SNIPPET" >/dev/null <<'EOF'
# asdf system-wide initialization
export ASDF_DIR="/opt/asdf"

# Put shims and bin early in PATH (idempotent)
case ":$PATH:" in
  *:"$ASDF_DIR/shims":*) ;;
  *) PATH="$ASDF_DIR/shims:$PATH" ;;
esac
case ":$PATH:" in
  *:"$ASDF_DIR/bin":*) ;;
  *) PATH="$ASDF_DIR/bin:$PATH" ;;
esac
export PATH

# Load asdf core (required)
if [ -r "$ASDF_DIR/asdf.sh" ]; then
  # shellcheck disable=SC1091
  . "$ASDF_DIR/asdf.sh"
fi

# Bash completions
if [ -n "${BASH_VERSION:-}" ] && [ -r "$ASDF_DIR/completions/asdf.bash" ]; then
  # shellcheck disable=SC1091
  . "$ASDF_DIR/completions/asdf.bash"
fi

# Zsh completions
if [ -n "${ZSH_VERSION:-}" ] && [ -d "$ASDF_DIR/completions" ]; then
  fpath=("$ASDF_DIR/completions" $fpath)
  autoload -Uz compinit 2>/dev/null || true
  compinit -i 2>/dev/null || true
fi
EOF
  $SUDO chmod 644 "$PROFILE_SNIPPET"
}

verify() {
  # Make asdf available in this process too
  export ASDF_DIR="$INSTALL_DIR"
  export PATH="$ASDF_DIR/shims:$ASDF_DIR/bin:$PATH"
  if [[ -r "$ASDF_DIR/asdf.sh" ]]; then . "$ASDF_DIR/asdf.sh"; fi

  if ! command -v asdf >/dev/null 2>&1; then
    echo "asdf not found on PATH after install." >&2
    exit 1
  fi
  asdf --version || true
}

main() {
  require_linux_amd64
  apt_install_prereqs
  download_and_extract
  symlink_binary
  write_profile_snippet
  verify
  echo "✅ asdf ${ASDF_VERSION} installed to $INSTALL_DIR"
  echo "➡️  Open a new shell or: source $PROFILE_SNIPPET"
}
main "$@"


# Be explicit for CI/containers: success exit code. :contentReference[oaicite:3]{index=3}
# exit 0
