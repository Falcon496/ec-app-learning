package taka.example.spring_project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taka.example.spring_project.dto.MemberStatusRequest;
import taka.example.spring_project.dto.MemberStatusResponse;
import taka.example.spring_project.service.MemberStatusService;

import java.util.UUID;

@RestController
@RequestMapping("/api/member-status")
public class MemberStatusController {
    private static final Logger logger = LoggerFactory.getLogger(MemberStatusController.class);
    private final MemberStatusService memberStatusService;

    @Autowired
    public MemberStatusController(MemberStatusService memberStatusService) {
        this.memberStatusService = memberStatusService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<MemberStatusResponse> calculateMemberStatus(@RequestBody MemberStatusRequest memberStatusRequest) {
        try {
            MemberStatusResponse result = memberStatusService.calculateAndUpdateMemberStatus(memberStatusRequest.getUserId(), memberStatusRequest.getOrderNumber());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error occurred while calculate member status", e);
            MemberStatusResponse errorResponse = new MemberStatusResponse(memberStatusRequest.getUserId(), 0, "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MemberStatusResponse> getMemberStatus(@PathVariable UUID userId) {
        try {
            MemberStatusResponse status = memberStatusService.getMemberStatus(userId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            logger.error("Error occurred while get member status", e);
            MemberStatusResponse errorResponse = new MemberStatusResponse(userId, 0, "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
