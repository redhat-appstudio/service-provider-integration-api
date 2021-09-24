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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.Arrays;
import java.util.List;
import org.redhat.appstudio.serviceprovider.service.dto.AccessTokenDto;
import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class AccessTokenResourceTest {

  @Test
  @DisplayName("Test if Application is up by accessing health endpoint")
  public void test_applicationIsUp() {
    try {
      given()
          .get("/q/health/ready")
          .then()
          .assertThat()
          .spec(prepareResponseSpec(200))
          .and()
          .assertThat()
          .body("status", equalTo("UP"));
    } catch (Exception e) {
      fail("Error occurred while testing application health check", e);
    }
  }

  @Test
  public void test_createAccessToken() throws Exception {

    AccessTokenDto token = prepareAccessToken(NameGenerator.generate("accesstoken-", 3));

    postAccessTokenDto(token)
        .then()
        .assertThat()
        .spec(prepareResponseSpec(201))
        .and()
        .assertThat()
        .body("name", equalTo(token.getName()));
  }

  @Test
  public void test_createAccessTokenWithoutName() throws Exception {

    AccessTokenDto token = prepareAccessToken(null);

    postAccessTokenDto(token)
        .then()
        .assertThat()
        .spec(prepareResponseSpec(201))
        .and()
        .assertThat()
        .body("name", startsWith("token-"));
  }

  @Test
  public void test_getAccessTokens() throws Exception {
    postAccessTokenDto(prepareAccessToken(NameGenerator.generate("accesstoken-", 3)));
    postAccessTokenDto(prepareAccessToken(NameGenerator.generate("accesstoken-", 3)));

    given()
        .get("api/v1/token")
        .then()
        .assertThat()
        .spec(prepareResponseSpec(200))
        .and()
        .assertThat()
        .body("data", is(not(empty())));
  }

  @Test
  public void test_getCatalogueItem() throws Exception {
    AccessTokenDto token = prepareAccessToken(NameGenerator.generate("accesstoken-", 3));
    postAccessTokenDto(token);

    given()
        .pathParam("name", token.getName())
        .when()
        .get("api/v1/token/{name}")
        .then()
        .assertThat()
        .spec(prepareResponseSpec(200))
        .and()
        .assertThat()
        .body("name", equalTo(token.getName()))
        .and()
        .assertThat()
        .body("token", equalTo(token.getToken()));
  }

  @Test
  public void test_updateCatalogueItem() throws Exception {
    // Create Catalogue Item
    AccessTokenDto token = prepareAccessToken(NameGenerator.generate("accesstoken-", 3));
    postAccessTokenDto(token);

    // Update catalogue item
    token.setToken(NameGenerator.generate("t2-", 8));

    given()
        .contentType("application/json")
        .body(token)
        .pathParam("name", token.getName())
        .when()
        .put("api/v1/token/{name}")
        .then()
        .assertThat()
        .spec(prepareResponseSpec(200));

    // Get updated catalogue item with the sku of the catalogue item that is created and compare the
    // response fields
    given()
        .pathParam("name", token.getName())
        .when()
        .get("api/v1/token/{name}")
        .then()
        .assertThat()
        .spec(prepareResponseSpec(200))
        .and()
        .assertThat()
        .body("name", equalTo(token.getName()))
        .and()
        .assertThat()
        .body("token", equalTo(token.getToken()));
  }

  @Test
  public void test_deleteCatalogueItem() throws Exception {
    // Create Catalogue Item
    AccessTokenDto token = prepareAccessToken(NameGenerator.generate("accesstoken-", 3));
    postAccessTokenDto(token);

    // Delete Catalogue Item
    given()
        .pathParam("name", token.getName())
        .when()
        .delete("api/v1/token/{name}")
        .then()
        .assertThat()
        .spec(prepareResponseSpec(204));

    // Trying to get the deleted catalogue item should throw 400
    given()
        .pathParam("name", token.getName())
        .when()
        .get("api/v1/token/{name}")
        .then()
        .assertThat()
        .spec(prepareResponseSpec(404));
  }

  @Test
  public void test_resourceNotFound() {
    given()
        .pathParam("name", NameGenerator.generate("accesstoken-", 3))
        .when()
        .get("api/v1/token/{name}")
        .then()
        .assertThat()
        .spec(prepareResponseSpec(404));
  }

  @Test
  public void test_validationErrors() throws Exception {
      AccessTokenDto token = prepareAccessToken(NameGenerator.generate("accesstoken-", 3));
      token.setName("-1");

      Response response =
          postAccessTokenDto(token)
              .then()
              .log()
              .all()
              .assertThat()
              .spec(prepareResponseSpec(400))
              .and()
              .body("parameterViolations[0].path", equalTo("create.arg0.name"))
              .and()
              .body(
                  "parameterViolations[0].message",
                  equalTo("must match \"[a-z0-9]([-a-z0-9]*[a-z0-9])?\""))
              .and()
              .log()
              .all()
              .extract()
              .response();

  }

  @Test
  @DisplayName("Test Invalid Request")
  public void test_invalidRequest() {
    try {
      Response response =
          given()
              .contentType("application/json")
              .body("{\"foo\" : \"bar\"}".getBytes())
              .post("api/v1/token")
              .then()
              .assertThat().spec(prepareResponseSpec(400))
              .and()
              .extract().response();

      List<ResteasyConstraintViolation> parameterViolations = Arrays
          .asList(response.getBody().jsonPath().getObject("parameterViolations",
              ResteasyConstraintViolation[].class));

      assertTrue(parameterViolations != null && parameterViolations.size() > 0);

      assertTrue(
          parameterViolations.get(0).getMessage().equalsIgnoreCase("token may not be blank"));
    } catch (Exception e) {
      fail("Error occurred while testing invalid request", e);
    }
  }

  private ResponseSpecification prepareResponseSpec(int responseStatus) {
    return new ResponseSpecBuilder().expectStatusCode(responseStatus).build();
  }

  private Response postAccessTokenDto(AccessTokenDto accessTokenDto) throws Exception {
    RequestSpecification request = given().log().all().contentType("application/json").header("Accept-Language", "en-US").body(accessTokenDto);

    return request.post("api/v1/token");
  }

  private AccessTokenDto prepareAccessToken(String name) {
    return new AccessTokenDto(NameGenerator.generate("token-", 10), name);
  }
}
