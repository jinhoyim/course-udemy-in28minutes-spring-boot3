package com.in28minutes.rest.webservices.restfulwebservices;

import com.in28minutes.rest.webservices.restfulwebservices.jwt.JwtTokenRequest;
import com.in28minutes.rest.webservices.restfulwebservices.jwt.JwtTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestAuthenticationComponent {

    @Lazy
    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${app.jwt.user.name}")
    private String username;

    @Value("${app.jwt.user.password}")
    private String password;

    private String token = "";

    public String getToken(int port) {
        if (token == null || token.isEmpty()) {
            log.info("Token is empty. Generating new token.");
            var jwtTokenRequest = new JwtTokenRequest(username, password);
            JwtTokenResponse jwtTokenResponse = this.restTemplate.postForObject(
                    "http://localhost:" + port + "/authenticate", jwtTokenRequest,
                    JwtTokenResponse.class);
            token = jwtTokenResponse.token();
        }
        return token;
    }

    public HttpHeaders getAuthorizationHeaders(int port) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken(port));
        return headers;
    }
}
