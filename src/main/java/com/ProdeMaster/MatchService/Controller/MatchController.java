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
    //public List<Mono<MatchModel>> getAllMatches() {
    public Void getAllMatches() {
        matchService.SeeWeekMatch();
        return null;
    }

    @GetMapping("/{id}")
    public Mono<MatchModel> getMatchesByID(@PathVariable Long id) {

    }

    @PostMapping("/update-weekly")
    public List<Mono<MatchModel>> updateWeekly() {

    }

    @PostMapping("/update-weekly/{id}")
    public Mono<MatchModel> updateWeeklyByID(@PathVariable Long id) {

    }

}

@RestController
@RequestMapping("/matches/{league}")
public class MatchLeagueController {
    @Autowired
    private MatchService matchService;

    @GetMapping("")
    public List<Mono<MatchModel>> getAllMatchesFromLeague(@PathVariable String league) {

    }

    @GetMapping("/{id}")
    public Mono <MatchModel> getMatchesByID(@PathVariable String league, @PathVariable Long id) {

    }

    @PostMapping("/update-weekly")
    public List<Mono<MatchModel>> updateWeekly(@PathVariable String league) {

    }

    @PostMapping("/update-weekly/{id}")
    public Mono<MatchModel> updateWeeklyByID(@PathVariable String league, @PathVariable Long id) {

    }

}
