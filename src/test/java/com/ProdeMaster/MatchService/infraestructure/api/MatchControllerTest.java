package com.ProdeMaster.MatchService.infraestructure.api;

import com.ProdeMaster.MatchService.application.dto.request.UpdateScoreRequest;
import com.ProdeMaster.MatchService.application.dto.request.UpdateStatusRequest;
import com.ProdeMaster.MatchService.application.service.MatchApplicationService;
import com.ProdeMaster.MatchService.domain.model.Match;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MatchControllerTest {

        private MockMvc mockMvc;

        @Mock
        private MatchApplicationService matchApplicationService;

        @InjectMocks
        private MatchController matchController;

        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders.standaloneSetup(matchController).build();
                objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
        }

        private Match createTestMatch(Long id, String homeTeam, String awayTeam, MatchStatus status) {
                Match match = new Match(id, homeTeam, awayTeam, "Premier League",
                                LocalDateTime.now().plusDays(1));
                match.setMatchId(id);
                if (status != null) {
                        match.startFirstHalf();
                        if (status == MatchStatus.HT) {
                                match.endFirstHalf();
                        } else if (status == MatchStatus.FT) {
                                match.endFirstHalf();
                                match.startSecondHalf();
                                match.endRegularTime();
                        }
                }
                return match;
        }

        // ==================== GET ALL MATCHES ====================
        @Test
        void testGetAllMatchesSuccess() throws Exception {
                Match match1 = createTestMatch(1L, "Home Team 1", "Away Team 1", MatchStatus.NS);
                Match match2 = createTestMatch(2L, "Home Team 2", "Away Team 2", MatchStatus.NS);

                when(matchApplicationService.getAllMatches()).thenReturn(Arrays.asList(match1, match2));

                mockMvc.perform(get("/api/v1/matches"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].matchId", is(1)))
                                .andExpect(jsonPath("$[0].homeTeam", is("Home Team 1")))
                                .andExpect(jsonPath("$[1].matchId", is(2)))
                                .andExpect(jsonPath("$[1].homeTeam", is("Home Team 2")));

                verify(matchApplicationService).getAllMatches();
        }

        @Test
        void testGetAllMatchesEmpty() throws Exception {
                when(matchApplicationService.getAllMatches()).thenReturn(Collections.emptyList());

                mockMvc.perform(get("/api/v1/matches"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$", hasSize(0)));
        }

        // ==================== GET MATCH BY ID ====================
        @Test
        void testGetMatchSuccess() throws Exception {
                Match match = createTestMatch(1L, "Home Team", "Away Team", MatchStatus.NS);

                when(matchApplicationService.getMatch(1L)).thenReturn(Optional.of(match));

                mockMvc.perform(get("/api/v1/matches/1"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.matchId", is(1)))
                                .andExpect(jsonPath("$.homeTeam", is("Home Team")))
                                .andExpect(jsonPath("$.awayTeam", is("Away Team")));
        }

        @Test
        void testGetMatchNotFound() throws Exception {
                when(matchApplicationService.getMatch(999L)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/v1/matches/999"))
                                .andExpect(status().isNotFound());
        }

        // ==================== UPDATE SCORE ====================
        @Test
        void testUpdateMatchScoreSuccess() throws Exception {
                Match match = createTestMatch(1L, "Home Team", "Away Team", MatchStatus.INPLAY_1ST_HALF);
                match.updateScore(2, 1);

                when(matchApplicationService.updateMatchScore(1L, 2, 1)).thenReturn(match);

                UpdateScoreRequest request = new UpdateScoreRequest(2, 1);

                mockMvc.perform(put("/api/v1/matches/1/score")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.matchId", is(1)))
                                .andExpect(jsonPath("$.homeTeamScore", is(2)))
                                .andExpect(jsonPath("$.awayTeamScore", is(1)));
        }

        @Test
        void testUpdateMatchScoreConflict() throws Exception {
                when(matchApplicationService.updateMatchScore(1L, 2, 1))
                                .thenThrow(new IllegalStateException("No se puede actualizar el partido"));

                UpdateScoreRequest request = new UpdateScoreRequest(2, 1);

                mockMvc.perform(put("/api/v1/matches/1/score")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.status", is(409)))
                                .andExpect(jsonPath("$.error", is("Conflict")));
        }

        // ==================== UPDATE STATUS ====================
        @Test
        void testUpdateMatchStatusSuccess() throws Exception {
                Match match = createTestMatch(1L, "Home Team", "Away Team", MatchStatus.INPLAY_1ST_HALF);

                when(matchApplicationService.updateMatchStatus(1L, MatchStatus.INPLAY_1ST_HALF))
                                .thenReturn(match);

                UpdateStatusRequest request = new UpdateStatusRequest(MatchStatus.INPLAY_1ST_HALF);

                mockMvc.perform(put("/api/v1/matches/1/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.matchId", is(1)))
                                .andExpect(jsonPath("$.status", is("INPLAY_1ST_HALF")));
        }

        @Test
        void testUpdateMatchStatusInvalidTransition() throws Exception {
                when(matchApplicationService.updateMatchStatus(1L, MatchStatus.FT))
                                .thenThrow(new IllegalStateException("Transición inválida"));

                UpdateStatusRequest request = new UpdateStatusRequest(MatchStatus.FT);

                mockMvc.perform(put("/api/v1/matches/1/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.status", is(409)))
                                .andExpect(jsonPath("$.error", is("Conflict")));
        }

        // ==================== CONFIRM RESULT ====================
        @Test
        void testConfirmMatchResultSuccess() throws Exception {
                Match match = createTestMatch(1L, "Home Team", "Away Team", MatchStatus.FT);
                match.confirmResult();

                when(matchApplicationService.confirmMatchResult(1L)).thenReturn(match);

                mockMvc.perform(put("/api/v1/matches/1/confirm"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.matchId", is(1)))
                                .andExpect(jsonPath("$.resultConfirmed", is(true)));
        }

        @Test
        void testConfirmMatchResultNotFinished() throws Exception {
                when(matchApplicationService.confirmMatchResult(1L))
                                .thenThrow(new IllegalStateException("Partido no finalizado"));

                mockMvc.perform(put("/api/v1/matches/1/confirm"))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.status", is(409)));
        }

        // ==================== GET MATCHES BY LEAGUE ====================
        @Test
        void testGetMatchesByLeagueSuccess() throws Exception {
                Match match = createTestMatch(1L, "Home Team", "Away Team", MatchStatus.NS);

                when(matchApplicationService.getMatchesByLeague("Premier League"))
                                .thenReturn(Arrays.asList(match));

                mockMvc.perform(get("/api/v1/matches/league/Premier League"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].league", is("Premier League")));
        }

        // ==================== GET MATCHES BY DATE RANGE ====================
        @Test
        void testGetMatchesByDateRangeSuccess() throws Exception {
                Match match = createTestMatch(1L, "Home Team", "Away Team", MatchStatus.NS);
                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = LocalDateTime.now().plusDays(7);

                when(matchApplicationService.getMatchesByDateRange(start, end))
                                .thenReturn(Arrays.asList(match));

                mockMvc.perform(get("/api/v1/matches/dates")
                                .param("start", start.toString())
                                .param("end", end.toString()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)));
        }

        // ==================== GET MATCHES BY STATUS ====================
        @Test
        void testGetMatchesByStatusSuccess() throws Exception {
                Match match = createTestMatch(1L, "Home Team", "Away Team", MatchStatus.INPLAY_1ST_HALF);

                when(matchApplicationService.getMatchesByStatus(MatchStatus.INPLAY_1ST_HALF))
                                .thenReturn(Arrays.asList(match));

                mockMvc.perform(get("/api/v1/matches/status/INPLAY_1ST_HALF"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].status", is("INPLAY_1ST_HALF")));
        }

        // ==================== GET MATCHES BY TEAM ====================
        @Test
        void testGetMatchesByTeamSuccess() throws Exception {
                Match match = createTestMatch(1L, "Home Team", "Away Team", MatchStatus.NS);

                when(matchApplicationService.getMatchesByTeam("Home Team"))
                                .thenReturn(Arrays.asList(match));

                mockMvc.perform(get("/api/v1/matches/team/Home Team"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].homeTeam", is("Home Team")));
        }

        // ==================== CHECK MATCH EXISTS ====================
        @Test
        void testIsMatchExistTrue() throws Exception {
                when(matchApplicationService.isMatchExist(1L)).thenReturn(true);

                mockMvc.perform(get("/api/v1/matches/1/exists"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("true"));
        }

        @Test
        void testIsMatchExistFalse() throws Exception {
                when(matchApplicationService.isMatchExist(999L)).thenReturn(false);

                mockMvc.perform(get("/api/v1/matches/999/exists"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("false"));
        }

        // ==================== CHECK CAN UPDATE ====================
        @Test
        void testCanUpdateMatchTrue() throws Exception {
                when(matchApplicationService.canUpdateMatch(1L)).thenReturn(true);

                mockMvc.perform(get("/api/v1/matches/1/can-update"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("true"));
        }

        @Test
        void testCanUpdateMatchFalse() throws Exception {
                when(matchApplicationService.canUpdateMatch(1L)).thenReturn(false);

                mockMvc.perform(get("/api/v1/matches/1/can-update"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("false"));
        }
}
