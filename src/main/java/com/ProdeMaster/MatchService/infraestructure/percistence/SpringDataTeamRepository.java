package com.ProdeMaster.MatchService.infraestructure.percistence;

import com.ProdeMaster.MatchService.infraestructure.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface SpringDataTeamRepository extends JpaRepository<TeamEntity, Long> {
    Optional<TeamEntity> findByName(String name);

    Optional<TeamEntity> findById(Long id);

    @Query("""
            SELECT new com.ProdeMaster.MatchService.infraestructure.projection.TeamIdProjection(t.name, t.id)
            FROM TeamEntity t
            WHERE t.name IN :names
            """)
    Map<String, Long> findIdsByNames(@Param("names") Collection<String> names);
}
