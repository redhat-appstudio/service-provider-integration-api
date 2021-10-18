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

import io.quarkus.vault.runtime.VaultKvManager;
import io.quarkus.vault.runtime.client.VaultClientException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.jboss.logging.Logger;

/** Vault-based implementation of tokens storage. */
public class VaultAccessTokenService implements AccessTokenService {

  private static final Logger LOG = Logger.getLogger(VaultAccessTokenService.class);

  private final VaultKvManager vaultKvManager;

  private final VaultKVMapHelper kvMapHelper = new VaultKVMapHelper();

  private final String vaultPath = "spi/accesstokens";

  public VaultAccessTokenService(VaultKvManager vaultKvManager) {
    this.vaultKvManager = vaultKvManager;
  }

  public Set<AccessToken> fetchAll() {
    LOG.debug("fetchAll");
    try {
      Set<AccessToken> set = new HashSet<>();
      for (String s : vaultKvManager.listSecrets(vaultPath)) {
        this.get(s).ifPresent(set::add);
      }
      return set;
    } catch (VaultClientException e) {
      return Collections.emptySet();
    }
  }

  public Optional<AccessToken> get(String name) {
    LOG.debug("Get by name:" + name);
    try {
      return Optional.of(kvMapHelper.fromKVMap(vaultKvManager.readSecret(vaultPath + "/" + name)));
    } catch (VaultClientException e) {
      if (e.getStatus() == 404) {
        return Optional.empty();
      } else {
        throw e;
      }
    }
  }

  public AccessToken create(AccessToken accessToken) {
    LOG.debug("Create token: " + accessToken.toString());
    vaultKvManager.writeSecret(
        vaultPath + "/" + accessToken.getName(), kvMapHelper.asKVMap(accessToken));
    return accessToken;
  }

  public void update(String name, AccessToken accessToken) {
    LOG.debug("Update token: " + accessToken.toString());
    vaultKvManager.writeSecret(vaultPath + "/" + name, kvMapHelper.asKVMap(accessToken));
  }

  public void delete(String name) {
    LOG.debug("Delete token: " + name);
    vaultKvManager.deleteSecret(vaultPath + "/" + name);
  }
}
