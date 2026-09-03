package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danceflow.common.PageResult;
import com.danceflow.common.ResultCode;
import com.danceflow.dto.WorkAuditRequest;
import com.danceflow.dto.WorkCommentRequest;
import com.danceflow.dto.WorkRequest;
import com.danceflow.entity.*;
import com.danceflow.exception.BusinessException;
import com.danceflow.mapper.*;
import com.danceflow.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class WorkService {
    private final DanceWorkMapper workMapper;
    private final WorkLikeMapper likeMapper;
    private final WorkCollectionMapper collectionMapper;
    private final WorkCommentMapper commentMapper;
    private final UserMapper userMapper;

    public WorkService(DanceWorkMapper workMapper, WorkLikeMapper likeMapper, WorkCollectionMapper collectionMapper,
                       WorkCommentMapper commentMapper, UserMapper userMapper) {
        this.workMapper = workMapper; this.likeMapper = likeMapper; this.collectionMapper = collectionMapper;
        this.commentMapper = commentMapper; this.userMapper = userMapper;
    }

    public PageResult<WorkVO> publicPage(long page, long pageSize, String danceType, String sortBy, Long userId) {
        LambdaQueryWrapper<DanceWork> query = new LambdaQueryWrapper<DanceWork>().eq(DanceWork::getIsDeleted, 0)
                .eq(DanceWork::getAuditStatus, "PUBLISHED")
                .eq(danceType != null && !danceType.isBlank(), DanceWork::getDanceType, danceType);
        if ("popular".equalsIgnoreCase(sortBy)) query.orderByDesc(DanceWork::getLikeCount, DanceWork::getViewCount);
        else query.orderByDesc(DanceWork::getPublishedTime, DanceWork::getCreatedAt);
        Page<DanceWork> result = workMapper.selectPage(new Page<>(Math.max(page, 1), Math.min(Math.max(pageSize, 1), 100)), query);
        return new PageResult<>(result.getRecords().stream().map(w -> toVO(w, userId)).toList(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public PageResult<WorkVO> adminPage(long page, long pageSize, String status) {
        LambdaQueryWrapper<DanceWork> query = new LambdaQueryWrapper<DanceWork>().eq(DanceWork::getIsDeleted, 0)
                .eq(status != null && !status.isBlank(), DanceWork::getAuditStatus, status).orderByDesc(DanceWork::getCreatedAt);
        Page<DanceWork> result = workMapper.selectPage(new Page<>(Math.max(page, 1), Math.min(Math.max(pageSize, 1), 100)), query);
        return new PageResult<>(result.getRecords().stream().map(w -> toVO(w, null)).toList(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public PageResult<WorkVO> mine(long page, long pageSize, Long userId) {
        LambdaQueryWrapper<DanceWork> query = new LambdaQueryWrapper<DanceWork>().eq(DanceWork::getIsDeleted, 0)
                .eq(DanceWork::getUserId, userId).orderByDesc(DanceWork::getCreatedAt);
        Page<DanceWork> result = workMapper.selectPage(new Page<>(Math.max(page, 1), Math.min(Math.max(pageSize, 1), 100)), query);
        return new PageResult<>(result.getRecords().stream().map(w -> toVO(w, userId)).toList(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Transactional
    public WorkVO create(Long userId, WorkRequest request) {
        if (!Set.of("IMAGE", "VIDEO").contains(request.mediaType().toUpperCase())) throw new BusinessException("作品媒体类型不正确");
        DanceWork work = new DanceWork(); copy(work, request); work.setUserId(userId); work.setMediaType(request.mediaType().toUpperCase());
        work.setAuditStatus("PENDING"); work.setLikeCount(0); work.setCommentCount(0); work.setCollectionCount(0); work.setViewCount(0); work.setIsDeleted(0);
        workMapper.insert(work); return toVO(work, userId);
    }

    public WorkVO detail(Long id, Long userId, boolean publicOnly) {
        DanceWork work = required(id);
        if (publicOnly && !"PUBLISHED".equals(work.getAuditStatus())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "作品不存在");
        if (publicOnly) { work.setViewCount((work.getViewCount() == null ? 0 : work.getViewCount()) + 1); workMapper.updateById(work); }
        return toVO(work, userId);
    }

    @Transactional public WorkVO update(Long id, Long userId, boolean admin, WorkRequest request) {
        DanceWork work = required(id); checkOwner(work, userId, admin); copy(work, request); work.setMediaType(request.mediaType().toUpperCase());
        if (!admin) { work.setAuditStatus("PENDING"); work.setAuditReason(null); work.setPublishedTime(null); }
        workMapper.updateById(work); return toVO(work, userId);
    }

    @Transactional public void delete(Long id, Long userId, boolean admin) { DanceWork work = required(id); checkOwner(work, userId, admin); work.setIsDeleted(1); workMapper.updateById(work); }

    @Transactional public WorkActionVO toggleLike(Long id, Long userId) { requiredPublic(id); boolean active = toggleLikeRow(id, userId); refreshCounts(id); DanceWork w = required(id); return new WorkActionVO(active, isCollected(id, userId), w.getLikeCount(), w.getCollectionCount()); }
    @Transactional public WorkActionVO toggleCollection(Long id, Long userId) { requiredPublic(id); boolean active = toggleCollectionRow(id, userId); refreshCounts(id); DanceWork w = required(id); return new WorkActionVO(isLiked(id, userId), active, w.getLikeCount(), w.getCollectionCount()); }

    public List<WorkCommentVO> comments(Long id) { requiredPublic(id); return commentMapper.selectList(new LambdaQueryWrapper<WorkComment>().eq(WorkComment::getWorkId, id).eq(WorkComment::getStatus, "NORMAL").eq(WorkComment::getIsDeleted, 0).orderByAsc(WorkComment::getCreatedAt)).stream().map(this::commentVO).toList(); }
    @Transactional public WorkCommentVO comment(Long id, Long userId, WorkCommentRequest request) {
        requiredPublic(id); Long parentId = request.parentId() == null ? 0L : request.parentId();
        if (parentId != 0) { WorkComment parent = commentMapper.selectById(parentId); if (parent == null || !id.equals(parent.getWorkId()) || parent.getParentId() != 0) throw new BusinessException("只支持两级评论"); }
        if (containsSensitive(request.content())) throw new BusinessException("评论包含不适宜内容");
        WorkComment c = new WorkComment(); c.setWorkId(id); c.setUserId(userId); c.setParentId(parentId); c.setContent(request.content()); c.setStatus("NORMAL"); c.setIsDeleted(0); commentMapper.insert(c);
        DanceWork work = required(id); work.setCommentCount((work.getCommentCount() == null ? 0 : work.getCommentCount()) + 1); workMapper.updateById(work); return commentVO(c);
    }
    @Transactional public void deleteComment(Long commentId, Long userId, boolean admin) { WorkComment c = commentMapper.selectById(commentId); if (c == null || Integer.valueOf(1).equals(c.getIsDeleted())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "评论不存在"); if (!admin && !userId.equals(c.getUserId())) throw new BusinessException(ResultCode.FORBIDDEN); c.setStatus("HIDDEN"); c.setIsDeleted(1); commentMapper.updateById(c); }
    @Transactional public WorkVO audit(Long id, WorkAuditRequest request) { DanceWork w = required(id); if (!Set.of("PUBLISHED", "REJECTED", "OFFLINE").contains(request.status())) throw new BusinessException("审核状态不正确"); w.setAuditStatus(request.status()); w.setAuditReason(request.reason()); if ("PUBLISHED".equals(request.status())) w.setPublishedTime(LocalDateTime.now()); workMapper.updateById(w); return toVO(w, null); }

    private boolean toggleLikeRow(Long workId, Long userId) { WorkLike row = likeMapper.selectOne(pairLike(workId, userId)); if (row == null) { row = new WorkLike(); row.setWorkId(workId); row.setUserId(userId); row.setIsDeleted(0); likeMapper.insert(row); return true; } row.setIsDeleted(Integer.valueOf(1).equals(row.getIsDeleted()) ? 0 : 1); likeMapper.updateById(row); return Integer.valueOf(0).equals(row.getIsDeleted()); }
    private boolean toggleCollectionRow(Long workId, Long userId) { WorkCollection row = collectionMapper.selectOne(pairCollection(workId, userId)); if (row == null) { row = new WorkCollection(); row.setWorkId(workId); row.setUserId(userId); row.setIsDeleted(0); collectionMapper.insert(row); return true; } row.setIsDeleted(Integer.valueOf(1).equals(row.getIsDeleted()) ? 0 : 1); collectionMapper.updateById(row); return Integer.valueOf(0).equals(row.getIsDeleted()); }
    private void refreshCounts(Long id) { DanceWork w = required(id); w.setLikeCount(likeMapper.selectCount(new LambdaQueryWrapper<WorkLike>().eq(WorkLike::getWorkId,id).eq(WorkLike::getIsDeleted,0)).intValue()); w.setCollectionCount(collectionMapper.selectCount(new LambdaQueryWrapper<WorkCollection>().eq(WorkCollection::getWorkId,id).eq(WorkCollection::getIsDeleted,0)).intValue()); workMapper.updateById(w); }
    private boolean isLiked(Long id, Long uid) { return likeMapper.selectCount(pairLike(id,uid).eq(WorkLike::getIsDeleted,0)) > 0; }
    private boolean isCollected(Long id, Long uid) { return collectionMapper.selectCount(pairCollection(id,uid).eq(WorkCollection::getIsDeleted,0)) > 0; }
    private LambdaQueryWrapper<WorkLike> pairLike(Long id, Long uid) { return new LambdaQueryWrapper<WorkLike>().eq(WorkLike::getWorkId,id).eq(WorkLike::getUserId,uid).last("LIMIT 1"); }
    private LambdaQueryWrapper<WorkCollection> pairCollection(Long id, Long uid) { return new LambdaQueryWrapper<WorkCollection>().eq(WorkCollection::getWorkId,id).eq(WorkCollection::getUserId,uid).last("LIMIT 1"); }
    private DanceWork required(Long id) { DanceWork w = workMapper.selectById(id); if (w == null || Integer.valueOf(1).equals(w.getIsDeleted())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "作品不存在"); return w; }
    private DanceWork requiredPublic(Long id) { DanceWork w = required(id); if (!"PUBLISHED".equals(w.getAuditStatus())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "作品不存在"); return w; }
    private void checkOwner(DanceWork w, Long uid, boolean admin) { if (!admin && !w.getUserId().equals(uid)) throw new BusinessException(ResultCode.FORBIDDEN); }
    private void copy(DanceWork w, WorkRequest r) { w.setTitle(r.title()); w.setCoverUrl(r.coverUrl()); w.setMediaUrl(r.mediaUrl()); w.setDescription(r.description()); w.setDanceType(r.danceType()); }
    private WorkVO toVO(DanceWork w, Long uid) { User u = userMapper.selectById(w.getUserId()); return new WorkVO(w.getId(),w.getUserId(),u == null ? "未知用户" : u.getNickname(),w.getTitle(),w.getCoverUrl(),w.getMediaUrl(),w.getMediaType(),w.getDescription(),w.getDanceType(),w.getAuditStatus(),w.getAuditReason(),w.getLikeCount(),w.getCommentCount(),w.getCollectionCount(),w.getViewCount(),uid == null ? false : isLiked(w.getId(),uid),uid == null ? false : isCollected(w.getId(),uid),w.getPublishedTime(),w.getCreatedAt()); }
    private WorkCommentVO commentVO(WorkComment c) { User u = userMapper.selectById(c.getUserId()); return new WorkCommentVO(c.getId(),c.getWorkId(),c.getUserId(),u == null ? "未知用户" : u.getNickname(),c.getParentId(),c.getContent(),c.getCreatedAt()); }
    private boolean containsSensitive(String content) { return content.contains("赌博") || content.contains("诈骗") || content.contains("辱骂"); }
}
