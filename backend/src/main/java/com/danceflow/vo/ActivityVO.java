package com.danceflow.vo;

import com.danceflow.entity.Activity;

import java.time.LocalDateTime;

public record ActivityVO(Long id, String title, String coverUrl, String description, String activityType,
                         LocalDateTime startTime, LocalDateTime endTime, String location, Integer capacity,
                         LocalDateTime applyDeadline, String status, Long publisherId, String publisherName,
                         Boolean applied, Long appliedCount, Integer remainingCapacity, String applyStatus) {
    public static ActivityVO from(Activity activity, String publisherName) {
        return new ActivityVO(activity.getId(), activity.getTitle(), activity.getCoverUrl(), activity.getDescription(),
                activity.getActivityType(), activity.getStartTime(), activity.getEndTime(), activity.getLocation(),
                activity.getCapacity(), activity.getApplyDeadline(), activity.getStatus(), activity.getPublisherId(), publisherName, null, null, null, null);
    }
}
