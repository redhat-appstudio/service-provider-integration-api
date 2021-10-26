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
package org.redhat.appstudio.serviceprovider.service.json;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.base.Splitter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.redhat.appstudio.serviceprovider.service.SpiTestProfile;

@QuarkusTest
@TestProfile(SpiTestProfile.NoDockerTestProfile.class)
public class ScopesSerializerTest {

  ScopesSerializer scopesSerializer = new ScopesSerializer();

  JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
  SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

  @ParameterizedTest
  @MethodSource("serializedData")
  public void testSerialize(Set<String> input) throws Exception {
    scopesSerializer.serialize(input, jsonGenerator, serializerProvider);
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    verify(jsonGenerator).writeString(captor.capture());
    assertEquals(
        Splitter.on(",").splitToStream(captor.getValue()).collect(Collectors.toSet()), input);
  }

  private static Stream<Arguments> serializedData() {
    return Stream.of(
        Arguments.of(Set.of("a", "b", "c")), Arguments.of(Set.of("foo", "bar", "api", "repo")));
  }
}
