package com.example.cirestechnologiesTest.app.services.implimentations;

import com.example.cirestechnologiesTest.app.entities.User;
import com.example.cirestechnologiesTest.app.enums.Role;
import com.example.cirestechnologiesTest.app.repositories.UserRepository;
import com.example.cirestechnologiesTest.app.dtos.UserDTO;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;
    private Faker faker;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        modelMapper = new ModelMapper();
        userService = new UserServiceImpl(userRepository, passwordEncoder, faker, modelMapper);
    }

    @Test
    void testRegisterUsers() {
        List<UserDTO> userDTOs = generateUserDTOs(5);
        when(userRepository.findByUsernameOrEmail(any(), any())).thenReturn(null);
        when(userRepository.saveAll(any())).thenReturn(generateUsers(5));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        Map<String, Integer> summary = userService.registerUsers(userDTOs);
        assertEquals(5, summary.get("total"));
        assertEquals(5, summary.get("success"));
        assertEquals(0, summary.get("failure"));
    }

    @Test
    void testGenerateUsers() {
        int count = 10;
        List<UserDTO> userDTOs = userService.generateUsers(count);
        assertEquals(count, userDTOs.size());
    }

    private List<UserDTO> generateUserDTOs(int count) {
        List<UserDTO> userDTOs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            UserDTO userDTO = new UserDTO();
            userDTO.setFirstName(faker.name().firstName());
            userDTO.setLastName(faker.name().lastName());
            userDTO.setBirthDate(faker.date().birthday().toString());
            userDTO.setCity(faker.address().city());
            userDTO.setCountry(faker.address().countryCode());
            userDTO.setAvatar(faker.internet().avatar());
            userDTO.setCompany(faker.company().name());
            userDTO.setJobPosition(faker.job().position());
            userDTO.setMobile(faker.phoneNumber().cellPhone());
            userDTO.setUsername(faker.internet().emailAddress());
            userDTO.setEmail(faker.internet().emailAddress());
            userDTO.setPassword(faker.internet().password());
            userDTO.setRole(Role.user);
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    private List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = User.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .birthDate(faker.date().birthday().toString())
                    .city(faker.address().city())
                    .country(faker.address().countryCode())
                    .avatar(faker.internet().avatar())
                    .company(faker.company().name())
                    .jobPosition(faker.job().position())
                    .mobile(faker.phoneNumber().cellPhone())
                    .username(faker.internet().emailAddress())
                    .email(faker.internet().emailAddress())
                    .password("encodedPassword")
                    .role(Role.user)
                    .build();
            users.add(user);
        }
        return users;
    }
}