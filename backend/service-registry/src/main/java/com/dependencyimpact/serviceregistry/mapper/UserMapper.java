package com.dependencyimpact.serviceregistry.mapper;

import com.dependencyimpact.serviceregistry.dto.CurrentUserResponse;
import com.dependencyimpact.serviceregistry.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public CurrentUserResponse toResponse(User user) {
        return new CurrentUserResponse(user.getId(), user.getEmail(), user.getFullName(), List.of(user.getRole()));
    }
}
