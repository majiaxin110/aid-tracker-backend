package org.aidtracker.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

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
@Builder
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

    /**
     * 返回contactList列表内第一个电话号码的项
     * @param contactList
     * @return 若均为微信，返回为null
     */
    public static String getPhoneNumFromList(List<Contact> contactList) {
        return contactList.stream().filter(c -> c.type == ContactTypeEnum.PHONE)
                .findFirst().map(Contact::getContactInfo).orElse(null);
    }
}
