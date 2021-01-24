package com.example.api.user;

import java.util.Optional;

public final class GetUserResponse {
    public final Optional<User> maybeUser;

    public GetUserResponse(Optional<User> maybeUser) {
        this.maybeUser = maybeUser;
    }
}
