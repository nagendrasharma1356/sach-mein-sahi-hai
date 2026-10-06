package com.hexawarre.sach.controller;

import com.hexawarre.sach.dto.VerificationResponse;
import com.hexawarre.sach.dto.VerifyRequest;
import com.hexawarre.sach.entity.VerificationHistory;
import com.hexawarre.sach.repository.VerificationHistoryRepository;
import com.hexawarre.sach.service.VerificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VerificationController {

    private final VerificationService verificationService;
    private final VerificationHistoryRepository historyRepository;

    public VerificationController(VerificationService verificationService,
                                  VerificationHistoryRepository historyRepository) {
        this.verificationService = verificationService;
        this.historyRepository = historyRepository;
    }

    @PostMapping("/verify")
    public VerificationResponse verify(@Valid @RequestBody VerifyRequest request) {
        return verificationService.verify(request.text());
    }

    @GetMapping("/history")
    public List<VerificationHistory> getHistory() {
        return historyRepository.findTop100ByOrderByCheckedAtDesc();
    }

    @DeleteMapping("/history")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearHistory() {
        historyRepository.deleteAll();
    }
}
