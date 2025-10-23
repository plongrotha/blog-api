package org.personalblogapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.personalblogapi.dto.LoginRequest;
import org.personalblogapi.dto.RegisterRequest;
import org.personalblogapi.mapper.UserMapper;
import org.personalblogapi.model.entity.User;
import org.personalblogapi.model.response.ApiResponse;
import org.personalblogapi.model.response.LoginResponse;
import org.personalblogapi.model.response.UserResponse;
import org.personalblogapi.service.AuthService;
import org.personalblogapi.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @Operation(summary = "Register")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody @Valid RegisterRequest request) {
        User user = authService.register(request);
        return ResponseUtil.created(userMapper.toUserResponse(user), "User registered successfully");
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request,
            HttpSession session) {
        // User user = authService.login(request);
        //
        // if (user != null) {
        // session.setAttribute("userId", user.getUserId());
        // session.setAttribute("username", user.getUsername());
        //
        //
        // return ResponseUtil.ok(null, "Login successful");
        // } else {
        //
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        // ApiResponse.<UserResponse>builder()
        // .message("Invalid credentials")
        // .code(HttpStatus.UNAUTHORIZED.value())
        // .build());
        // }
        User user = authService.login(request);

        if (user != null) {
            // ✅ Store session attributes
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());

            // ✅ Map User entity to LoginResponse
            LoginResponse loginResponse = userMapper.toResponseLogin(user);

            // ✅ Return standard success response
            return ResponseUtil.ok(loginResponse, "Login successful");
        } else {
            // ❌ Invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.<LoginResponse>builder()
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message("Invalid credentials")
                            .isSuccess(false)
                            .build());
        }
    }

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        session.invalidate();
        return ResponseUtil.success("log out successfully");
    }

}
