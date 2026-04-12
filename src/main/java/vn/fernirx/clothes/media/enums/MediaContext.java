package vn.fernirx.clothes.media.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MediaContext {
    AVATAR("avatars"),
    PRODUCT("products"),
    BRAND("brands");

    private final String folder;

    public String getFolder() {
        return "clothes/" + folder;
    }
}
