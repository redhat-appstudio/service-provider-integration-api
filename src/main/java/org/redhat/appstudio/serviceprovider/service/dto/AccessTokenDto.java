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
  @NotBlank(message = "Token may not be empty.")
  private String token;

  @Pattern(regexp = "[a-z0-9]([-a-z0-9]*[a-z0-9])?",
           message="Token name must not be empty, and may only contain dashes, numbers or lowercase letters.")
  private String name;

  private String serviceProviderUrl;

  private String serviceProviderUserName;

  private String serviceProviderUserId;

  private String userId;

  private Integer expiredAfter;

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
        && Objects.equals(name, that.name);
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
        name);
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
        + '}';
  }
}
