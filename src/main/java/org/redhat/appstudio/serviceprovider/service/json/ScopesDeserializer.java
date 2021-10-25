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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.Splitter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Helps to deserialize raw set of comma-separated strings or string array as {@code scopes} field.
 */
public class ScopesDeserializer extends JsonDeserializer<Set<String>> {

  @Override
  public Set<String> deserialize(
      JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    JsonNode node = jsonParser.readValueAsTree();
    if (node instanceof TextNode) {
      return Splitter.on(",")
          .trimResults()
          .omitEmptyStrings()
          .splitToStream(node.asText())
          .collect(Collectors.toSet());
    } else if (node instanceof ArrayNode) {
      Set<String> scopes = new HashSet<>();
      node.elements().forEachRemaining(el -> scopes.add(el.textValue()));
      return scopes;
    }
    throw new JsonParseException(jsonParser, "Cannot parse format of `scopes` field.");
  }
}
