package taka.example.spring_project.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import taka.example.spring_project.entity.MemberStatus;

import java.util.UUID;

@Repository
public interface MemberStatusRepository extends ReactiveCrudRepository<MemberStatus, UUID> {
    Mono<MemberStatus> findByUserId(UUID userId);

    @Modifying
    @Query("""
            INSERT INTO member_status (user_id, total_points, rank, created_at, updated_at)
            VALUES (:userId, :totalPoints, :rank, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            ON CONFLICT (user_id)
            DO UPDATE SET
                total_points = EXCLUDED.total_points,
                rank = EXCLUDED.rank,
                updated_at = CURRENT_TIMESTAMP
            """)
    Mono<Integer> upsertStatus(
            @Param("userId") UUID userId,
            @Param("totalPoints") Integer totalPoints,
            @Param("rank") String rank);
}
