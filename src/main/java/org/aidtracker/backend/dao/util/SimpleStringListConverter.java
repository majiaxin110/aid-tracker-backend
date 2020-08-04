package org.aidtracker.backend.dao.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.AttributeConverter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mtage
 * @since 2020/7/25 13:24
 */
public class SimpleStringListConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        return CollectionUtils.isEmpty(attribute) ? null : attribute.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        return StringUtils.isBlank(dbData) ? Lists.newArrayList() : Lists.newArrayList(dbData.split(","))
                .stream().map(Long::parseLong).collect(Collectors.toList());
    }
}
