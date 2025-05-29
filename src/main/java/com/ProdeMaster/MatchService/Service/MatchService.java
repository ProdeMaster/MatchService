package com.ProdeMaster.MatchService.Service;

import com.ProdeMaster.MatchService.Dto.MatchDto;
import com.ProdeMaster.MatchService.Service.Adapters.FootballApiAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ProdeMaster.MatchService.Repository.MatchRepository;

import java.util.List;


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

}
