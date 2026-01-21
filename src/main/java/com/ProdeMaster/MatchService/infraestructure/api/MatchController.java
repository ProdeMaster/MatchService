package com.ProdeMaster.MatchService.infraestructure.api;

import com.ProdeMaster.MatchService.application.dto.request.UpdateScoreRequest;
import com.ProdeMaster.MatchService.application.dto.request.UpdateStatusRequest;
import com.ProdeMaster.MatchService.application.dto.response.ApiErrorResponse;
import com.ProdeMaster.MatchService.application.dto.response.MatchResponse;
import com.ProdeMaster.MatchService.application.service.MatchApplicationService;
import com.ProdeMaster.MatchService.domain.model.Match;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {

    @Autowired
    private MatchApplicationService matchApplicationService;

    @GetMapping
    public ResponseEntity<List<MatchResponse>> getAllMatches() {
        List<Match> matches = matchApplicationService.getAllMatches();
        List<MatchResponse> responses = matches.stream()
                .map(this::toMatchResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> getMatch(@PathVariable Long id) {
        Optional<Match> match = matchApplicationService.getMatch(id);
        return match.map(m -> ResponseEntity.ok(toMatchResponse(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/score")
    public ResponseEntity<?> updateMatchScore(@PathVariable Long id,
                                               @RequestBody UpdateScoreRequest request) {
        try {
            Match updated = matchApplicationService.updateMatchScore(id,
                    request.getHomeTeamScore(), request.getAwayTeamScore());
            return ResponseEntity.ok(toMatchResponse(updated));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiErrorResponse(409, "Conflict", e.getMessage(), "/api/v1/matches/" + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(500, "Internal Server Error", e.getMessage(), "/api/v1/matches/" + id));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateMatchStatus(@PathVariable Long id,
                                                 @RequestBody UpdateStatusRequest request) {
        try {
            Match updated = matchApplicationService.updateMatchStatus(id, request.getNewStatus());
            return ResponseEntity.ok(toMatchResponse(updated));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiErrorResponse(409, "Conflict", e.getMessage(), "/api/v1/matches/" + id + "/status"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(500, "Internal Server Error", e.getMessage(), "/api/v1/matches/" + id + "/status"));
        }
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmMatchResult(@PathVariable Long id) {
        try {
            Match confirmed = matchApplicationService.confirmMatchResult(id);
            return ResponseEntity.ok(toMatchResponse(confirmed));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiErrorResponse(409, "Conflict", e.getMessage(), "/api/v1/matches/" + id + "/confirm"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(500, "Internal Server Error", e.getMessage(), "/api/v1/matches/" + id + "/confirm"));
        }
    }

    @GetMapping("/league/{league}")
    public ResponseEntity<List<MatchResponse>> getMatchesByLeague(@PathVariable String league) {
        List<Match> matches = matchApplicationService.getMatchesByLeague(league);
        List<MatchResponse> responses = matches.stream()
                .map(this::toMatchResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/dates")
    public ResponseEntity<List<MatchResponse>> getMatchesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Match> matches = matchApplicationService.getMatchesByDateRange(start, end);
        List<MatchResponse> responses = matches.stream()
                .map(this::toMatchResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MatchResponse>> getMatchesByStatus(@PathVariable MatchStatus status) {
        List<Match> matches = matchApplicationService.getMatchesByStatus(status);
        List<MatchResponse> responses = matches.stream()
                .map(this::toMatchResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/team/{teamName}")
    public ResponseEntity<List<MatchResponse>> getMatchesByTeam(@PathVariable String teamName) {
        List<Match> matches = matchApplicationService.getMatchesByTeam(teamName);
        List<MatchResponse> responses = matches.stream()
                .map(this::toMatchResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> isMatchExist(@PathVariable Long id) {
        return ResponseEntity.ok(matchApplicationService.isMatchExist(id));
    }

    @GetMapping("/{id}/can-update")
    public ResponseEntity<Boolean> canUpdateMatch(@PathVariable Long id) {
        return ResponseEntity.ok(matchApplicationService.canUpdateMatch(id));
    }

    private MatchResponse toMatchResponse(Match match) {
        return new MatchResponse(
                match.getMatchId(),
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getLeague(),
                match.getMatchDateTime(),
                match.getStatus(),
                match.getHomeTeamScore(),
                match.getAwayTeamScore(),
                match.isResultConfirmed()
        );
    }
}
