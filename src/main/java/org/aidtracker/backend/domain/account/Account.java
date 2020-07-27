package org.aidtracker.backend.domain.account;

import lombok.Data;
import org.aidtracker.backend.domain.Contact;

import javax.persistence.*;

/**
 * 用户账户
 * 唯一索引：微信id 账户角色类型 —— 要求同一个微信用户每种账户唯一
 * @author mtage
 * @since 2020/7/24 15:14
 */
@Entity
@Table(name = "account", uniqueConstraints = {
        @UniqueConstraint(name="uniq_wechatid_role", columnNames = {"wechat_openid", "role"})
})
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;

    private String name;

    /**
     * 资质
     */
    private String qualification;

    /**
     * 地区代码
     * @see <a href="https://lbs.qq.com/service/webService/webServiceGuide/webServiceDistrict">行政区划代码</href>
     */
    private String areaAdCode;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private AccountRoleEnum role;

    /**
     * 微信账号id
     * 注意目前业务较为简单，因此直接关联微信id
     */
    @Column(name = "wechat_openid", nullable = false)
    private String wechatOpenId;

    /**
     * 联系方式
     */
    @Embedded
    private Contact contact;
}
