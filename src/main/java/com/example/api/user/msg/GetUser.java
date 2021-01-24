package com.example.api.user.msg;

import akka.actor.typed.ActorRef;
import com.example.api.user.Command;
import com.example.api.user.GetUserResponse;
import com.example.api.user.User;
import com.example.api.user.Users;

import java.util.Optional;

public final class GetUser implements Command {
    private final String name;
    private final ActorRef<GetUserResponse> replyTo;

    public GetUser(String name, ActorRef<GetUserResponse> replyTo) {
        this.name = name;
        this.replyTo = replyTo;
    }

    public void onGetUser(Users users) {
        Optional<User> maybeUser = users.getUsers().stream()
                .filter(user -> user.name.equals(this.name))
                .findFirst();
        this.replyTo.tell(new GetUserResponse(maybeUser));
    }
}
