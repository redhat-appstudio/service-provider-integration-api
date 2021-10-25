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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

@QuarkusTest
class ScopesDeserializerTest {

  JsonParser jsonParser = Mockito.mock(JsonParser.class);
  DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

  ScopesDeserializer scopesDeserializer = new ScopesDeserializer();

  @ParameterizedTest
  @MethodSource("serializedData")
  public void testDeserialize(JsonNode input, Set<String> expectedResult) throws Exception {
    when(jsonParser.readValueAsTree()).thenReturn(input);
    Set<String> result = scopesDeserializer.deserialize(jsonParser, deserializationContext);
    assertEquals(expectedResult, result);
  }

  private static Stream<Arguments> serializedData() throws JsonProcessingException {
    return Stream.of(
        Arguments.of(new ObjectMapper().valueToTree("a,  b,   c"), Set.of("a", "b", "c")),
        Arguments.of(
            new ObjectMapper().readValue("[\"a\",\"b\",\"c\"]", JsonNode.class),
            Set.of("a", "b", "c")));
  }
}
