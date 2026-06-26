#!/usr/bin/env sh
set -eu

APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd -P)

JAVA_MAJOR=$(java -version 2>&1 | sed -n 's/.*version "\([0-9][0-9]*\).*/\1/p' | head -n 1)
if [ -z "${JAVA_HOME:-}" ] || [ "${JAVA_MAJOR:-0}" -gt 21 ]; then
  for candidate in \
    "$HOME/.local/share/mise/installs/java/17.0.2" \
    "$HOME/.local/share/mise/installs/java/21.0.2" \
    "/usr/lib/jvm/temurin-17-jdk-amd64" \
    "/usr/lib/jvm/java-17-openjdk-amd64"; do
    if [ -x "$candidate/bin/java" ]; then
      export JAVA_HOME="$candidate"
      export PATH="$JAVA_HOME/bin:$PATH"
      break
    fi
  done
fi

if ! command -v gradle >/dev/null 2>&1; then
  echo "ERROR: No se encontró el comando 'gradle'. Instala Gradle o ejecuta desde un entorno con setup-gradle." >&2
  exit 127
fi

cd "$APP_HOME"
exec gradle "$@"
