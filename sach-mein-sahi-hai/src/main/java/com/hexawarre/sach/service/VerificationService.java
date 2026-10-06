package com.hexawarre.sach.service;

import com.hexawarre.sach.dto.VerificationResponse;
import com.hexawarre.sach.entity.Verdict;
import com.hexawarre.sach.entity.VerificationHistory;
import com.hexawarre.sach.repository.VerificationHistoryRepository;
import com.hexawarre.sach.service.ScamDetectionService.ScamAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificationService {

    private static final Logger log = LoggerFactory.getLogger(VerificationService.class);
    private static final int MAX_CLAIM_LENGTH = 160;

    private final ScamDetectionService scamDetectionService;
    private final VerificationHistoryRepository historyRepository;

    public VerificationService(ScamDetectionService scamDetectionService,
                               VerificationHistoryRepository historyRepository) {
        this.scamDetectionService = scamDetectionService;
        this.historyRepository = historyRepository;
    }

    public VerificationResponse verify(String text) {
        String claim = extractClaim(text);
        ScamAnalysis analysis = scamDetectionService.analyse(text);
        Verdict verdict = decideVerdict(analysis.score());

        historyRepository.save(new VerificationHistory(claim, analysis.category(), verdict));
        log.info("Checked claim, category={}, verdict={}", analysis.category(), verdict);

        return new VerificationResponse(
                claim,
                analysis.category(),
                verdict,
                confidenceFor(verdict, analysis.score()),
                evidenceStrengthFor(verdict),
                analysis.flags(),
                List.of(
                        "Do not pay or share personal details.",
                        "Check the offer on the official website. Type the address yourself.",
                        "If you already paid, call your bank or UPI app now and report at cybercrime.gov.in or call 1930."),
                "This is an assessment based on warning patterns. Live source research is not connected yet.");
    }

    private String extractClaim(String text) {
        String cleaned = text.strip().replaceAll("\\s+", " ");
        return cleaned.length() > MAX_CLAIM_LENGTH ? cleaned.substring(0, MAX_CLAIM_LENGTH) : cleaned;
    }

    private Verdict decideVerdict(int score) {
        if (score >= 55) {
            return Verdict.HIGH_RISK;
        }
        if (score >= 28) {
            return Verdict.SUSPICIOUS;
        }
        if (score > 0) {
            return Verdict.PARTIALLY_VERIFIED;
        }
        return Verdict.CANNOT_VERIFY;
    }

    private int confidenceFor(Verdict verdict, int score) {
        if (verdict == Verdict.CANNOT_VERIFY) {
            return 35;
        }
        return Math.min(92, 50 + score / 2);
    }

    private String evidenceStrengthFor(Verdict verdict) {
        return switch (verdict) {
            case HIGH_RISK -> "Moderate (pattern based)";
            case CANNOT_VERIFY -> "No reliable evidence";
            default -> "Weak";
        };
    }
}
