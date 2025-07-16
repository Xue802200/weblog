package com.quanxiaoha.weblog.admin.model.vo.category;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "删除分类VO")
public class DeleteCategoryReqVO {

    @NotNull(message = "分类id不能为空")
    private Long id;
}
