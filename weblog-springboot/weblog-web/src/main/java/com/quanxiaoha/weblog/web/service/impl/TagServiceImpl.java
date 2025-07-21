package com.quanxiaoha.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.quanxiaoha.weblog.common.domain.dos.TagDO;
import com.quanxiaoha.weblog.common.domain.mapper.TagMapper;
import com.quanxiaoha.weblog.common.utils.Response;
import com.quanxiaoha.weblog.web.model.vo.tag.FindTagListRspVO;
import com.quanxiaoha.weblog.web.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public Response findTagList() {
        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.emptyWrapper());

        //封装返回值类型
        List<FindTagListRspVO> vos = null;
        if(CollectionUtils.isNotEmpty(tagDOS)){
            vos = tagDOS.stream().map(tagDO -> FindTagListRspVO.builder()
                    .id(tagDO.getId())
                    .name(tagDO.getName())
                    .build())
                    .collect(Collectors.toList());
        }
        return Response.success(vos);
    }
}
