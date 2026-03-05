package service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avito.dto.RegisterReqDto;
import ru.avito.entity.Role;
import ru.avito.entity.User;
import ru.avito.mapper.UserMapper;
import ru.avito.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterReqDto registerReq) {
        if (userRepository.findByEmail(registerReq.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = userMapper.registerToEntity(registerReq);
        user.setPassword(passwordEncoder.encode(registerReq.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }
}