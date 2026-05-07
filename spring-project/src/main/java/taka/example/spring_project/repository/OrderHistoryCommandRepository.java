package taka.example.spring_project.repository;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import taka.example.spring_project.entity.OrderHistory;

@Repository
public class OrderHistoryCommandRepository {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public OrderHistoryCommandRepository(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    public Mono<OrderHistory> insert(OrderHistory orderHistory) {
        return r2dbcEntityTemplate.insert(OrderHistory.class).using(orderHistory);
    }
}
