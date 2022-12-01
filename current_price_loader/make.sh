#!/bin/bash

export $(grep -vE "^(#.*|\s*)$" .env)

build_and_push() {
  epoch_ms=$(date +%s%N)
  echo "Compiling..."
  mvn clean compile dependency:copy-dependencies -DincludeScope=runtime
  echo "Old image is removing..."
  docker image rm -f current_price_loader $ECR_REPO_URI:latest
  echo "New image is building..."
  docker build -t current_price_loader .
  echo "ECR tags are tagging..."
  docker tag current_price_loader:latest $ECR_REPO_URI:latest
  docker tag current_price_loader:latest $ECR_REPO_URI:$epoch_ms
  echo "ECR images are pushing..."
  docker push $ECR_REPO_URI:latest
  docker push $ECR_REPO_URI:$epoch_ms
}

if false; then
  echo
elif [ "$1" = "build_and_push" ]; then
  build_and_push ${@:2}
else
  echo "Usage: bash make.sh [OPTIONS]"
  echo
  echo -e "\033[1;4;32m""Options:""\033[0;34m"
  compgen -A function
  echo
  echo -e "\033[0m"
fi
