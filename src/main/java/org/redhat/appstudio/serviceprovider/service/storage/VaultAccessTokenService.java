/*
 * Copyright (C) 2021 Red Hat, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *         http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redhat.appstudio.serviceprovider.service.storage;

import io.quarkus.vault.VaultKVSecretEngine;
import io.quarkus.vault.runtime.client.VaultClientException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;

/** Vault-based implementation of tokens storage. */
public class VaultAccessTokenService implements AccessTokenService {

  private static final Logger LOG = Logger.getLogger(VaultAccessTokenService.class);

  private final VaultKVSecretEngine kvSecretEngine;

  private final String vaultPath = "spi/accesstokens";

  public VaultAccessTokenService(VaultKVSecretEngine kvSecretEngine) {
    this.kvSecretEngine = kvSecretEngine;
  }

  public Set<AccessToken> fetchAll() {
    LOG.debug("fetchAll");
    return kvSecretEngine.listSecrets(vaultPath).stream()
        .map(s -> AccessToken.fromKVMap(kvSecretEngine.readSecret(vaultPath + "/" + s)))
        .collect(Collectors.toSet());
  }

  public Optional<AccessToken> get(String name) {
    LOG.debug("Get by name:" + name);
    try {
      return Optional.of(AccessToken.fromKVMap(kvSecretEngine.readSecret(vaultPath + "/" + name)));
    } catch (VaultClientException e) {
      return Optional.empty();
    }
  }

  public AccessToken create(AccessToken accessToken) {
    LOG.debug("Create token: " + accessToken.toString());
    kvSecretEngine.writeSecret(vaultPath + "/" + accessToken.getName(), accessToken.asKVMap());
    return accessToken;
  }

  public void update(String name, AccessToken accessToken) {
    LOG.debug("Update token: " + accessToken.toString());
    kvSecretEngine.writeSecret(vaultPath + "/" + name, accessToken.asKVMap());
  }

  public void delete(String name) {
    LOG.debug("Delete token: " + name);
    kvSecretEngine.deleteSecret(vaultPath + "/" + name);
  }
}
