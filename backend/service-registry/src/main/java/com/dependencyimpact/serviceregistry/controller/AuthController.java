package com.dependencyimpact.serviceregistry.controller;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.common.security.UserPrincipal;
import com.dependencyimpact.serviceregistry.dto.CurrentUserResponse;
import com.dependencyimpact.serviceregistry.dto.LoginRequest;
import com.dependencyimpact.serviceregistry.dto.LoginResponse;
import com.dependencyimpact.serviceregistry.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, authService.login(request), null, null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CurrentUserResponse>> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(new ApiResponse<>(true, authService.getCurrentUser(principal.getId()), null, null));
    }
}
