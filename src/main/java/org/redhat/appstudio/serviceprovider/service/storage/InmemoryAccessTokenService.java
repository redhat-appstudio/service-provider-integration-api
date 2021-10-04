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

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.jboss.logging.Logger;

/** In-memory tokens storage implementation. */
public class InmemoryAccessTokenService implements AccessTokenService {
  private static final Logger LOG = Logger.getLogger(InmemoryAccessTokenService.class);

  private Map<String, AccessToken> tokens = Collections.synchronizedMap(new LinkedHashMap<>());

  public Set<AccessToken> fetchAll() {
    LOG.debug("fetchAll");
    return new HashSet<>(tokens.values());
  }

  public Optional<AccessToken> get(String name) {
    LOG.debug("Get by name:" + name);
    return Optional.ofNullable(tokens.get(name));
  }

  public AccessToken create(AccessToken accessToken) {
    LOG.debug("Create token: " + accessToken.toString());
    tokens.put(accessToken.getName(), accessToken);
    return accessToken;
  }

  public void update(String name, AccessToken accessTokenDto) {
    LOG.debug("Update token: " + accessTokenDto.toString());
    tokens.replace(name, accessTokenDto);
  }

  public void delete(String name) {
    LOG.debug("Delete token: " + name);
    tokens.remove(name);
  }
}
