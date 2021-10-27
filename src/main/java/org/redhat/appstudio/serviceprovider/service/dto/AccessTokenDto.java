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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.redhat.appstudio.serviceprovider.service.json.ScopesDeserializer;

public class AccessTokenDto {
  @NotBlank(message = "Token may not be empty.")
  @Schema(
      required = true,
      description =
          "OAuth token or Personal access tokens. (PATs) are an alternative to using passwords for authentication.",
      example = "ghp_szs_this_is_an_example_3459Eh24545ccqJstwW14545BD4545T15454")
  private String token;

  @Pattern(
      regexp = "[a-z0-9]([-a-z0-9]*[a-z0-9])?",
      message =
          "Token name must not be empty, and may only contain dashes, numbers or lowercase letters.")
  @Schema(
      example = "github_token",
      description =
          "Meaningful token name. Would be used for token referencing. Should be a valid Kubernetes name.")
  private String name;

  @Schema(
      example = "github.com",
      description = "Url of service provider. Available options: github.com, quay.io.")
  private String serviceProviderUrl;

  @Schema(
      example = "jdoe",
      description =
          "User name associated with token and registered in service provider's database.")
  private String serviceProviderUserName;

  @Schema(
      example = "id-234346456",
      description = "User id associated with token and registered in service provider's database.")
  private String serviceProviderUserId;

  @Schema(
      example = "usr-234346456",
      description =
          "User id associated with token and registered in service provider integration database.")
  private String userId;

  @Schema(
      example = "1635317140530",
      description =
          "Time in milliseconds since January 1, 1970, 00:00:00 GMT when the token will expire.")
  private Integer expiredAfter;

  @Schema(
      example = "repo, user",
      description =
          "OAuth token scopes. Scope is a mechanism in OAuth 2.0 to limit an application's access to a user's account.")
  @JsonDeserialize(using = ScopesDeserializer.class)
  private Set<String> scopes;

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

  public AccessTokenDto withToken(String token) {
    this.token = token;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AccessTokenDto withName(String name) {
    this.name = name;
    return this;
  }

  public String getServiceProviderUrl() {
    return serviceProviderUrl;
  }

  public void setServiceProviderUrl(String serviceProviderUrl) {
    this.serviceProviderUrl = serviceProviderUrl;
  }

  public AccessTokenDto withServiceProviderUrl(String serviceProviderUrl) {
    this.serviceProviderUrl = serviceProviderUrl;
    return this;
  }

  public String getServiceProviderUserName() {
    return serviceProviderUserName;
  }

  public void setServiceProviderUserName(String serviceProviderUserName) {
    this.serviceProviderUserName = serviceProviderUserName;
  }

  public AccessTokenDto withServiceProviderUserName(String serviceProviderUserName) {
    this.serviceProviderUserName = serviceProviderUserName;
    return this;
  }

  public String getServiceProviderUserId() {
    return serviceProviderUserId;
  }

  public void setServiceProviderUserId(String serviceProviderUserId) {
    this.serviceProviderUserId = serviceProviderUserId;
  }

  public AccessTokenDto withServiceProviderUserId(String serviceProviderUserId) {
    this.serviceProviderUserId = serviceProviderUserId;
    return this;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public AccessTokenDto withUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public Integer getExpiredAfter() {
    return expiredAfter;
  }

  public void setExpiredAfter(Integer expiredAfter) {
    this.expiredAfter = expiredAfter;
  }

  public AccessTokenDto withExpiredAfter(Integer expiredAfter) {
    this.expiredAfter = expiredAfter;
    return this;
  }

  public Set<String> getScopes() {
    return scopes;
  }

  public void setScopes(Set<String> scopes) {
    this.scopes = scopes;
  }

  public AccessTokenDto withScopes(Set<String> scopes) {
    this.scopes = scopes;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AccessTokenDto that = (AccessTokenDto) o;
    return Objects.equals(token, that.token)
        && Objects.equals(serviceProviderUrl, that.serviceProviderUrl)
        && Objects.equals(serviceProviderUserName, that.serviceProviderUserName)
        && Objects.equals(serviceProviderUserId, that.serviceProviderUserId)
        && Objects.equals(userId, that.userId)
        && Objects.equals(expiredAfter, that.expiredAfter)
        && Objects.equals(name, that.name)
        && Objects.equals(scopes, that.scopes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        token,
        serviceProviderUrl,
        serviceProviderUserName,
        serviceProviderUserId,
        userId,
        expiredAfter,
        name,
        scopes);
  }

  @Override
  public String toString() {
    return "AccessTokenDto{"
        + "token='"
        + token
        + '\''
        + ", serviceProviderUrl='"
        + serviceProviderUrl
        + '\''
        + ", serviceProviderUserName='"
        + serviceProviderUserName
        + '\''
        + ", serviceProviderUserId='"
        + serviceProviderUserId
        + '\''
        + ", userId='"
        + userId
        + '\''
        + ", expiredAfter='"
        + expiredAfter
        + '\''
        + ", name='"
        + name
        + '\''
        + ", scopes='"
        + scopes
        + '\''
        + '}';
  }
}
