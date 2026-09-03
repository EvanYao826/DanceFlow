package com.danceflow.vo;

public record FileUploadVO(String originalName, String url, String storageKey, String contentType, long size) {
}
