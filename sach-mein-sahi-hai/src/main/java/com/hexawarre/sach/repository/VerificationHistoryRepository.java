package com.hexawarre.sach.repository;

import com.hexawarre.sach.entity.VerificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerificationHistoryRepository extends JpaRepository<VerificationHistory, Long> {

    List<VerificationHistory> findTop100ByOrderByCheckedAtDesc();
}
