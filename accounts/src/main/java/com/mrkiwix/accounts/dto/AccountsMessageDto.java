package com.mrkiwix.accounts.dto;

public record AccountsMessageDto(
        Long accountNumber,
        String name,
        String email,
        String mobileNumber
) {
}
