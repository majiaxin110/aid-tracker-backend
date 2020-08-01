package org.aidtracker.backend.dao.util;

import com.google.common.collect.Lists;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * @author mtage
 * @since 2020/7/25 13:24
 */
public class SimpleStringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute == null ? null : String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Lists.newArrayList(dbData.split(","));
    }
}
