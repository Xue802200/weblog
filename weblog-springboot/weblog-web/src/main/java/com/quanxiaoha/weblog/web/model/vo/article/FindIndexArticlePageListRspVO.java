package com.quanxiaoha.weblog.web.model.vo.article;

import com.quanxiaoha.weblog.common.domain.dos.CategoryDO;
import com.quanxiaoha.weblog.common.domain.dos.TagDO;
import com.quanxiaoha.weblog.web.model.vo.category.FindCategoryListRspVO;
import com.quanxiaoha.weblog.web.model.vo.tag.FindTagListRspVO;
import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindIndexArticlePageListRspVO {

    private Long id;
    private String cover;
    private String title;
    private LocalDateTime createTime;
    private String summary;

    /**
     * 文章分类
     */
    private FindCategoryListRspVO findCategoryListRspVO;

    /**
     * 文章标签集合
     */
    private List<FindTagListRspVO> findTagListRspVOList;
}
