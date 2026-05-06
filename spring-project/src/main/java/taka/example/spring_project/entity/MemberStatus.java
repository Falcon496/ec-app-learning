package taka.example.spring_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "member_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberStatus {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;

    @Column(name = "rank", nullable = false)
    private String rank;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at",  nullable = false)
    private LocalDateTime updatedAt;
}
