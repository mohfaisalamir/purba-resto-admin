package com.enigma.purba_resto.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAdminRequest {
    @NotBlank(message = "id is required")
    private String id;
    @NotBlank(message = "name is required")
    private String name;
}

