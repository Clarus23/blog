package kr.pe.hw.blog.domain;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_SUPER("super"), ROLE_ADMIN("admin"), ROLE_USER("user");
    private final String description;
    Role(String description) {
        this.description = description;
    }
}
