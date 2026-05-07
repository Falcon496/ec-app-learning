package taka.example.spring_project.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import taka.example.spring_project.dto.MemberStatusRequest;
import taka.example.spring_project.dto.MemberStatusResponse;
import taka.example.spring_project.service.MemberStatusService;

import java.util.UUID;

@RestController
@RequestMapping("/api/member-status")
public class MemberStatusController {
    private final MemberStatusService memberStatusService;

    @Autowired
    public MemberStatusController(MemberStatusService memberStatusService) {
        this.memberStatusService = memberStatusService;
    }

    @PostMapping("/calculate")
    public Mono<ResponseEntity<MemberStatusResponse>> calculateMemberStatus(
            @Valid @RequestBody MemberStatusRequest memberStatusRequest) {
        return memberStatusService.calculateAndUpdateMemberStatus(
                        memberStatusRequest.getUserId(),
                        memberStatusRequest.getOrderNumber())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<MemberStatusResponse>> getMemberStatus(@PathVariable UUID userId) {
        return memberStatusService.getMemberStatus(userId)
                .map(ResponseEntity::ok);
    }
}
