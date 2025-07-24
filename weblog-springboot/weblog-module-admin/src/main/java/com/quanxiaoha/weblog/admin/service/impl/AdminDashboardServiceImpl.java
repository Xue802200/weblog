package com.quanxiaoha.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quanxiaoha.weblog.admin.model.vo.dashboard.FindDashboardPVStatisticsInfoRspVO;
import com.quanxiaoha.weblog.common.constant.Constants;
import com.quanxiaoha.weblog.common.domain.dos.ArticleDO;
import com.quanxiaoha.weblog.common.domain.dos.ArticlePublishCountDO;
import com.quanxiaoha.weblog.common.domain.dos.StatisticsArticlePVDO;
import com.quanxiaoha.weblog.common.domain.mapper.ArticleMapper;
import com.quanxiaoha.weblog.common.domain.mapper.CategoryMapper;
import com.quanxiaoha.weblog.common.domain.mapper.StatisticsArticlePVMapper;
import com.quanxiaoha.weblog.common.domain.mapper.TagMapper;
import com.quanxiaoha.weblog.common.utils.Response;
import com.quanxiaoha.weblog.admin.model.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.quanxiaoha.weblog.admin.service.AdminDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private StatisticsArticlePVMapper articlePVMapper;

    /**
     * 获取仪表盘基础统计信息
     * @return
     */
    @Override
    public Response findDashboardStatistics() {
        //查询文章总数
        Long articleCount = articleMapper.selectCount(Wrappers.emptyWrapper());

        //查询分类总数
        Long categoryCount = categoryMapper.selectCount(Wrappers.emptyWrapper());

        //查询标签总数
        Long tagCount = tagMapper.selectCount(Wrappers.emptyWrapper());

        // 总浏览量
        List<ArticleDO> articleDOS = articleMapper.selectAllReadNum();
        Long pvTotalCount = 0L;

        if (!CollectionUtils.isEmpty(articleDOS)) {
            // 所有 read_num 相加
            pvTotalCount = articleDOS.stream().mapToLong(ArticleDO::getReadNum).sum();
        }

        //封装
        return Response.success(
                FindDashboardStatisticsInfoRspVO.builder()
                        .articleTotalCount(articleCount)
                        .categoryTotalCount(categoryCount)
                        .tagTotalCount(tagCount)
                        .pvTotalCount(pvTotalCount)
                        .build()
        );

    }

    /**
     * 获取文章发布详情
     * <KEY,VALUE>的形式来保存,KEY保存时间,VALUE保存每天的浏览量
     * @return
     */
    @Override
    public Response findDashboardPublishArticleStatistics() {

        //获取当前日期和一年之后的日期
        LocalDate currDate = LocalDate.now();
        LocalDate startDate = currDate.minusYears(1L);
        List<ArticlePublishCountDO> articlePublishCountDOS = articleMapper.selectDateArticlePublishCount(startDate,currDate.plusDays(1));

        Map<LocalDate,Long> collect = null;
        if(!CollectionUtils.isEmpty(articlePublishCountDOS)){
            //获取每天对应的浏览量
            Map<LocalDate,Long> dateArticleCountMap = articlePublishCountDOS.stream()
                    .collect(Collectors.toMap(ArticlePublishCountDO::getDate, ArticlePublishCountDO::getCount));

            //返回的Map集合需要按照升序排列
            collect = Maps.newLinkedHashMap();

            //从上一年的今天循环到现在
            for (; startDate.isBefore(currDate); startDate=startDate.plusDays(1)) {
                //以日期作为key从dateArticleCountMap中获取元素
                Long count = dateArticleCountMap.get(startDate);
                //设置到返参map中
                collect.put(startDate, Objects.isNull(count) ? 0L : count);
            }
        }

        log.info("collect count:{}",collect);
        return Response.success(collect);
    }

    /**
     * 查询近七天每天的文章阅读量
     * @return
     */
    @Override
    public Response findDashboardPVStatistics() {
        List<StatisticsArticlePVDO> statisticsArticlePVDOS = articlePVMapper.selectLatestWeekRecords();

        //转Map,方便后续通过date拿到对应的浏览量
        Map<LocalDate, Long> newDateCountMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(statisticsArticlePVDOS)) {
            newDateCountMap = statisticsArticlePVDOS.stream().collect(Collectors.toMap(StatisticsArticlePVDO::getPvDate,StatisticsArticlePVDO::getPvCount));
        }

        FindDashboardPVStatisticsInfoRspVO vo = null;

        //构造返回对象
        List<String> pvDates = Lists.newArrayList();
        List<Long> pvCounts = Lists.newArrayList();

        //获取当天和一周前的日期
        LocalDate currDate = LocalDate.now();
        LocalDate lastWeek = currDate.minusWeeks(1);

        //从一周前开始循环到今天的前一天
        for (; currDate.minusDays(1).isAfter(lastWeek) || currDate.minusDays(1).isEqual(lastWeek); lastWeek = lastWeek.plusDays(1)) {
            pvDates.add(lastWeek.format(Constants.MONTH_DAY_FORMATTER));
            Long count = newDateCountMap.get(lastWeek);
            pvCounts.add(Objects.isNull(count) ? 0L : count);
        }

        vo =  FindDashboardPVStatisticsInfoRspVO.builder()
                .pvDates(pvDates)
                .pvCounts(pvCounts)
                .build();

        return Response.success(vo);
    }
}
