package com.quanxiaoha.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.injector.methods.SelectList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.quanxiaoha.weblog.common.domain.dos.StatisticsArticlePVDO;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsArticlePVMapper extends BaseMapper<StatisticsArticlePVDO> {

    /**
     * 统计每天文章的浏览量s
     * @param date
     * @return
     */
    default int increasePVCount(LocalDate date){
        return update(null, Wrappers.<StatisticsArticlePVDO>lambdaUpdate()
                .setSql("pv_count = pv_count + 1")
                .eq(StatisticsArticlePVDO::getPvDate,date));
    }


    /**
     * 查询近一周每天的浏览量
     */
    default List<StatisticsArticlePVDO> selectLatestWeekRecords(){
        return selectList(Wrappers.<StatisticsArticlePVDO>lambdaQuery()
                .le(StatisticsArticlePVDO::getPvDate,LocalDate.now().plusDays(1))
                .orderByDesc(StatisticsArticlePVDO::getPvDate)
                .last("limit 7"));
    }
}