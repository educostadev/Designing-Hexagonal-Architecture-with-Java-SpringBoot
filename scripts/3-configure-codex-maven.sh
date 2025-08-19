#!/bin/bash
# Configure Maven on OpenAI codex to use local repository and proxy to download dependencies
set -e

echo "[INFO] Installing..."
apt update && apt install -y curl unzip

echo "[INFO] Setting up Maven to use local repository only..."
mkdir -p /root/.m2

cat <<EOF > /root/.m2/settings.xml
<settings>
  <mirrors>
    <mirror>
      <id>local-repo</id>
      <mirrorOf>*</mirrorOf>
      <url>file:///root/.m2/repository</url>
    </mirror>
  </mirrors>
  <proxies>
    <proxy>
      <id>codexProxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>proxy</host>
      <port>8080</port>
    </proxy>
  </proxies>
</settings>
EOF

# TESTING...
echo "[INFO] Downloading Maven plugin and dependencies..."
BASE_URL="https://repo1.maven.org/maven2"
M2_REPO="/root/.m2/repository"

# Utility function
download_artifact() {
  GROUP_ID="$1"
  ARTIFACT_ID="$2"
  VERSION="$3"

  GROUP_PATH=$(echo "$GROUP_ID" | tr '.' '/')
  TARGET_DIR="$M2_REPO/$GROUP_PATH/$ARTIFACT_ID/$VERSION"
  ARTIFACT_URL="$BASE_URL/$GROUP_PATH/$ARTIFACT_ID/$VERSION"

  mkdir -p "$TARGET_DIR"
  cd "$TARGET_DIR"

  for ext in pom jar; do
    FILE="$ARTIFACT_ID-$VERSION.$ext"
    if [ ! -f "$FILE" ]; then
      echo "[INFO] Downloading $FILE"
      curl -s -O "$ARTIFACT_URL/$FILE"
      touch "$FILE.lastUpdated"
    fi
  done
}

#  This is test on how download a dependency
download_artifact "org.apache.maven.plugins" "maven-plugin-plugin" "3.6.0"

echo "[INFO] Preloading done. You can run maven now in offline mode: mvn clean install --offline"

