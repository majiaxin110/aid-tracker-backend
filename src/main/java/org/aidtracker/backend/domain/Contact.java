package org.aidtracker.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

/**
 * 抽象的联系方式 包含联系人等
 * Immutable Value Object
 * @author mtage
 * @since 2020/7/24 15:06
 */
@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    /**
     * 联系人姓名
     */
    private String contactName;

    @Enumerated(EnumType.STRING)
    private ContactTypeEnum type;

    /**
     * 具体的联系方式
     */
    private String contactInfo;
}
