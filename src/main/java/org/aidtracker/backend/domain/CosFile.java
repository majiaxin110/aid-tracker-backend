package org.aidtracker.backend.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author mtage
 * @since 2020/7/25 13:20
 */
@Entity
@Data
public class CosFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fileId;

    private String name;

    private String comment;

    private String url;

    private long accountId;
}
