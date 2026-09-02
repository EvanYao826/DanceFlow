package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("club_member")
public class ClubMember {
    @TableId
    private Long id;
    private Long userId;
    private String danceType;
    private String skillLevel;
    private LocalDate joinDate;
    private String memberStatus;
    private String bio;
    private String auditReason;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
