package com.quanxiaoha.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;

import java.time.LocalDate;

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
}
