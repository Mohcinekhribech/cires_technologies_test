package com.example.cirestechnologiesTest.security.auth;

import com.example.cirestechnologiesTest.config.ResponseMessage;
import com.example.cirestechnologiesTest.security.User.DTOs.UserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;
    private final ResponseMessage responseMessage;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate JWT token by removing it from client-side storage
        service.logout(request,response);
        responseMessage.setMessage("Logout successful");
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

}