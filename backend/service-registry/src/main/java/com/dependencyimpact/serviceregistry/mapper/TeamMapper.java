package com.dependencyimpact.serviceregistry.mapper;

import com.dependencyimpact.serviceregistry.dto.CreateTeamRequest;
import com.dependencyimpact.serviceregistry.dto.TeamResponse;
import com.dependencyimpact.serviceregistry.entity.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public Team toEntity(CreateTeamRequest request) {
        Team team = new Team();
        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team.setOwnerEmail(request.getOwnerEmail());
        return team;
    }

    public TeamResponse toResponse(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getOwnerEmail(),
                team.getCreatedAt(),
                team.getUpdatedAt()
        );
    }
}
