package com.caixabank.authservice.service;

import com.caixabank.authservice.data.entity.Token;
import com.caixabank.authservice.data.entity.User;
import com.caixabank.authservice.data.mapper.UserMapper;
import com.caixabank.authservice.data.repository.TokenRepository;
import com.caixabank.authservice.data.repository.UserRepository;
import com.caixabank.authservice.dto.LoginRequest;
import com.caixabank.authservice.dto.LoginResponse;
import com.caixabank.authservice.dto.UserRequest;
import com.caixabank.authservice.dto.UserResponse;
import com.caixabank.authservice.security.JwtUtil;
import com.caixabank.authservice.validator.EmailValidator;
import com.caixabank.authservice.validator.PasswordValidator;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository,
                       UserMapper userMapper, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    public UserResponse register(UserRequest userRequest) {
        EmailValidator.validateEmail(userRequest.email());
        PasswordValidator.validatePassword(userRequest.password());

        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = findByEmail(loginRequest.email());
        validatePassword(loginRequest.password(), user);

        String jwtToken = generateJwtTokenForUser(user);
        saveToken(jwtToken, user);

        Long expiresIn = jwtUtil.getExpirationInSeconds();
        UserResponse userResponse = userMapper.toResponse(user);

        return new LoginResponse(jwtToken, "Bearer", expiresIn, userResponse);
    }

    public void logout(String rawToken) {
        Token token = findExtractedToken(extractToken(rawToken));
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()
                -> new BadCredentialsException("Invalid email or password"));
    }

    private void validatePassword(String rawPassword, User user) {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    private String generateJwtTokenForUser(User user) {
        return jwtUtil.generateTokenFromUser(user);
    }

    private void saveToken(String jwtToken, User user) {
        revokeExistingTokens(user);
        createAndSaveNewToken(jwtToken, user);
    }

    private void revokeExistingTokens(User user) {
        List<Token> validTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
        if (validTokens.isEmpty()) return;

        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }

    private void createAndSaveNewToken(String jwtToken, User user) {
        Token token = new Token();
        token.setToken(jwtToken);
        token.setUser(user);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    private String extractToken(String rawToken) {
        if (rawToken == null || !rawToken.startsWith("Bearer ")) {
            throw new BadCredentialsException("Invalid or expired token");
        }
        return rawToken.substring(7).trim();
    }

    private Token findExtractedToken(String extractedToken) {
        return tokenRepository.findByToken(extractedToken).orElseThrow(() ->
                new BadCredentialsException("Invalid or expired token"));
    }
}

