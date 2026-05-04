package taka.example.spring_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taka.example.spring_project.entity.OrderDetails;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

    // Add findByOrderNumber method to find order details by order number
    List<OrderDetails> findByOrderNumber(String orderNumber);
}
