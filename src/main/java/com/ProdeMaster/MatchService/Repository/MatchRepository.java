package com.ProdeMaster.MatchService.Repository;

import com.ProdeMaster.MatchService.Model.MatchModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MatchRepository extends ListCrudRepository <MatchModel, Long> {
}
