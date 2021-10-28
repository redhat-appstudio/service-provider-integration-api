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

import static com.google.common.base.MoreObjects.firstNonNull;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.redhat.appstudio.serviceprovider.service.dto.AccessTokenDto;
import org.redhat.appstudio.serviceprovider.service.dto.DtoConverter;
import org.redhat.appstudio.serviceprovider.service.storage.AccessToken;
import org.redhat.appstudio.serviceprovider.service.storage.AccessTokenService;

@Path("token")
public class AccessTokenResource {
  @Inject AccessTokenService accessTokenService;

  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "All available access tokens",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(type = SchemaType.ARRAY, implementation = AccessTokenDto.class))),
        @APIResponse(responseCode = "500", description = "Internal service error")
      })
  @Operation(
      summary = "Get all available access tokens",
      description = "Get all available access tokens")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Set<AccessTokenDto> fetchAll() {
    return accessTokenService.fetchAll().stream()
        .map(DtoConverter::asDto)
        .collect(Collectors.toSet());
  }

  @APIResponses(
      value = {
        @APIResponse(responseCode = "404", description = "Access token Not Found"),
        @APIResponse(
            responseCode = "200",
            description = "Access token with requested name",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccessTokenDto.class))),
        @APIResponse(responseCode = "500", description = "Internal service error")
      })
  @Operation(
      summary = "Get specific access token by name",
      description = "Get specific access token by name")
  @GET
  @Path("{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(
      @Parameter(description = "Access token name", required = true) @PathParam("name")
          String name) {
    Optional<AccessTokenDto> token = accessTokenService.get(name).map(DtoConverter::asDto);
    if (token.isEmpty()) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok().entity(token.get()).build();
  }

  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "201",
            description = "Access token with requested name",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccessTokenDto.class))),
        @APIResponse(
            responseCode = "400",
            description = "Missed required parameters, parameters are not valid"),
        @APIResponse(responseCode = "409", description = "Token with same name already exists"),
        @APIResponse(responseCode = "500", description = "Internal service error")
      })
  @Operation(summary = "Create access token", description = "Create access token")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(
      @Parameter(description = "Access token", required = true) @Valid
          AccessTokenDto accessTokenDto) {
    Optional<AccessToken> existed = accessTokenService.get(accessTokenDto.getName());
    if (existed.isPresent()) {
      return Response.status(Response.Status.CONFLICT)
          .entity(String.format("Token with name %s already exists", accessTokenDto.getName()))
          .build();
    }
    AccessToken token = asToken(accessTokenDto);
    if (Strings.isNullOrEmpty(token.getName())) {
      token.setName(NameGenerator.generate("token-", 6));
    }

    // Temporary until proper information expansion.
    token.setServiceProviderUrl(firstNonNull(token.getServiceProviderUrl(), "https://github.com"));
    token.setServiceProviderUserName(firstNonNull(token.getServiceProviderUserName(), "jdoe"));
    token.setServiceProviderUserId(firstNonNull(token.getServiceProviderUserId(), "u-3434653"));
    token.setExpiredAfter(firstNonNull(token.getExpiredAfter(), 1663409036L));

    return Response.status(Response.Status.CREATED)
        .entity(asDto(accessTokenService.create(token)))
        .build();
  }

  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "400",
            description = "Missed required parameters, parameters are not valid"),
        @APIResponse(responseCode = "404", description = "Access token Not Found"),
        @APIResponse(
            responseCode = "200",
            description = "Updated access token/",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AccessTokenDto.class))),
        @APIResponse(responseCode = "500", description = "Internal service error")
      })
  @Operation(
      summary = "Update specific access token by name",
      description = "Update specific access token by name")
  @PUT
  @Path("/{name}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(
      @Parameter(description = "Access token name", required = true) @PathParam("name") String name,
      AccessTokenDto accessTokenDto) {
    if (!name.equals(accessTokenDto.getName())) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Token name is not matched path parameter")
          .build();
    }
    accessTokenService.update(name, asToken(accessTokenDto));
    return Response.ok().entity(accessTokenDto).build();
  }

  @APIResponses(
      value = {
        @APIResponse(responseCode = "404", description = "Access token Not Found"),
        @APIResponse(responseCode = "204", description = "Access token deleted successfully"),
        @APIResponse(responseCode = "500", description = "Internal service error")
      })
  @Operation(
      summary = "Delete specific access token by name",
      description = "Delete specific access token by name")
  @DELETE
  @Path("/{name}")
  public Response delete(
      @Parameter(description = "Access token name", required = true) @PathParam("name")
          String name) {
    if (accessTokenService.get(name).isEmpty()) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    accessTokenService.delete(name);
    return Response.status(Response.Status.NO_CONTENT)
        .entity("Token deleted successfully !!")
        .build();
  }
}
