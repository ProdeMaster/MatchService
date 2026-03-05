package com.ProdeMaster.MatchService.infraestructure.service;

import com.ProdeMaster.MatchService.application.port.in.event.MatchEventPublisher;
import com.ProdeMaster.MatchService.application.port.out.cache.MatchCacheRepository;
import com.ProdeMaster.MatchService.application.port.out.db.MatchRepository;
import com.ProdeMaster.MatchService.application.service.MatchApplicationService;
import org.springframework.stereotype.Service;

@Service
public class MatchInfrastructureService extends MatchApplicationService {

    public MatchInfrastructureService(MatchRepository matchRepository,
            MatchCacheRepository cacheRepository,
            MatchEventPublisher eventPublisher) {
        super(matchRepository, cacheRepository, eventPublisher);
    }
}
