package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danceflow.common.PageResult;
import com.danceflow.common.ResultCode;
import com.danceflow.dto.MemberApplyRequest;
import com.danceflow.dto.MemberAuditRequest;
import com.danceflow.dto.MemberStatusRequest;
import com.danceflow.dto.MemberUpdateRequest;
import com.danceflow.entity.ClubMember;
import com.danceflow.entity.User;
import com.danceflow.exception.BusinessException;
import com.danceflow.mapper.ClubMemberMapper;
import com.danceflow.mapper.UserMapper;
import com.danceflow.vo.MemberVO;
import com.danceflow.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class MemberService {
    private final ClubMemberMapper memberMapper;
    private final UserMapper userMapper;

    public MemberService(ClubMemberMapper memberMapper, UserMapper userMapper) {
        this.memberMapper = memberMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public MemberVO apply(Long userId, MemberApplyRequest request) {
        if (findByUserId(userId) != null) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "你已经提交过入社申请");
        ClubMember member = new ClubMember();
        member.setUserId(userId);
        member.setDanceType(request.danceType());
        member.setSkillLevel(request.skillLevel());
        member.setBio(request.bio());
        member.setMemberStatus("PENDING");
        member.setIsDeleted(0);
        memberMapper.insert(member);
        return toVO(member);
    }

    public MemberVO me(Long userId) {
        ClubMember member = findByUserId(userId);
        if (member == null) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "暂未找到社员档案");
        return toVO(member);
    }

    public PageResult<MemberVO> page(long page, long pageSize) {
        Page<ClubMember> result = memberMapper.selectPage(new Page<>(Math.max(page, 1), Math.min(Math.max(pageSize, 1), 100)),
                new LambdaQueryWrapper<ClubMember>().orderByDesc(ClubMember::getCreatedAt));
        return new PageResult<>(result.getRecords().stream().map(this::toVO).toList(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public MemberVO detail(Long id) {
        ClubMember member = memberMapper.selectById(id);
        if (member == null) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "成员不存在");
        return toVO(member);
    }

    @Transactional
    public MemberVO update(Long id, Long operatorId, boolean admin, MemberUpdateRequest request) {
        ClubMember member = required(id);
        if (!admin && !member.getUserId().equals(operatorId)) throw new BusinessException(ResultCode.FORBIDDEN);
        if (!admin && !"PENDING".equals(member.getMemberStatus())) throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "当前状态不能修改档案");
        member.setDanceType(request.danceType());
        member.setSkillLevel(request.skillLevel());
        member.setBio(request.bio());
        memberMapper.updateById(member);
        return toVO(member);
    }

    @Transactional
    public MemberVO audit(Long id, MemberAuditRequest request) {
        ClubMember member = required(id);
        if (!"PENDING".equals(member.getMemberStatus())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "该申请已经审核过");
        if (!"ACTIVE".equals(request.status()) && !"REJECTED".equals(request.status())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "审核状态不正确");
        member.setMemberStatus(request.status());
        member.setAuditReason(request.reason());
        if ("ACTIVE".equals(request.status())) member.setJoinDate(LocalDate.now());
        memberMapper.updateById(member);
        return toVO(member);
    }

    @Transactional
    public MemberVO status(Long id, MemberStatusRequest request) {
        ClubMember member = required(id);
        if (!java.util.Set.of("ACTIVE", "REJECTED", "QUIT").contains(request.status())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "成员状态不正确");
        member.setMemberStatus(request.status());
        memberMapper.updateById(member);
        return toVO(member);
    }

    private ClubMember required(Long id) {
        ClubMember member = memberMapper.selectById(id);
        if (member == null) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "成员不存在");
        return member;
    }

    private ClubMember findByUserId(Long userId) {
        return memberMapper.selectOne(new LambdaQueryWrapper<ClubMember>().eq(ClubMember::getUserId, userId).last("LIMIT 1"));
    }

    private MemberVO toVO(ClubMember member) {
        User user = userMapper.selectById(member.getUserId());
        if (user == null) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "成员用户不存在");
        return MemberVO.from(member, UserVO.from(user));
    }
}
