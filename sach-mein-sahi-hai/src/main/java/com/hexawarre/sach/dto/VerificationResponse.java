package com.hexawarre.sach.dto;

import com.hexawarre.sach.entity.Verdict;

import java.util.List;

public record VerificationResponse(
        String claim,
        String category,
        Verdict verdict,
        int confidence,
        String evidenceStrength,
        List<String> redFlags,
        List<String> whatToDo,
        String note) {
}
