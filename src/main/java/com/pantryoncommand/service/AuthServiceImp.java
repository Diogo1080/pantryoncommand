package com.pantryoncommand.service;

import com.pantryoncommand.command.auth.CredentialsDto;
import com.pantryoncommand.command.auth.LoggedInDto;
import com.pantryoncommand.command.auth.PrincipalDto;
import com.pantryoncommand.converters.UserConverter;
import com.pantryoncommand.error.ErrorMessages;
import com.pantryoncommand.exeption.auth.WrongCredentialsException;
import com.pantryoncommand.exeption.user.UserNotFoundException;
import com.pantryoncommand.persistence.entity.UserEntity;
import com.pantryoncommand.persistence.repository.UserRepository;
import com.pantryoncommand.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * An {@link AuthService} implementation
 */
@Service
public class AuthServiceImp implements AuthService {

    private static final Logger LOGGER = LogManager.getLogger(AuthServiceImp.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    private String secretKey;

    public AuthServiceImp(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    /**
     * @see AuthService#loginUser(CredentialsDto)
     */
    @Override
    public LoggedInDto loginUser(CredentialsDto credentialsDto) {
        // Get user by email
        UserEntity userEntity =
                userRepository.findByEmail(credentialsDto.getEmail())
                        .orElseThrow(() -> {
                            LOGGER.error("User with email {} not found on database", credentialsDto.getEmail());
                            return new WrongCredentialsException(ErrorMessages.WRONG_CREDENTIALS);
                        });

        // Verify password
        boolean passwordMatches = passwordEncoder.matches(credentialsDto.getPassword(), userEntity.getEncryptedPassword());
        if (!passwordMatches) {
            LOGGER.error("The password doesn't match");
            throw new WrongCredentialsException(ErrorMessages.WRONG_CREDENTIALS);
        }

        // Build principal for the logged in user
        PrincipalDto principal = UserConverter.fromUserEntityToPrincipalDto(userEntity);

        // Get JWT token
        LOGGER.info("Generating JWT token for user with id {}...", userEntity.getId());
        String token = createJwtToken(principal);

        // Build LoggedInDto for the response
        return LoggedInDto.builder()
                .principal(principal)
                .token(token)
                .build();
    }

    /**
     * @see AuthService#validateToken(String)
     */
    @Override
    public PrincipalDto validateToken(String token) {
        // Parse token (if the token is has expired or has an invalid signature it will throw an exception)
        Jws<Claims> jwtClaims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);

        // Get userId from payload/body
        Long userId = jwtClaims.getBody()
                .get("userId", Long.class);

        // Get user from database
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessages.USER_NOT_FOUND));

        // Build principalDto
        return UserConverter.fromUserEntityToPrincipalDto(userEntity);
    }

    /**
     * Helper to create JWT Token
     * @param principalDto
     * @return the token as {@link String}
     */
    private String createJwtToken(PrincipalDto principalDto) {
        // Set claims
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("userId", principalDto.getUserId());
        claimsMap.put("username", principalDto.getUsername());
        claimsMap.put("role", principalDto.getUserRole());

        Claims claims = Jwts.claims(claimsMap);

        // Calculate issued at and expire at
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + jwtProperties.getExpiresIn());

        // Build jwt token and compact as string
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}