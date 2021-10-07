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

import io.quarkus.runtime.configuration.ConfigurationException;
import io.quarkus.vault.runtime.VaultKvManager;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/** Produces token service instance depending of configuration. */
@ApplicationScoped
public class TokenServiceProducer {

  private final String backendType;
  private final VaultKvManager vaultKvManager;

  @Inject
  public TokenServiceProducer(
      VaultKvManager vaultKvManager,
      @ConfigProperty(name = "spi.backend.type") String backendType) {
    this.vaultKvManager = vaultKvManager;
    this.backendType = backendType;
  }

  @Produces
  @ApplicationScoped
  public AccessTokenService produceAccessTokenService() {
    if (backendType.equals("vault")) {
      return new VaultAccessTokenService(vaultKvManager);
    } else if (backendType.equals("inmemory")) {
      return new InmemoryAccessTokenService();
    } else {
      throw new ConfigurationException(
          "Unknown or empty backend provider. Check 'spi.backend.type' property.");
    }
  }
}
