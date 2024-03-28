package com.example.cirestechnologiesTest.app.controllers;

import com.example.cirestechnologiesTest.app.entities.User;
import com.example.cirestechnologiesTest.app.services.interfaces.UserService;
import com.example.cirestechnologiesTest.app.dtos.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PostMapping("/batch")
    public ResponseEntity<Map<String, Integer>> uploadUsers(@RequestParam("file") MultipartFile file) {
        try {
            List<UserDTO> users = objectMapper.readValue(file.getBytes(), objectMapper.getTypeFactory().constructCollectionType(List.class, UserDTO.class));

            Map<String, Integer> summary = userService.registerUsers(users);
            return ResponseEntity.ok(summary);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/generate")
    public ResponseEntity<List<UserDTO>> generateUsers(@RequestParam int count) {
        List<UserDTO>  users = userService.generateUsers(count);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Content-Disposition", "attachment; filename=users.json");

        return ResponseEntity.ok()
                .headers(headers)
                .body(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> profile() {
        return ResponseEntity.ok()
                .body(userService.profile());
    }
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getOneUser(@PathVariable String username) {
        return ResponseEntity.ok()
                .body(userService.getOneUser(username));
    }
}
