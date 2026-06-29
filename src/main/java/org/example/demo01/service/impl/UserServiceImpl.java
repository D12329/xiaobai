package org.example.demo01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.demo01.common.Result;
import org.example.demo01.entity.User;
import org.example.demo01.mapper.UserMapper;
import org.example.demo01.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret:MySecretKeyForJWTTokenGenerationAndVerification2024}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    @Transactional(readOnly = true)
    public Result<Map<String, Object>> login(String username, String password) {
        User user = findByUsername(username);
        if (user == null) {
            return Result.errorWithMsg("User not found", 404);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Result.errorWithMsg("Invalid password", 401);
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            return Result.errorWithMsg("User is disabled", 403);
        }
        String token = generateToken(user);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return Result.success(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Result<User> getUserInfo() {
        return Result.success(null);
    }

    @Override
    public Result<Void> logout() {
        return Result.success();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    @Transactional
    public boolean register(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        user.setCreatedAt(LocalDateTime.now());
        return userMapper.insert(user) > 0;
    }

    @Override
    public String verifyToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    private String generateToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpiration);
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("realName", user.getRealName())
                .claim("roleId", user.getRoleId())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    @Transactional
    public Result<Void> changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.errorWithMsg("User not found", 404);
        }
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.errorWithMsg("Old password is incorrect", 401);
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
        
        return Result.success();
    }

    @Override
    @Transactional
    public Result<Void> resetPassword(String username, String newPassword) {
        User user = findByUsername(username);
        if (user == null) {
            return Result.errorWithMsg("User not found", 404);
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
        
        return Result.success();
    }
}
