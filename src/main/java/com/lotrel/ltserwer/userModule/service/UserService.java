package com.lotrel.ltserwer.userModule.service;

import com.lotrel.ltserwer.authModule.infrastructure.exception.ResourceNotFoundException;
import com.lotrel.ltserwer.authModule.infrastructure.oauth2.UserPrincipal;
import com.lotrel.ltserwer.userModule.model.Users;
import com.lotrel.ltserwer.userModule.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserPrincipal loadUserById(Long id) {
        Users user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }


}
