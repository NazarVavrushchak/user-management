package com.nazar.usermanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nazar.usermanagement.DTO.UserDTO;
import com.nazar.usermanagement.repository.UserRepository;
import com.nazar.usermanagement.utils.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nazar.usermanagement.entity.User;


@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUser(Long id) {
        return userRepository.findById(id).map(UserMapper::toDTO);
    }

    public void createUser(UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        userRepository.save(user);
    }

    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setAge(userDTO.getAge());
            userRepository.save(user);
            return UserMapper.toDTO(user);
        });
    }
}
