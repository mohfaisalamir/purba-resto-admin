package com.enigma.purba_resto.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ValidationUtil {
    private final Validator validator;
    @Autowired
    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }
    public <T> void validate(T object){ //<T> agar ValidationUtil , lebih fleksible
        // pakai object karena fleksibel,
        // jika lang sung pakai kelas misal :NewMenu Request,
        // maka akan terlalu banyak validator yang harus kita buat
        Set<ConstraintViolation<T>> result = validator.validate(object);
        if (!result.isEmpty()) {
            throw new ConstraintViolationException(result);
        }

    }
}
