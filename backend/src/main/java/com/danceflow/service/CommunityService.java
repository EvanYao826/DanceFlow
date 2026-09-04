package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danceflow.common.PageResult;
import com.danceflow.common.ResultCode;
import com.danceflow.dto.ForumPostRequest;
import com.danceflow.dto.ForumPostStatusRequest;
import com.danceflow.entity.ForumPost;
import com.danceflow.entity.User;
import com.danceflow.exception.BusinessException;
import com.danceflow.mapper.ForumPostMapper;
import com.danceflow.mapper.UserMapper;
import com.danceflow.vo.ForumPostVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommunityService {
    private final ForumPostMapper postMapper; private final UserMapper userMapper; private final PointService pointService;
    public CommunityService(ForumPostMapper postMapper, UserMapper userMapper, PointService pointService) { this.postMapper = postMapper; this.userMapper = userMapper; this.pointService = pointService; }
    public PageResult<ForumPostVO> publicPage(long page, long pageSize, String keyword, String category) { return page(page, pageSize, keyword, category, "PUBLISHED", false); }
    public PageResult<ForumPostVO> adminPage(long page, long pageSize, String keyword, String status) { return page(page, pageSize, keyword, null, status, true); }
    private PageResult<ForumPostVO> page(long page, long pageSize, String keyword, String category, String status, boolean admin) {
        LambdaQueryWrapper<ForumPost> q = new LambdaQueryWrapper<ForumPost>().eq(ForumPost::getIsDeleted, 0)
                .eq(status != null && !status.isBlank(), ForumPost::getStatus, status)
                .eq(category != null && !category.isBlank(), ForumPost::getCategory, category)
                .and(keyword != null && !keyword.isBlank(), x -> x.like(ForumPost::getTitle, keyword).or().like(ForumPost::getContent, keyword))
                .orderByDesc(ForumPost::getCreatedAt);
        Page<ForumPost> result = postMapper.selectPage(new Page<>(safePage(page), safeSize(pageSize)), q);
        return new PageResult<>(result.getRecords().stream().map(this::vo).toList(), result.getTotal(), result.getCurrent(), result.getSize());
    }
    @Transactional public ForumPostVO detail(Long id, boolean publicOnly) { ForumPost post = required(id); if (publicOnly && !"PUBLISHED".equals(post.getStatus())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "帖子不存在"); post.setViewCount((post.getViewCount() == null ? 0 : post.getViewCount()) + 1); postMapper.updateById(post); return vo(post); }
    @Transactional public ForumPostVO create(Long userId, ForumPostRequest request) { ForumPost post = new ForumPost(); copy(post, request); post.setUserId(userId); post.setStatus("PUBLISHED"); post.setViewCount(0); post.setLikeCount(0); post.setIsDeleted(0); postMapper.insert(post); pointService.grantOnce(userId, "FORUM_POST", 5, "FORUM_POST", post.getId(), "发布帖子：" + post.getTitle()); return vo(post); }
    @Transactional public ForumPostVO update(Long id, Long userId, boolean admin, ForumPostRequest request) { ForumPost post = required(id); checkOwner(post, userId, admin); copy(post, request); postMapper.updateById(post); return vo(post); }
    @Transactional public void delete(Long id, Long userId, boolean admin) { ForumPost post = required(id); checkOwner(post, userId, admin); post.setIsDeleted(1); postMapper.updateById(post); }
    @Transactional public ForumPostVO updateStatus(Long id, ForumPostStatusRequest request) { ForumPost post = required(id); if (!java.util.Set.of("PUBLISHED", "OFFLINE").contains(request.status())) throw new BusinessException("帖子状态不正确"); post.setStatus(request.status()); postMapper.updateById(post); return vo(post); }
    private ForumPost required(Long id) { ForumPost post = postMapper.selectById(id); if (post == null || Integer.valueOf(1).equals(post.getIsDeleted())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "帖子不存在"); return post; }
    private void copy(ForumPost post, ForumPostRequest request) { post.setTitle(request.title()); post.setContent(request.content()); post.setCoverUrl(request.coverUrl()); post.setCategory(request.category()); }
    private void checkOwner(ForumPost post, Long userId, boolean admin) { if (!admin && !post.getUserId().equals(userId)) throw new BusinessException(ResultCode.FORBIDDEN); }
    private ForumPostVO vo(ForumPost post) { User user = userMapper.selectById(post.getUserId()); return ForumPostVO.from(post, user == null ? "未知用户" : user.getNickname()); }
    private long safePage(long page) { return Math.max(page, 1); } private long safeSize(long size) { return Math.min(Math.max(size, 1), 100); }
}
