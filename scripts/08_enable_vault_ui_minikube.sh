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
echo "Enable Vault UI on minikube"
kubectl patch svc vault -n vault --patch \
  '{"spec": { "type": "NodePort", "ports": [ { "nodePort": 31442, "port": 8200, "protocol": "TCP", "targetPort": 8200 } ] } }'
VAULT_URL=$(minikube service  vault  --url -n vault)
echo "Vault UI: "$VAULT_URL
