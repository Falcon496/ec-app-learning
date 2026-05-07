package taka.example.spring_project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import taka.example.spring_project.entity.OrderHistory;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<OrderHistory, String> {
    Mono<OrderHistory> findByOrderNumber(String orderNumber);

    Flux<OrderHistory> findByUserId(UUID userId, Pageable pageable);

    Mono<Long> countByUserId(UUID userId);

    @Query("SELECT CAST(COALESCE(SUM(earned_points), 0) AS INTEGER) FROM order_history WHERE user_id = :userId")
    Mono<Integer> sumEarnedPointsByUserId(@Param("userId") UUID userId);

    @Query("""
            SELECT CAST(COALESCE(SUM(earned_points), 0) AS INTEGER)
            FROM order_history
            WHERE user_id = :userId
              AND order_date >= :since
            """)
    Mono<Integer> sumEarnedPointsForUserSince(@Param("userId") UUID userId, @Param("since") LocalDateTime since);
}
