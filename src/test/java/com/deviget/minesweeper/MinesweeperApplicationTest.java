package com.deviget.minesweeper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.deviget.minesweeper.payload.request.LoginRequest;
import com.deviget.minesweeper.payload.request.NewGameRequest;
import com.deviget.minesweeper.payload.request.SignupRequest;
import com.deviget.minesweeper.payload.response.LoginResponse;
import com.deviget.minesweeper.payload.response.MessageResponse;

@SpringBootTest(classes = MinesweeperApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MinesweeperApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;
    private HttpHeaders headers = new HttpHeaders();

    @Test
    @Order(1)
    public void testSignupSuccessfully() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<MessageResponse> responseEntity = restTemplate.postForEntity("/user/signup", new SignupRequest("test",
                "test@test.com", "testpwd"), MessageResponse.class);

        MessageResponse messageResponse = responseEntity.getBody();
        assertNotNull(messageResponse, "Message Response is empty");
        // assertThat(messageResponse.getMessage(), anyOf(is("User registered"), is("Error: Username already exists")));
    }

    @Test
    @Order(2)
    public void testLoginSuccessfully() {
        ResponseEntity<LoginResponse> responseEntity = restTemplate.postForEntity("/user/signin", new LoginRequest("test", "testpwd"),
                LoginResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Unexpected Status code");

        LoginResponse loginResponse = responseEntity.getBody();
        assertNotNull(loginResponse, "Response body is empty");

        accessToken = loginResponse.getAccessToken();
        assertNotNull(accessToken, "Authorization token is not present");
    }

    @Test
    @Order(3)
    public void testCreateGameSucessfully() {
        headers.setBearerAuth(accessToken);

        NewGameRequest request = new NewGameRequest(10, 10, 5);
        HttpEntity<NewGameRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<?> response = restTemplate.postForEntity("/game", httpEntity, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Unexpected Status code");

        List<String> location = response.getHeaders()
                .get(HttpHeaders.LOCATION);
        assertNotNull(location, "Location header is not present");
        assertNotEquals(location.size(), 0, "Location header is empty");

        String actual = location.get(0);
        assertTrue(actual.contains("/minesweeper/game/"), "Unexpected Location headers");
    }

    @Test
    @Order(4)
    public void testDeleteAllGamesSucessfully() {
        HttpEntity<NewGameRequest> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<MessageResponse> response = restTemplate.exchange("/game", HttpMethod.DELETE, httpEntity, MessageResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected Status code");

        MessageResponse messageResponse = response.getBody();
        assertNotNull(messageResponse, "Message Response is empty");
        assertEquals(messageResponse.getMessage(), "All Games deleted");
    }
}
