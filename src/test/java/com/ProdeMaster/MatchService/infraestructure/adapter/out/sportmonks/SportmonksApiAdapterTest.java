package com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks;

import com.ProdeMaster.MatchService.application.dto.MatchDto;
import com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks.dto.SportmonksApiResponse;
import com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks.dto.SportmonksFixtureResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SportmonksApiAdapterTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private SportmonksApiAdapter sportmonksApiAdapter;

    private static final String TEST_TOKEN = "test-token";
    private static final String TEST_BASE_URL = "https://api.sportmonks.com/v3";

    @BeforeEach
    void setUp() {
        sportmonksApiAdapter = new SportmonksApiAdapter(restClient, TEST_TOKEN, TEST_BASE_URL);
    }

    private SportmonksFixtureResponse.FixtureData createFixture(Long id, String name, String startingAt) {
        SportmonksFixtureResponse.FixtureData fixture = new SportmonksFixtureResponse.FixtureData();
        fixture.setId(id);
        fixture.setName(name);
        fixture.setStartingAt(startingAt);
        return fixture;
    }

    private SportmonksApiResponse createApiResponse(List<SportmonksFixtureResponse.FixtureData> fixtures) {
        SportmonksApiResponse response = new SportmonksApiResponse();
        response.setFixtures(fixtures);
        return response;
    }

    // ==================== GET WEEKLY MATCHES ====================

    /**
     * Testea la obtención exitosa de partidos semanales.
     * Verifica la llamada a la API, el mapeo correcto de la respuesta JSON a
     * objetos MatchDto y la cantidad de resultados.
     */
    @Test
    void testGetWeeklyMatchesSuccess() {
        List<SportmonksFixtureResponse.FixtureData> fixtures = Arrays.asList(
                createFixture(1L, "Team A vs Team B", "2024-01-15 20:00:00"),
                createFixture(2L, "Team C vs Team D", "2024-01-16 18:00:00"));
        SportmonksApiResponse apiResponse = createApiResponse(fixtures);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq(TEST_BASE_URL + "/football/fixtures/between/{start}/{end}?api_token={token}"),
                any(Map.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SportmonksApiResponse.class)).thenReturn(apiResponse);

        List<MatchDto> result = sportmonksApiAdapter.getWeeklyMatches();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Team A", result.get(0).getHomeTeam());
        assertEquals("Team B", result.get(0).getAwayTeam());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Team C", result.get(1).getHomeTeam());
        assertEquals("Team D", result.get(1).getAwayTeam());
    }

    /**
     * Testea el comportamiento cuando la API retorna una lista vacía de partidos
     * semanales.
     * Verifica que el método retorne una lista vacía.
     */
    @Test
    void testGetWeeklyMatchesEmpty() {
        SportmonksApiResponse apiResponse = createApiResponse(Collections.emptyList());

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Map.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SportmonksApiResponse.class)).thenReturn(apiResponse);

        List<MatchDto> result = sportmonksApiAdapter.getWeeklyMatches();

        assertTrue(result.isEmpty());
    }

    /**
     * Testea el comportamiento ante un error de la API al obtener partidos
     * semanales.
     * Verifica que se capture la excepción y se retorne una lista vacía en lugar de
     * propagar el error.
     */
    @Test
    void testGetWeeklyMatchesApiError() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Map.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SportmonksApiResponse.class)).thenThrow(new RuntimeException("API Error"));

        List<MatchDto> result = sportmonksApiAdapter.getWeeklyMatches();

        assertTrue(result.isEmpty());
    }

    // ==================== GET TODAY MATCHES ====================

    /**
     * Testea la obtención exitosa de los partidos del día.
     * Verifica la llamada a un endpoint diferente específico para la fecha actual.
     */
    @Test
    void testGetTodayMatchesSuccess() {
        List<SportmonksFixtureResponse.FixtureData> fixtures = Arrays.asList(
                createFixture(1L, "Team A vs Team B", "2024-01-15 20:00:00"));
        SportmonksApiResponse apiResponse = createApiResponse(fixtures);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq(TEST_BASE_URL + "/football/fixtures/date/{today}?api_token={token}"),
                any(Map.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SportmonksApiResponse.class)).thenReturn(apiResponse);

        List<MatchDto> result = sportmonksApiAdapter.getTodayMatches();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Team A", result.get(0).getHomeTeam());
        assertEquals("Team B", result.get(0).getAwayTeam());
    }

    // ==================== GET MATCHES BY LEAGUE ====================

    /**
     * Testea la obtención de partidos filtrados por ID de liga.
     * Verifica que se añada el filtro correcto en la URL de la API.
     */
    @Test
    void testGetMatchesByLeagueSuccess() {
        List<SportmonksFixtureResponse.FixtureData> fixtures = Arrays.asList(
                createFixture(1L, "Team A vs Team B", "2024-01-15 20:00:00"));
        SportmonksApiResponse apiResponse = createApiResponse(fixtures);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(
                eq(TEST_BASE_URL + "/football/fixtures?api_token={token}&filters=fixtureLeagues:{leagueId}"),
                any(Map.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SportmonksApiResponse.class)).thenReturn(apiResponse);

        List<MatchDto> result = sportmonksApiAdapter.getMatchesByLeague(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    // ==================== GET MATCHES BY DATE RANGE ====================

    /**
     * Testea la obtención de partidos en un rango de fechas específico.
     * Verifica que las fechas de inicio y fin se pasen correctamente en la URL.
     */
    @Test
    void testGetMatchesByDateRangeSuccess() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 15, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 22, 0, 0);

        List<SportmonksFixtureResponse.FixtureData> fixtures = Arrays.asList(
                createFixture(1L, "Team A vs Team B", "2024-01-15 20:00:00"));
        SportmonksApiResponse apiResponse = createApiResponse(fixtures);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq(TEST_BASE_URL + "/football/fixtures/between/{start}/{end}?api_token={token}"),
                any(Map.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SportmonksApiResponse.class)).thenReturn(apiResponse);

        List<MatchDto> result = sportmonksApiAdapter.getMatchesByDateRange(startDate, endDate);

        assertEquals(1, result.size());
    }

    // ==================== PARSE TO LOCAL DATE TIME ====================

    /**
     * Testea el parsiado de fechas desde el formato String de la API a
     * LocalDateTime.
     * Verifica que los componentes de la fecha y hora sean correctos.
     */
    @Test
    void testParseToLocalDateTimeSuccess() {
        String dateString = "2024-01-15 20:30:00";

        LocalDateTime result = sportmonksApiAdapter.parseToLocalDateTime(dateString);

        assertEquals(2024, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(20, result.getHour());
        assertEquals(30, result.getMinute());
    }

    /**
     * Testea el manejo de errores al parsear una fecha con formato inválido.
     * Verifica que se lance una excepción.
     */
    @Test
    void testParseToLocalDateTimeInvalidFormat() {
        String invalidDate = "2024/01/15 20:30:00";

        assertThrows(Exception.class, () -> sportmonksApiAdapter.parseToLocalDateTime(invalidDate));
    }

    // ==================== MATCH NAME PARSING ====================

    /**
     * Testea el parsiado de nombres de equipos separados por " vs ".
     * Verifica que se extraigan correctamente el equipo local y visitante.
     */
    @Test
    void testMatchNameWithVsSeparator() {
        List<SportmonksFixtureResponse.FixtureData> fixtures = Arrays.asList(
                createFixture(1L, "Real Madrid vs Barcelona", "2024-01-15 20:00:00"));
        SportmonksApiResponse apiResponse = createApiResponse(fixtures);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Map.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SportmonksApiResponse.class)).thenReturn(apiResponse);

        List<MatchDto> result = sportmonksApiAdapter.getWeeklyMatches();

        assertEquals(1, result.size());
        assertEquals("Real Madrid", result.get(0).getHomeTeam());
        assertEquals("Barcelona", result.get(0).getAwayTeam());
    }

    /**
     * Testea el parsiado de nombres con espacios extra alrededor del separador.
     * Verifica que se haga trim de los nombres de los equipos.
     */
    @Test
    void testMatchNameWithExtraWhitespace() {
        List<SportmonksFixtureResponse.FixtureData> fixtures = Arrays.asList(
                createFixture(1L, "  Team A   vs   Team B  ", "2024-01-15 20:00:00"));
        SportmonksApiResponse apiResponse = createApiResponse(fixtures);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Map.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SportmonksApiResponse.class)).thenReturn(apiResponse);

        List<MatchDto> result = sportmonksApiAdapter.getWeeklyMatches();

        assertEquals(1, result.size());
        assertEquals("Team A", result.get(0).getHomeTeam());
        assertEquals("Team B", result.get(0).getAwayTeam());
    }

    /**
     * Testea el caso en que el nombre del partido no contiene el separador " vs ".
     * Verifica que se use el nombre completo como local y "Unknown" como visitante.
     */
    @Test
    void testMatchNameWithoutVsUsesUnknown() {
        List<SportmonksFixtureResponse.FixtureData> fixtures = Arrays.asList(
                createFixture(1L, "Invalid Match Name", "2024-01-15 20:00:00"));
        SportmonksApiResponse apiResponse = createApiResponse(fixtures);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Map.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SportmonksApiResponse.class)).thenReturn(apiResponse);

        List<MatchDto> result = sportmonksApiAdapter.getWeeklyMatches();

        assertEquals(1, result.size());
        assertEquals("Invalid Match Name", result.get(0).getHomeTeam());
        assertEquals("Unknown", result.get(0).getAwayTeam());
    }

    // ==================== NULL AND EDGE CASES ====================

    /**
     * Testea el caso en que el cuerpo de la respuesta de la API es nulo.
     * Verifica que se maneje devolviendo una lista vacía.
     */
    @Test
    void testNullResponse() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Map.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SportmonksApiResponse.class)).thenReturn(null);

        List<MatchDto> result = sportmonksApiAdapter.getWeeklyMatches();

        assertTrue(result.isEmpty());
    }

    /**
     * Testea el caso en que la lista de fixtures dentro de la respuesta es nula.
     * Verifica que se maneje devolviendo una lista vacía.
     */
    @Test
    void testNullFixturesList() {
        SportmonksApiResponse apiResponse = new SportmonksApiResponse();
        apiResponse.setFixtures(null);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Map.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SportmonksApiResponse.class)).thenReturn(apiResponse);

        List<MatchDto> result = sportmonksApiAdapter.getWeeklyMatches();

        assertTrue(result.isEmpty());
    }
}
