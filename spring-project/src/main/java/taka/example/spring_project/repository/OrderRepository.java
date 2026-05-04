package taka.example.spring_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taka.example.spring_project.entity.OrderHistory;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderHistory, String> {
    // Add find by order
    Optional<OrderHistory> findByOrderNumber(String orderNumber);

    // Add findByUserId
    Page<OrderHistory> findByUserId(UUID userId, Pageable pageable);
}
