package com.enigma.purba_resto.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewMenuRequest {
    @NotBlank(message = "name is required, HEH") // artinya : field ini tidak boleh kosong(""/null) dan ini khusus data String
    private String name;
    @NotNull(message = "price is required, SU") // ini untuk  data apapun, Number, Boolean, String dll.(lebih umum dari @NotBlank)
    @Min(value = 0, message = "price must be greater than or equal zero(0), CUK")
    private Long price;
}
