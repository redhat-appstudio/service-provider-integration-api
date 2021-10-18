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
package org.redhat.appstudio.serviceprovider.service.storage.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.vault.runtime.VaultKvManager;
import io.quarkus.vault.runtime.client.VaultClientException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.redhat.appstudio.serviceprovider.service.NameGenerator;
import org.redhat.appstudio.serviceprovider.service.SpiTestProfile;
import org.redhat.appstudio.serviceprovider.service.storage.AccessToken;
import org.redhat.appstudio.serviceprovider.service.storage.AccessTokenService;
import org.redhat.appstudio.serviceprovider.service.storage.VaultKVMapHelper;

@TestProfile(SpiTestProfile.VaultProfile.class)
@QuarkusTest
class VaultAccessTokenServiceTest {

  @Inject VaultKvManager vaultKvManager;
  @Inject AccessTokenService service;

  private final String vaultPath = "spi/accesstokens";

  private final VaultKVMapHelper kvMapHelper = new VaultKVMapHelper();

  @AfterEach
  void cleanUp() {
    vaultKvManager
        .listSecrets(vaultPath)
        .forEach(s -> vaultKvManager.deleteSecret(vaultPath + "/" + s));
  }

  @Test
  public void testTokenRead() {
    // Pre-write token
    final String secretName = NameGenerator.generate("name-", 6);
    final Map<String, String> tokenMap = tokenAsMap(secretName);
    vaultKvManager.writeSecret(vaultPath + "/" + secretName, tokenMap);

    Optional<AccessToken> token = service.get(secretName);

    assertFalse(token.isEmpty());
    assertEquals(kvMapHelper.fromKVMap(tokenMap), token.get());
  }

  @Test
  public void testTokenWrite() {
    final String secretName = NameGenerator.generate("name-", 6);
    final Map<String, String> tokenMap = tokenAsMap(secretName);

    service.create(kvMapHelper.fromKVMap(tokenMap));

    Map<String, String> readMap = vaultKvManager.readSecret(vaultPath + "/" + secretName);

    assertFalse(readMap.isEmpty());
    assertEquals(tokenMap, readMap);
  }

  @Test
  public void testTokenUpdate() {
    // Pre-write token
    final String secretName = NameGenerator.generate("name-", 6);
    final Map<String, String> tokenMap = tokenAsMap(secretName);
    vaultKvManager.writeSecret(vaultPath + "/" + secretName, tokenMap);

    // Modify Map
    Map<String, String> modified = new HashMap<>(tokenMap);
    modified.put("token", "123foo");
    modified.put("serviceProviderUrl", "https://gitlab.com");

    service.update(secretName, kvMapHelper.fromKVMap(modified));

    Map<String, String> readMap = vaultKvManager.readSecret(vaultPath + "/" + secretName);

    assertFalse(readMap.isEmpty());
    assertEquals("123foo", readMap.get("token"));
    assertEquals("https://gitlab.com", readMap.get("serviceProviderUrl"));
  }

  @Test
  public void testTokenRemove() {
    // Pre-write token
    final String secretName = NameGenerator.generate("name-", 6);
    final Map<String, String> tokenMap = tokenAsMap(secretName);
    vaultKvManager.writeSecret(vaultPath + "/" + secretName, tokenMap);

    service.delete(secretName);

    try {
      vaultKvManager.readSecret(vaultPath + "/" + secretName);
      fail("Token must not be found");
    } catch (VaultClientException e) {
      if (!(e.getStatus() == 404)) {
        fail("Incorrect code status");
      }
    }
  }

  @Test
  public void testGetRemovedV2Token() {
    // Pre-write token
    final String secretName = NameGenerator.generate("name-", 6);
    final Map<String, String> tokenMap = tokenAsMap(secretName);
    vaultKvManager.writeSecret(vaultPath + "/" + secretName, tokenMap);
    service.delete(secretName);

    Optional<AccessToken> token = service.get(vaultPath + "/" + secretName);
    assertTrue(token.isEmpty());
  }

  @Test
  public void testListAfterRemovedV2Token() {
    // Pre-write tokens
    final String secretName1 = NameGenerator.generate("name-", 6);
    vaultKvManager.writeSecret(vaultPath + "/" + secretName1, tokenAsMap(secretName1));

    final String secretName2 = NameGenerator.generate("name-", 6);
    vaultKvManager.writeSecret(vaultPath + "/" + secretName2, tokenAsMap(secretName2));

    final String secretName3 = NameGenerator.generate("name-", 6);
    vaultKvManager.writeSecret(vaultPath + "/" + secretName3, tokenAsMap(secretName3));

    service.delete(secretName2);

    Set<AccessToken> tokens = service.fetchAll();
    assertEquals(2, tokens.size());
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
