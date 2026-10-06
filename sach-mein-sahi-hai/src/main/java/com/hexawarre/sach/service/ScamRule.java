package com.hexawarre.sach.service;

import java.util.regex.Pattern;

public record ScamRule(Pattern pattern, int weight, String category, String flag) {

    public static ScamRule of(String regex, int weight, String category, String flag) {
        return new ScamRule(Pattern.compile(regex, Pattern.CASE_INSENSITIVE), weight, category, flag);
    }

    public boolean matches(String text) {
        return pattern.matcher(text).find();
    }
}
