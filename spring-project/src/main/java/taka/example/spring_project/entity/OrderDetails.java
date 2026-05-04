package taka.example.spring_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @NonNull
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @NonNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NonNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "created_at", nullable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;

}
