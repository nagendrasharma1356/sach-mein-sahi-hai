package com.hexawarre.sach.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_history")
public class VerificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String claim;

    private String category;

    @Enumerated(EnumType.STRING)
    private Verdict verdict;

    private LocalDateTime checkedAt = LocalDateTime.now();

    public VerificationHistory() {
    }

    public VerificationHistory(String claim, String category, Verdict verdict) {
        this.claim = claim;
        this.category = category;
        this.verdict = verdict;
    }

    public Long getId() {
        return id;
    }

    public String getClaim() {
        return claim;
    }

    public String getCategory() {
        return category;
    }

    public Verdict getVerdict() {
        return verdict;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }
}
