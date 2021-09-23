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

import java.util.Objects;

public class AccessToken {
  private String token;
  private String serviceProviderUrl;
  private String serviceProviderUserName;
  private String serviceProviderUserId;
  private String userId;
  private Integer expiredAfter;
  private String name;

  public AccessToken(
      String token,
      String name,
      String serviceProviderUrl,
      String serviceProviderUserName,
      String serviceProviderUserId,
      String userId,
      Integer expiredAfter) {
    this.token = token;
    this.serviceProviderUrl = serviceProviderUrl;
    this.serviceProviderUserName = serviceProviderUserName;
    this.serviceProviderUserId = serviceProviderUserId;
    this.userId = userId;
    this.expiredAfter = expiredAfter;
    this.name = name;
  }

  public AccessToken() {}

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getServiceProviderUrl() {
    return serviceProviderUrl;
  }

  public void setServiceProviderUrl(String serviceProviderUrl) {
    this.serviceProviderUrl = serviceProviderUrl;
  }

  public String getServiceProviderUserName() {
    return serviceProviderUserName;
  }

  public void setServiceProviderUserName(String serviceProviderUserName) {
    this.serviceProviderUserName = serviceProviderUserName;
  }

  public String getServiceProviderUserId() {
    return serviceProviderUserId;
  }

  public void setServiceProviderUserId(String serviceProviderUserId) {
    this.serviceProviderUserId = serviceProviderUserId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Integer getExpiredAfter() {
    return expiredAfter;
  }

  public void setExpiredAfter(Integer expiredAfter) {
    this.expiredAfter = expiredAfter;
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
    AccessToken that = (AccessToken) o;
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
    return "AccessToken{"
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
