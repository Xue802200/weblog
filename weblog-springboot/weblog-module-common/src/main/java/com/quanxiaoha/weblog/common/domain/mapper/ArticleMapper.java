package com.quanxiaoha.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import com.quanxiaoha.weblog.common.domain.dos.ArticlePublishCountDO;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

public interface ArticleMapper extends BaseMapper<ArticleDO> {

    //分页查询
    default Page<ArticleDO> selectPageList(Long current , Long size, String title, LocalDate startDate, LocalDate endDate){
        Page<ArticleDO> page = new Page<>(current, size);

        LambdaQueryWrapper<ArticleDO> queryWrapper = Wrappers.<ArticleDO>lambdaQuery()
                .like(!StringUtils.isEmpty(title), ArticleDO::getTitle, title)  //title 模糊查询
                .ge(startDate != null, ArticleDO::getCreateTime, startDate) // 大于startDate
                .le(endDate != null, ArticleDO::getCreateTime, endDate)     // 小于endDate
                .orderByDesc(ArticleDO::getCreateTime);  //根据创建时间倒叙排序


        return selectPage(page, queryWrapper);
    }

    /**
     * 查询上一篇文章
     * @param articleId
     * @return
     */
    default ArticleDO selectPreArticle(Long articleId) {
        return selectOne(Wrappers.<ArticleDO>lambdaQuery()
                .orderByAsc(ArticleDO::getId) // 按文章 ID 升序排列
                .gt(ArticleDO::getId, articleId) // 查询比当前文章 ID 大的
                .last("limit 1")); // 第一条记录即为上一篇文章
    }

    /**
     * 查询下一篇文章
     * @param articleId
     * @return
     */
    default ArticleDO selectNextArticle(Long articleId) {
        return selectOne(Wrappers.<ArticleDO>lambdaQuery()
                .orderByDesc(ArticleDO::getId) // 按文章 ID 倒序排列
                .lt(ArticleDO::getId, articleId) // 查询比当前文章 ID 小的
                .last("limit 1")); // 第一条记录即为下一篇文章
    }

    /**
     * 阅读量+1
     * @param articleId
     * @return
     */
    default int increaseReadNum(Long articleId) {
        // 执行 SQL : UPDATE t_article SET read_num = read_num + 1 WHERE (id = XX)
        return update(null, Wrappers.<ArticleDO>lambdaUpdate()
                .setSql("read_num = read_num + 1")
                .eq(ArticleDO::getId, articleId));
    }


    /**
     * 查询所有记录的浏览量
     * @return
     */
    default List<ArticleDO> selectAllReadNum(){
        //设置仅查read_num字段
        return selectList(Wrappers.<ArticleDO>lambdaQuery()
                .select(ArticleDO::getReadNum));
    }

    /**
     * 统计每天的文章发布数量,按日分组
     * @param startDate
     * @param endDate
     * @return
     */
//    @Select("select DATE(create_time) AS date ,COUNT(*) AS count\n " +
//            "from t_article\n " +
//            "where create_time >= #{startDate} and create_time < #{endDate}\n " +
//            "group by DATE(create_time)")
    @Select("SELECT DATE(create_time) AS date, COUNT(*) AS count\n" +
            "FROM t_article\n" +
            "WHERE create_time >= #{startDate} AND create_time < #{endDate}\n" +
            "GROUP BY DATE(create_time)")
    List<ArticlePublishCountDO> selectDateArticlePublishCount(LocalDate startDate, LocalDate endDate);
}
