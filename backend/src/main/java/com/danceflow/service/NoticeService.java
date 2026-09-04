package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danceflow.common.PageResult;
import com.danceflow.common.ResultCode;
import com.danceflow.dto.NoticeRequest;
import com.danceflow.dto.NoticeStatusRequest;
import com.danceflow.entity.Notice;
import com.danceflow.entity.User;
import com.danceflow.exception.BusinessException;
import com.danceflow.mapper.NoticeMapper;
import com.danceflow.mapper.UserMapper;
import com.danceflow.vo.NoticeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class NoticeService {
    private final NoticeMapper noticeMapper; private final UserMapper userMapper;
    public NoticeService(NoticeMapper noticeMapper, UserMapper userMapper) { this.noticeMapper = noticeMapper; this.userMapper = userMapper; }
    public List<NoticeVO> publicList() { return noticeMapper.selectList(new LambdaQueryWrapper<Notice>().eq(Notice::getIsDeleted, 0).eq(Notice::getPublishStatus, "PUBLISHED").orderByDesc(Notice::getTopFlag).orderByDesc(Notice::getPublishTime)).stream().map(this::vo).toList(); }
    public NoticeVO publicDetail(Long id) { Notice notice = required(id); if (!"PUBLISHED".equals(notice.getPublishStatus())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "公告不存在"); return vo(notice); }
    public PageResult<NoticeVO> adminPage(long page, long pageSize, String status) { Page<Notice> result = noticeMapper.selectPage(new Page<>(Math.max(page, 1), Math.min(Math.max(pageSize, 1), 100)), new LambdaQueryWrapper<Notice>().eq(Notice::getIsDeleted, 0).eq(status != null && !status.isBlank(), Notice::getPublishStatus, status).orderByDesc(Notice::getTopFlag).orderByDesc(Notice::getCreatedAt)); return new PageResult<>(result.getRecords().stream().map(this::vo).toList(), result.getTotal(), result.getCurrent(), result.getSize()); }
    @Transactional public NoticeVO create(Long adminId, NoticeRequest request) { Notice notice = new Notice(); copy(notice, request); notice.setPublisherId(adminId); notice.setPublishStatus("DRAFT"); notice.setIsDeleted(0); noticeMapper.insert(notice); return vo(notice); }
    @Transactional public NoticeVO update(Long id, NoticeRequest request) { Notice notice = required(id); copy(notice, request); noticeMapper.updateById(notice); return vo(notice); }
    @Transactional public NoticeVO updateStatus(Long id, NoticeStatusRequest request) { Notice notice = required(id); if (!Set.of("DRAFT", "PUBLISHED", "OFFLINE").contains(request.status())) throw new BusinessException("公告状态不正确"); notice.setPublishStatus(request.status()); if ("PUBLISHED".equals(request.status()) && notice.getPublishTime() == null) notice.setPublishTime(LocalDateTime.now()); noticeMapper.updateById(notice); return vo(notice); }
    private Notice required(Long id) { Notice notice = noticeMapper.selectById(id); if (notice == null || Integer.valueOf(1).equals(notice.getIsDeleted())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "公告不存在"); return notice; }
    private void copy(Notice notice, NoticeRequest request) { notice.setTitle(request.title()); notice.setContent(request.content()); notice.setTopFlag(Boolean.TRUE.equals(request.topFlag()) ? 1 : 0); }
    private NoticeVO vo(Notice notice) { User user = userMapper.selectById(notice.getPublisherId()); return NoticeVO.from(notice, user == null ? "管理员" : user.getNickname()); }
}
