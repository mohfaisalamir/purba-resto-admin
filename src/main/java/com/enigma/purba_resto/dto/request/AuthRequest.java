package com.enigma.purba_resto.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank(message = "username is required")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "invalid username")
    @Size(min = 6, max = 16, message = "must be greater than 6 character and less than 17 character")
    private String username;
    @NotBlank(message = "password is required")
    @Size(min = 6, message = "must be greater than 6 character")
    private String password;

}
