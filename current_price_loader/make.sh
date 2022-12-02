#!/bin/bash

export $(grep -vE "^(#.*|\s*)$" .env)

build_and_push() {
	epoch_ms=$(date +%s%N)
	echo "Compiling..."
	mvn clean compile dependency:copy-dependencies -DincludeScope=runtime
	echo "Old images are removing..."
	docker image rm -f current_price_loader current_price_receiver
	docker image rm "$(docker images "$ECR_CURRENT_PRICE_LOADER_REPO_URI" -a -q)"
	docker image rm "$(docker images "$ECR_CURRENT_PRICE_RECEIVER_REPO_URI" -a -q)"
	echo "New image is building..."
	docker build -t current_price_loader -f current_price_loader.Dockerfile .
	docker build -t current_price_receiver -f current_price_receiver.Dockerfile .
	echo "ECR tags are tagging..."
	docker tag current_price_loader:latest "$ECR_CURRENT_PRICE_LOADER_REPO_URI":latest
	docker tag current_price_loader:latest "$ECR_CURRENT_PRICE_LOADER_REPO_URI":"$epoch_ms"
	docker tag current_price_receiver:latest "$ECR_CURRENT_PRICE_RECEIVER_REPO_URI":latest
	docker tag current_price_receiver:latest "$ECR_CURRENT_PRICE_RECEIVER_REPO_URI":"$epoch_ms"
	echo "ECR images are pushing..."
	docker push "$ECR_CURRENT_PRICE_LOADER_REPO_URI":latest
	docker push "$ECR_CURRENT_PRICE_LOADER_REPO_URI":"$epoch_ms"
	docker push "$ECR_CURRENT_PRICE_RECEIVER_REPO_URI":latest
	docker push "$ECR_CURRENT_PRICE_RECEIVER_REPO_URI":"$epoch_ms"
}

run_current_price_loader() {
	docker run --rm -p 9000:8080 -e AWS_ACCESS_KEY_ID="$AWS_ACCESS_KEY_ID" -e AWS_SECRET_ACCESS_KEY="$AWS_SECRET_ACCESS_KEY" current_price_loader
}

run_current_price_receiver() {
	docker run --rm -p 9000:8080 -e AWS_ACCESS_KEY_ID="$AWS_ACCESS_KEY_ID" -e AWS_SECRET_ACCESS_KEY="$AWS_SECRET_ACCESS_KEY" current_price_receiver
}

clean_up() {
	echo "Images are being removed..."
	docker image rm -f current_price_loader current_price_receiver
	docker rmi $(docker images | grep "$ECR_CURRENT_PRICE_LOADER_REPO_URI")
	docker rmi $(docker images | grep "$ECR_CURRENT_PRICE_RECEIVER_REPO_URI")
	echo "Cleaned up!"
}

if false; then
	echo
elif [ "$1" = "build_and_push" ]; then
	build_and_push ${@:2}
elif [ "$1" = "run_current_price_loader" ]; then
	run_current_price_loader ${@:2}
elif [ "$1" = "run_current_price_receiver" ]; then
	run_current_price_receiver ${@:2}
elif [ "$1" = "clean_up" ]; then
	clean_up ${@:2}
else
	echo "Usage: bash make.sh [OPTIONS]"
	echo
	echo -e "\033[1;4;32m""Options:""\033[0;34m"
	compgen -A function
	echo
	echo -e "\033[0m"
fi
