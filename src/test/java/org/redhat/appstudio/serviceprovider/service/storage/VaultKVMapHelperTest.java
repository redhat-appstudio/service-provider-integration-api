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

import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.junit.TestProfile;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.redhat.appstudio.serviceprovider.service.NameGenerator;
import org.redhat.appstudio.serviceprovider.service.SpiTestProfile;

@TestProfile(SpiTestProfile.NoDockerTestProfile.class)
class VaultKVMapHelperTest {

  private final VaultKVMapHelper helper = new VaultKVMapHelper();

  @Test
  void testTokenHelper() {
    String tokenValue = NameGenerator.generate("token-", 10);
    AccessToken initialToken =
        new AccessToken(
            tokenValue,
            "name",
            "https://gihtum.com",
            "user1",
            "id1",
            "id1",
            1209438182L,
            Set.of("api", "user", "repo"));
    Map<String, String> map = helper.asKVMap(initialToken);
    AccessToken resultToken = helper.fromKVMap(map);

    assertEquals(initialToken, resultToken);
  }
}
