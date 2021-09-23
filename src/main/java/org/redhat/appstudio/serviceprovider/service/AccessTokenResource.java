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

import static org.redhat.appstudio.serviceprovider.service.dto.DtoConverter.asDto;
import static org.redhat.appstudio.serviceprovider.service.dto.DtoConverter.asToken;

import com.google.common.base.Strings;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.redhat.appstudio.serviceprovider.service.dto.AccessTokenDto;
import org.redhat.appstudio.serviceprovider.service.dto.DtoConverter;
import org.redhat.appstudio.serviceprovider.service.storage.AccessToken;
import org.redhat.appstudio.serviceprovider.service.storage.AccessTokenService;

@Path("token")
public class AccessTokenResource {
  @Inject AccessTokenService accessTokenService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Set<AccessTokenDto> fetchAll() {
    return accessTokenService.fetchAll().stream()
        .map(DtoConverter::asDto)
        .collect(Collectors.toSet());
  }

  @GET
  @Path("{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@PathParam("name") String name) {
    Optional<AccessTokenDto> token = accessTokenService.get(name).map(DtoConverter::asDto);
    if (token.isEmpty()) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok().entity(token.get()).build();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@Valid AccessTokenDto accessToken) {
    AccessToken token = asToken(accessToken);
    if (Strings.isNullOrEmpty(token.getName())) {
      token.setName(NameGenerator.generate("token-", 6));
    }
    return Response.status(Response.Status.CREATED)
        .entity(asDto(accessTokenService.create(token)))
        .build();
  }

  @PUT
  @Path("/{name}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("name") String name, AccessToken accessToken) {
    if (!name.equals(accessToken.getName())) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Token name is not matched path parameter")
          .build();
    }
    accessTokenService.update(name, accessToken);
    return Response.ok().entity(accessToken).build();
  }

  @DELETE
  @Path("/{name}")
  public Response delete(@PathParam("name") String name) {
    accessTokenService.delete(name);
    return Response.status(Response.Status.NO_CONTENT)
        .entity("Token deleted successfully !!")
        .build();
  }
}
