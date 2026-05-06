package taka.example.spring_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import taka.example.spring_project.entity.OrderHistory;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderHistory, String> {
    // Add find by order
    Optional<OrderHistory> findByOrderNumber(String orderNumber);

    // Add findByUserId
    Page<OrderHistory> findByUserId(UUID userId, Pageable pageable);

    // Add sumPointsForUserSince method
    @Query("SELECT SUM(o.totalPrice) / 100 FROM OrderHistory o WHERE o.userId = :userId AND o.orderDate >= :since")
    int sumPointsForUserSince(@Param("userId") UUID userId, @Param("since") LocalDateTime since);
}
