#!/usr/bin/env bash
# Add the Maven plugin (community)
asdf plugin add maven https://github.com/halcyon/asdf-maven.git

# Install Maven 3.9.11
asdf install maven 3.9.11

# Set as the user-wide (global) default in ~/.tool-versions
# Use the short flag shown in your help (-u). (Long form is --home.)
asdf set -u maven 3.9.11
# asdf set --home maven 3.9.11   # equivalent long form

# Check what asdf thinks is active
asdf current maven

# Verify Maven itself
mvn -v