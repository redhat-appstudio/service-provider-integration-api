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
package org.redhat.appstudio.serviceprovider.service.dto;

import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class AccessTokenDto {
  @NotBlank(message = "token may not be blank")
  private String token;

  @Pattern(regexp = "[a-z0-9]([-a-z0-9]*[a-z0-9])?")
  private String name;

  public AccessTokenDto() {}

  public AccessTokenDto(String token, String name) {
    this.token = token;
    this.name = name;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AccessTokenDto that = (AccessTokenDto) o;
    return Objects.equals(token, that.token) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, name);
  }

  @Override
  public String toString() {
    return "AccessTokenDto{" + "token='" + token + '\'' + ", name='" + name + '\'' + '}';
  }
}
