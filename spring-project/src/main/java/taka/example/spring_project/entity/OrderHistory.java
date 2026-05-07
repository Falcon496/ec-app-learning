package taka.example.spring_project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("order_history")
public class OrderHistory {
    @Id
    @Column("order_number")
    private String orderNumber;

    @Column("order_date")
    private LocalDateTime orderDate;

    @Column("user_id")
    private UUID userId;

    @Column("user_name")
    private String userName;

    @Column("total_price")
    private Integer totalPrice;

    @Column("total_quantity")
    private Integer totalQuantity;

    @Column("earned_points")
    private Integer earnedPoints;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
