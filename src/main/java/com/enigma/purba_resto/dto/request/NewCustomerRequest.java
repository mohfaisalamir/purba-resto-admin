package com.enigma.purba_resto.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class NewCustomerRequest {
    @NotBlank(message = "name is required")
    private String name;
    //@NotBlank(message = "email is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    private String email;
    @NotNull(message = "Phone Number is required")@Pattern(regexp = "^\\+?\\d{8,14}$", message = "Invalid phone number format")
    @Size(min = 8, max = 14, message = "Invalid Phone number")
    private String phone;
}
