package com.example.cirestechnologiesTest.app.services.interfaces;

import com.example.cirestechnologiesTest.app.dtos.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDTO> generateUsers(int count);
    Map<String, Integer> registerUsers(List<UserDTO> users);
    UserDTO profile();
    UserDTO getOneUser(String username);
}
