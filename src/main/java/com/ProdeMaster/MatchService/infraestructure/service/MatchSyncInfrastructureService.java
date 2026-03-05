package com.ProdeMaster.MatchService.infraestructure.service;

import com.ProdeMaster.MatchService.application.port.out.api.FootballApiAdapter;
import com.ProdeMaster.MatchService.application.port.out.cache.MatchCacheRepository;
import com.ProdeMaster.MatchService.application.port.out.db.MatchRepository;
import com.ProdeMaster.MatchService.application.service.MatchSyncServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MatchSyncInfrastructureService extends MatchSyncServiceImpl {

    public MatchSyncInfrastructureService(FootballApiAdapter apiAdapter,
            MatchRepository matchRepository,
            MatchCacheRepository cacheRepository) {
        super(apiAdapter, matchRepository, cacheRepository);
    }
}
