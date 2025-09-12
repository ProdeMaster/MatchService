package com.ProdeMaster.MatchService.Controller;

import com.ProdeMaster.MatchService.Model.MatchModel;
import com.ProdeMaster.MatchService.Service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping("")
    public List<Mono<MatchModel>> getAllMatches() {
        matchService.SeeWeekMatch();
        return null;
    }

    @GetMapping("/next-month")
    public List<Mono<MatchModel>> getNextMonthMatches() {
        return null;
    }

    @GetMapping("/{id}")
    public Mono<MatchModel> getMatcheById(@PathVariable long id) {
        return null;
    }

    @PutMapping("/{id}")
    public Mono<MatchModel> setMatcheById(@PathVariable long id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public Mono<MatchModel> deleteMatcheById(@PathVariable long id) {
        return null;
    }
}