package org.aidtracker.backend.dao.util;

import com.google.common.collect.Lists;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author mtage
 * @since 2020/7/25 13:24
 */
public class SimpleStringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData)) {
            return new ArrayList<>();
        }
        return Lists.newArrayList(dbData.split(","));
    }
}
