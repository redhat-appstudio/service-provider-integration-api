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
echo "Logging in vault"
while [ "$(kubectl get pods -l app.kubernetes.io/name=vault -n vault -o jsonpath='{.items[*].status.phase}')" != "Running" ]; do
   sleep 5
   echo "Waiting for vault to be ready."
done

kubectl exec -ti vault-0 -n vault -- sh -c 'vault login $(grep -h '"'"'Initial Root Token'"'"' /tmp/keys.txt | awk '"'"'{print $NF}'"'"')'
kubectl exec -ti vault-0 -n vault -- sh -c 'vault audit enable file file_path=/vault/logs/$(date '"'"'+%Y%m%d%H%M.%S'"'"').log.json'
kubectl exec -ti vault-0 -n vault -- vault status
echo "login complete"
