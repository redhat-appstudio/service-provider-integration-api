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
import javax.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AccessTokenService {
  private static final Logger LOG = Logger.getLogger(AccessTokenService.class);

  private Map<String, AccessToken> tokens = Collections.synchronizedMap(new LinkedHashMap<>());

  public Set<AccessToken> fetchAll() {
    LOG.info("fetchAll");
    return new HashSet<>(tokens.values());
  }

  public Optional<AccessToken> get(String name) {
    LOG.info(name);
    return Optional.ofNullable(tokens.get(name));
  }

  public AccessToken create(AccessToken accessToken) {
    LOG.info(accessToken);
    tokens.put(accessToken.getName(), accessToken);
    return accessToken;
  }

  public void update(String name, AccessToken accessTokenDto) {
    LOG.info(accessTokenDto);
    tokens.replace(name, accessTokenDto);
  }

  public void delete(String name) {
    LOG.info(name);
    tokens.remove(name);
  }
}
