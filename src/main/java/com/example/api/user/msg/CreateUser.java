package com.example.api.user.msg;

import akka.actor.typed.ActorRef;
import com.example.api.user.Command;
import com.example.api.user.User;
import com.example.api.user.Users;

public final class CreateUser implements Command {
    public final User user;
    public final ActorRef<ActionPerformed> replyTo;

    public CreateUser(User user, ActorRef<ActionPerformed> replyTo) {
        this.user = user;
        this.replyTo = replyTo;
    }

    public void onCreateUser(Users users) {
        users.getUsers().add(this.user);
        this.replyTo.tell(new ActionPerformed(String.format("User %s created.", this.user.name)));
    }
}
