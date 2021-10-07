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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.quarkus.runtime.configuration.ConfigurationException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.vault.runtime.VaultKvManager;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TokenServiceProducerTest {

  @InjectMock VaultKvManager vaultKvManager;

  @Test
  void produceInmemoryAccessTokenService() {
    TokenServiceProducer producer = new TokenServiceProducer(vaultKvManager, "inmemory");
    assertTrue(producer.produceAccessTokenService() instanceof InmemoryAccessTokenService);
  }

  @Test
  void produceVaultAccessTokenService() {
    TokenServiceProducer producer = new TokenServiceProducer(vaultKvManager, "vault");
    assertTrue(producer.produceAccessTokenService() instanceof VaultAccessTokenService);
  }

  @Test()
  void produceExceptionOnBadConfigurationValue() {
    try {
      TokenServiceProducer producer = new TokenServiceProducer(vaultKvManager, "foo");
      producer.produceAccessTokenService();
      fail();
    } catch (ConfigurationException e) {
      // ok
    }
  }
}
