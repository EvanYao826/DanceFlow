package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.danceflow.common.ResultCode;
import com.danceflow.dto.DictUpdateRequest;
import com.danceflow.entity.SysDict;
import com.danceflow.exception.BusinessException;
import com.danceflow.mapper.SysDictMapper;
import com.danceflow.vo.DictVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictService {
    private final SysDictMapper mapper;
    public DictService(SysDictMapper mapper) { this.mapper = mapper; }
    public List<DictVO> list(String type) { return mapper.selectList(new LambdaQueryWrapper<SysDict>().eq(SysDict::getDictType, type).eq(SysDict::getIsDeleted, 0).eq(SysDict::getStatus, 1).orderByAsc(SysDict::getSortNo)).stream().map(DictVO::from).toList(); }
    public DictVO update(Long id, DictUpdateRequest request) { SysDict dict = mapper.selectById(id); if (dict == null || Integer.valueOf(1).equals(dict.getIsDeleted())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "字典项不存在"); dict.setDictLabel(request.dictLabel()); dict.setDictValue(request.dictValue()); dict.setSortNo(request.sortNo()); dict.setStatus(request.status()); mapper.updateById(dict); return DictVO.from(dict); }
}
