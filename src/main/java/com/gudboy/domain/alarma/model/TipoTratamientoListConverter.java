package com.gudboy.domain.alarma.model;

import com.gudboy.domain.tratamiento.TipoTratamiento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = false)
public class TipoTratamientoListConverter implements AttributeConverter<List<TipoTratamiento>, String> {

    @Override
    public String convertToDatabaseColumn(List<TipoTratamiento> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<TipoTratamiento> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(dbData.split(","))
                .map(TipoTratamiento::valueOf)
                .collect(Collectors.toList());
    }
}
