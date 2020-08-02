package org.aidtracker.backend.web.dto;

import lombok.Data;
import org.aidtracker.backend.domain.CosFile;

/**
 * @author mtage
 * @since 2020/8/2 11:05
 */
@Data
public class CosFileDTO {
    private Long fileId;

    private String name;

    private String comment;

    private String url;

    public static CosFileDTO fromCosFile(CosFile cosFile) {
        CosFileDTO cosFileDTO = new CosFileDTO();
        cosFileDTO.setFileId(cosFile.getFileId());
        cosFileDTO.setName(cosFile.getName());
        cosFileDTO.setComment(cosFile.getComment());
        cosFileDTO.setUrl(cosFile.getUrl());
        return cosFileDTO;
    }
}
