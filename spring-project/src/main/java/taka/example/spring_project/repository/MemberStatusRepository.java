package taka.example.spring_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taka.example.spring_project.entity.MemberStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberStatusRepository extends JpaRepository<MemberStatus, UUID> {
    // Add findByUserId method
    Optional<MemberStatus> findByUserId(UUID userId);
}
