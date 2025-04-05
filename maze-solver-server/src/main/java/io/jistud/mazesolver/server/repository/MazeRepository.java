package io.jistud.mazesolver.server.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.jistud.mazesolver.server.entity.MazeEntity;

@Repository
public interface MazeRepository extends JpaRepository<MazeEntity, Integer> {

    /**
     * Find all mazes created before the given timestamp
     *
     * @param createdAt the timestamp to compare against
     * @return a list of mazes created before the given timestamp
     */
    List<MazeEntity> findByCreatedAtBefore(Instant createdAt);
}
