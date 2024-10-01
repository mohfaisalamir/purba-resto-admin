package com.enigma.purba_resto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminResponse {
    private String adminId;
    private String name;
    private String userCredentialId;
}
