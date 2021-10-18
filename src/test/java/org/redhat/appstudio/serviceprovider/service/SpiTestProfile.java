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
package org.redhat.appstudio.serviceprovider.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;
import java.util.Set;

public class SpiTestProfile {
    public static class NoDockerTestProfile implements QuarkusTestProfile {
        @Override
        public Set<String> tags() {
            return ImmutableSet.of("nodocker");
        }
    }

    public static class VaultProfile implements QuarkusTestProfile {

        @Override
        public Map<String, String> getConfigOverrides() {
            return ImmutableMap.of(
                    "spi.backend.type", "vault",  "quarkus.vault.devservices.enabled", "true");
        }

        @Override
        public Set<String> tags() {
            return ImmutableSet.of("vault");
        }
    }
}
