package com.quanxiaoha.weblog.common.domain.dos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticlePublishCountDO {

    /**
     * 文章日期
     */
    private LocalDate date;

    /**
     * 文章浏览量
     */
    private Long count;
}
