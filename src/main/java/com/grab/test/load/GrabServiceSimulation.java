package com.grab.test.load;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GrabServiceSimulation extends Simulation {

  // Define HTTP protocol (adjust base URL to your Spring Boot app)
  private HttpProtocolBuilder httpProtocol = http
    .baseUrl("http://34.87.143.89:30002/api") // adjust if you use context-path
    .contentTypeHeader("application/json");

  // Scenario for creating driver
  private ScenarioBuilder createDriverScenarioBuilder = scenario("Load Test: Create Driver")
    .exec(
      http("Create Driver")
        .post("/drivers")
        .body(StringBody("{\"fcmToken\": \"sample-token-123\"}"))
        .check(status().is(201)) // expect CREATED
    );

  {
    setUp(
      createDriverScenarioBuilder.injectOpen(
        rampUsers(60000).during(60) // 10 users over 10 seconds
      )).protocols(httpProtocol);
  }
}
