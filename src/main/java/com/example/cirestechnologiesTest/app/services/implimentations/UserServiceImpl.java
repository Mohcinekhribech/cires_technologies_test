package com.example.cirestechnologiesTest.app.services.implimentations;

import com.example.cirestechnologiesTest.app.entities.User;
import com.example.cirestechnologiesTest.app.enums.Role;
import com.example.cirestechnologiesTest.app.repositories.UserRepository;
import com.example.cirestechnologiesTest.app.services.interfaces.UserService;
import com.example.cirestechnologiesTest.app.dtos.UserDTO;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker;
    private final ModelMapper modelMapper;

    @Override
    public Map<String, Integer> registerUsers(List<UserDTO> users) {
        Map<String, Integer> summary = new HashMap<>();
        int totalCount = users.size();

        List<User> users1 = users.stream()
                .filter(u -> userRepository.findByUsernameOrEmail(u.getUsername(), u.getEmail()) == null)
                .map(u ->
            User.builder()
                    .firstName(u.getFirstName())
                    .lastName(u.getLastName())
                    .birthDate(u.getBirthDate())
                    .city(u.getCity())
                    .country(u.getCountry())
                    .avatar(u.getAvatar())
                    .company(u.getCompany())
                    .jobPosition(u.getJobPosition())
                    .mobile(u.getMobile())
                    .username(u.getUsername())
                    .email(u.getEmail())
                    .password(passwordEncoder.encode(u.getPassword()))
                    .role(u.getRole())
                    .build()
        ).toList();


        List<User> users2 = userRepository.saveAll(users1);

        summary.put("total", totalCount);
        summary.put("success", users2.size());
        summary.put("failure", totalCount - users2.size());

        return summary;
    }

    @Override
    public List<UserDTO> generateUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = User
                    .builder()
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
                    .password(generateRandomPassword())
                    .role(generateRandomRole())
                    .build();
            users.add(user);
        }
        return users.stream().map(user -> modelMapper.map(user,UserDTO.class)).collect(Collectors.toList());
    }

    private String generateRandomPassword() {
        int length = ThreadLocalRandom.current().nextInt(6, 11);
        return RandomStringUtils.randomAlphanumeric(length);
    }

    private Role generateRandomRole() {
        return ThreadLocalRandom.current().nextBoolean() ? Role.admin : Role.user;
    }
    public UserDTO profile()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return modelMapper.map(userRepository.findByEmail(username),UserDTO.class);
    }

    @Override
    public UserDTO getOneUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u ->{
            return modelMapper.map(u,UserDTO.class);
        }).orElse(null);
    }
}
