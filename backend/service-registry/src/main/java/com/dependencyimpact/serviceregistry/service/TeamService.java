package com.dependencyimpact.serviceregistry.service;

import com.dependencyimpact.serviceregistry.dto.CreateTeamRequest;
import com.dependencyimpact.serviceregistry.dto.TeamResponse;
import com.dependencyimpact.serviceregistry.dto.UpdateTeamRequest;
import com.dependencyimpact.serviceregistry.entity.Team;
import com.dependencyimpact.serviceregistry.exception.DuplicateTeamException;
import com.dependencyimpact.serviceregistry.exception.TeamNotFoundException;
import com.dependencyimpact.serviceregistry.mapper.TeamMapper;
import com.dependencyimpact.serviceregistry.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    public TeamResponse createTeam(CreateTeamRequest request) {
        teamRepository.findByName(request.getName()).ifPresent(existing -> {
            throw new DuplicateTeamException("A team named '" + request.getName() + "' already exists");
        });

        Team team = teamMapper.toEntity(request);
        Instant now = Instant.now();
        team.setId(UUID.randomUUID());
        team.setCreatedAt(now);
        team.setUpdatedAt(now);

        Team saved = teamRepository.save(team);
        return teamMapper.toResponse(saved);
    }

    public List<TeamResponse> listTeams() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toResponse)
                .toList();
    }

    public TeamResponse getTeam(UUID id) {
        Team team = findTeamOrThrow(id);
        return teamMapper.toResponse(team);
    }

    public TeamResponse updateTeam(UUID id, UpdateTeamRequest request) {
        Team team = findTeamOrThrow(id);

        if (request.getName() != null) {
            team.setName(request.getName());
        }
        if (request.getDescription() != null) {
            team.setDescription(request.getDescription());
        }
        if (request.getOwnerEmail() != null) {
            team.setOwnerEmail(request.getOwnerEmail());
        }
        team.setUpdatedAt(Instant.now());

        Team saved = teamRepository.save(team);
        return teamMapper.toResponse(saved);
    }

    public void deleteTeam(UUID id) {
        Team team = findTeamOrThrow(id);
        teamRepository.delete(team);
    }

    private Team findTeamOrThrow(UUID id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team not found: " + id));
    }
}
