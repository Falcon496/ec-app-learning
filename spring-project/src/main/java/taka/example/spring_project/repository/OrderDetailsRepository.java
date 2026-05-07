package taka.example.spring_project.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import taka.example.spring_project.entity.OrderDetails;

import java.util.Collection;

@Repository
public interface OrderDetailsRepository extends ReactiveCrudRepository<OrderDetails, Long> {

    Flux<OrderDetails> findByOrderNumber(String orderNumber);

    Flux<OrderDetails> findByOrderNumberIn(Collection<String> orderNumbers);
}
