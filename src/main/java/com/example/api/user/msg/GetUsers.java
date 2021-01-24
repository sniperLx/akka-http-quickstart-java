package com.example.api.user.msg;

import akka.actor.typed.ActorRef;
import com.example.api.user.Command;
import com.example.api.user.Users;

import java.util.ArrayList;
import java.util.Collections;

public final class GetUsers implements Command {
    private final ActorRef<Users> replyTo;

    public GetUsers(ActorRef<Users> replyTo) {
        this.replyTo = replyTo;
    }

    public void onGetUsers(Users users) {
        // We must be careful not to send out users since it is mutable
        // so for this response we need to make a defensive copy
        this.replyTo.tell(new Users(Collections.unmodifiableList(new ArrayList<>(users.getUsers()))));
    }
}
