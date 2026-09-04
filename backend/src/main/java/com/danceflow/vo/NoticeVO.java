package com.danceflow.vo;
import com.danceflow.entity.Notice;
import java.time.LocalDateTime;
public record NoticeVO(Long id, String title, String content, Long publisherId, String publisherName, String publishStatus,
                       LocalDateTime publishTime, Boolean topFlag, LocalDateTime createdAt) {
    public static NoticeVO from(Notice notice, String publisherName) { return new NoticeVO(notice.getId(), notice.getTitle(), notice.getContent(), notice.getPublisherId(), publisherName, notice.getPublishStatus(), notice.getPublishTime(), Integer.valueOf(1).equals(notice.getTopFlag()), notice.getCreatedAt()); }
}
