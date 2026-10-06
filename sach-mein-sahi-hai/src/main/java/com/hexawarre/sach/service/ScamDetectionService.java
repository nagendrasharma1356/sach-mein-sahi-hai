package com.hexawarre.sach.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScamDetectionService {

    public record ScamAnalysis(int score, String category, List<String> flags) {
    }

    private static final Pattern URL_PATTERN = Pattern.compile("https?://\\S+|www\\.\\S+", Pattern.CASE_INSENSITIVE);
    private static final Pattern GOV_DOMAIN = Pattern.compile("\\.(gov|nic)\\.in", Pattern.CASE_INSENSITIVE);

    private static final List<ScamRule> RULES = List.of(
            ScamRule.of("registration fee|processing fee|advance|deposit|payment|paisa bhej",
                    25, "Scam/Fraud", "Payment requested upfront"),
            ScamRule.of("\\botp\\b|upi pin|cvv|password|aadhaar|aadhar",
                    25, "Scam/Fraud", "Asks for OTP, PIN or ID details"),
            ScamRule.of("guarantee|assured|risk[- ]free|\\d+\\s?%\\s*(monthly|per month|daily|weekly)",
                    30, "Investment", "Guaranteed or unrealistic returns"),
            ScamRule.of("urgent|last date|today only|hurry|limited seats|jaldi",
                    12, "Other", "Pressure to act quickly"),
            ScamRule.of("100\\s?%\\s*cure|miracle|permanent cure|completely cures",
                    30, "Medical/Health", "Miracle or 100% cure claim"),
            ScamRule.of("work from home|earn\\s*(₹|rs)\\s?[\\d,]+|data entry|no experience",
                    18, "Job/Employment", "Unrealistic job or salary offer"),
            ScamRule.of("iphone|50\\s?%\\s*below|90\\s?%\\s*off|₹\\s?9,?999",
                    18, "Online Shopping", "Price looks too good to be true"),
            ScamRule.of("government|sarkar|yojana|scheme|subsidy|every (citizen|woman)",
                    8, "Government Scheme", "Mentions a government scheme, no official match checked")
    );

    public ScamAnalysis analyse(String text) {
        int score = 0;
        List<String> flags = new ArrayList<>();
        Map<String, Integer> categoryScores = new HashMap<>();

        for (ScamRule rule : RULES) {
            if (rule.matches(text)) {
                score += rule.weight();
                flags.add(rule.flag());
                categoryScores.merge(rule.category(), rule.weight(), Integer::sum);
            }
        }

        Matcher url = URL_PATTERN.matcher(text);
        if (url.find() && !GOV_DOMAIN.matcher(url.group()).find()) {
            score += 15;
            flags.add("Link is not a .gov.in or .nic.in website");
        }

        return new ScamAnalysis(score, topCategory(categoryScores), flags);
    }

    private String topCategory(Map<String, Integer> categoryScores) {
        String best = "Other";
        int bestScore = 0;
        for (Map.Entry<String, Integer> entry : categoryScores.entrySet()) {
            if (entry.getValue() > bestScore) {
                best = entry.getKey();
                bestScore = entry.getValue();
            }
        }
        return best;
    }
}
