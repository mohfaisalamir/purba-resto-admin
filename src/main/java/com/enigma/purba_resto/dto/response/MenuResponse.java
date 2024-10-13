package com.enigma.purba_resto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuResponse {
    private String id;
    private String name;
    private Long price;
    private FileResponse image;
}
