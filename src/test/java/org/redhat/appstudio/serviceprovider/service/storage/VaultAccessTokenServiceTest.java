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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.vault.VaultKVSecretEngine;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redhat.appstudio.serviceprovider.service.NameGenerator;
import org.testcontainers.vault.VaultContainer;

@QuarkusTest
class VaultAccessTokenServiceTest {

  @ConfigProperty(name = "vault.token")
  private String vaultToken;

  @ClassRule
  public static VaultContainer vaultContainer =
      new VaultContainer<>("vault:1.7.1").withVaultToken("1234567");

  @Inject VaultKVSecretEngine kvSecretEngine;

  private final String vaultPath = "spi/accesstokens";

  private final VaultKVMapHelper kvMapHelper = new VaultKVMapHelper();

  VaultAccessTokenService service;

  @BeforeAll
  public static void setUp() {
    vaultContainer.start();
  }

  @BeforeEach
  public void set() {
    service = new VaultAccessTokenService(kvSecretEngine);
  }

  @AfterEach
  void cleanUp() {
    kvSecretEngine
        .listSecrets(vaultPath)
        .forEach(s -> kvSecretEngine.deleteSecret(vaultPath + "/" + s));
  }

  @Test
  public void testSecretRead() {
    // Pre-write token
    final String secretName = NameGenerator.generate("name-", 6);
    final Map<String, String> tokenMap = tokenAsMap(secretName);
    kvSecretEngine.writeSecret(vaultPath + "/" + secretName, tokenMap);

    Optional<AccessToken> token = service.get(secretName);

    assertFalse(token.isEmpty());
    assertEquals(kvMapHelper.fromKVMap(tokenMap), token.get());
  }

  private Map<String, String> tokenAsMap(String name) {
    return Map.of(
        "token", NameGenerator.generate("token-", 256),
        "name", name,
        "serviceProviderUrl", "https://github.com",
        "serviceProviderUserName", "jdoe",
        "serviceProviderUserId", "jd-64839",
        "userId", "i-4934",
        "expiredAfter", "1663409036");
  }
}
