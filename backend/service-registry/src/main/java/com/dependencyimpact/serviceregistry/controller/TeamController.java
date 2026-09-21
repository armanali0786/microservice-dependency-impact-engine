package com.dependencyimpact.serviceregistry.controller;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.serviceregistry.dto.CreateTeamRequest;
import com.dependencyimpact.serviceregistry.dto.TeamResponse;
import com.dependencyimpact.serviceregistry.dto.UpdateTeamRequest;
import com.dependencyimpact.serviceregistry.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        TeamResponse response = teamService.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, response, null, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamResponse>>> listTeams() {
        List<TeamResponse> teams = teamService.listTeams();
        return ResponseEntity.ok(new ApiResponse<>(true, teams, null, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeam(@PathVariable UUID id) {
        TeamResponse response = teamService.getTeam(id);
        return ResponseEntity.ok(new ApiResponse<>(true, response, null, null));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(@PathVariable UUID id,
                                                                  @Valid @RequestBody UpdateTeamRequest request) {
        TeamResponse response = teamService.updateTeam(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, response, null, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
