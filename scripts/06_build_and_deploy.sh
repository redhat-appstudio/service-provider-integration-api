#!/bin/bash
#
# Copyright (C) 2021 Red Hat, Inc.
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#         http://www.apache.org/licenses/LICENSE-2.0
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -e
echo "Connecting docker to minikube"
eval $(minikube docker-env)

echo "Building Service Integration API"
mvn package -Dquarkus.container-image.build=true  -DskipTests -Dskip-validate-sources

echo "Deploying to vault namespace"
#kubectl apply -f src/main/kubernetes/app-auth.yaml -n vault
kubectl apply -f src/main/kubernetes/auth-delegator.yaml -n vault
kubectl apply -f target/kubernetes/minikube.yml -n vault

kubectl rollout status deployment/service-provider-integration-api  -n vault
echo "Unset docker connection to minikube"
eval "$(minikube docker-env -u)"
