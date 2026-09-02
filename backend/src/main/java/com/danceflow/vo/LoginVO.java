package com.danceflow.vo;

import java.util.List;

public record LoginVO(String token, long expiresIn, UserVO user, List<String> permissions) {
}
