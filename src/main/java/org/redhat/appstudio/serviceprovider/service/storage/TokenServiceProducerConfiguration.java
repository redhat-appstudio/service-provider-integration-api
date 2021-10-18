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

import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.vault.runtime.VaultKvManager;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

/** Produces token service instance depending of configuration. */
@Dependent
public class TokenServiceProducerConfiguration {

  @Produces
  @IfBuildProperty(name = "spi.backend.type", stringValue = "vault")
  public AccessTokenService vaultAccessTokenService(VaultKvManager vaultKvManager) {
    return new VaultAccessTokenService(vaultKvManager);
  }

  @Produces
  @DefaultBean
  public AccessTokenService inmemoryAccessTokenService() {
    return new InmemoryAccessTokenService();
  }
}
