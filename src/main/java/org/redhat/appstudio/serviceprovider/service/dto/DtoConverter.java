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

import org.redhat.appstudio.serviceprovider.service.storage.AccessToken;

public class DtoConverter {
  public static AccessTokenDto asDto(AccessToken accessToken) {
    return new AccessTokenDto()
        .withToken(accessToken.getToken())
        .withName(accessToken.getName())
        .withServiceProviderUrl(accessToken.getServiceProviderUrl())
        .withServiceProviderUserId(accessToken.getServiceProviderUserId())
        .withServiceProviderUserName(accessToken.getServiceProviderUserName())
        .withUserId(accessToken.getUserId())
        .withExpiredAfter(accessToken.getExpiredAfter());
  }

  public static AccessToken asToken(AccessTokenDto accessTokenDto) {
    return new AccessToken(
        accessTokenDto.getToken(),
        accessTokenDto.getName(),
        accessTokenDto.getServiceProviderUrl() != null
            ? accessTokenDto.getServiceProviderUrl()
            : "https://github.com",
        "jdoe",
        "jd-64839",
        "i-4934",
        1663409036);
  }
}
