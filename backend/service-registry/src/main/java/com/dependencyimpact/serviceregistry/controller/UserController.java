package com.dependencyimpact.serviceregistry.controller;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.serviceregistry.dto.CreateUserRequest;
import com.dependencyimpact.serviceregistry.dto.CurrentUserResponse;
import com.dependencyimpact.serviceregistry.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CurrentUserResponse>>> listUsers() {
        return ResponseEntity.ok(new ApiResponse<>(true, userService.listUsers(), null, null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CurrentUserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, userService.createUser(request), null, null));
    }
}
