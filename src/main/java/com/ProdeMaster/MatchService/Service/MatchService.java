package com.ProdeMaster.MatchService.Service;

import com.ProdeMaster.MatchService.Dto.MatchDto;
import com.ProdeMaster.MatchService.Model.MatchModel;
import com.ProdeMaster.MatchService.Service.Adapters.FootballApiAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ProdeMaster.MatchService.Repository.MatchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;


@Service
public class MatchService {
    @Autowired
    private FootballApiAdapter footballApiClient;

    @Autowired
    private MatchRepository cacheRepository;

    public Void SeeWeekMatch (){
        List<MatchDto> matchDtos = footballApiClient.getWeeklyMatches();
        matchDtos.forEach(System.out::println);
        return null;
    }

        public Mono<Void> cacheWeeklyMatches() {
            List <MatchDto> matchDtos = footballApiClient.getWeeklyMatches();

            Stream<MatchModel> matchs = footballApiClient
                    .getWeeklyMatches()
                    .stream()
                    .map(m -> new MatchModel(m.getId(), m.getTeam1(), m.getTeam2(), m.getLeague(), footballApiClient.parseToLocalDateTime(m.getMatchDateTime()), m.getStatus(), Boolean.FALSE));

            return Flux.fromStream(matchs)
                    .map(match -> cacheRepository.save(match))
                    .then();
    }
}
