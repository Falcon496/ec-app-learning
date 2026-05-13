package taka.example.spring_project.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("order_details")
public class OrderDetails {

    @Id
    @Column("id")
    private Long id;

    @NonNull
    @Column("order_number")
    private String orderNumber;

    @NonNull
    @Column("product_id")
    private Integer productId;

    @NonNull
    @Column("product_name")
    private String productName;

    @NonNull
    @Column("price")
    private Integer price;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

}
