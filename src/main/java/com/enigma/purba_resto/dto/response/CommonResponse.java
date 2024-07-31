package com.enigma.purba_resto.dto.response;

import com.enigma.purba_resto.entity.Customer;
import com.enigma.purba_resto.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResponse<T> { //CommonResponse<T>  ini dibuat di controller saja biasanya
    private String message;
    private Integer statusCode;
    private T data;
    private PagingResponse pagingResponse;
}

