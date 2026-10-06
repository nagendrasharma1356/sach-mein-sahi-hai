package com.hexawarre.sach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyRequest(
        @NotBlank(message = "Text is required")
        @Size(max = 5000, message = "Text must be under 5000 characters")
        String text) {
}
