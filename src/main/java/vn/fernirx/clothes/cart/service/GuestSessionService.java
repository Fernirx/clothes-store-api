package vn.fernirx.clothes.cart.service;

import vn.fernirx.clothes.cart.entity.GuestSession;

public interface GuestSessionService {
    GuestSession getOrCreate(String guestToken);
}
