package com.danceflow.vo;

import com.danceflow.entity.SysDict;

public record DictVO(Long id, String dictType, String dictLabel, String dictValue, Integer sortNo, Integer status) {
    public static DictVO from(SysDict dict) { return new DictVO(dict.getId(), dict.getDictType(), dict.getDictLabel(), dict.getDictValue(), dict.getSortNo(), dict.getStatus()); }
}
