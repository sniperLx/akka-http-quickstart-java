package com.example.api.user.msg;

import akka.actor.typed.ActorRef;
import com.example.api.user.Command;
import com.example.api.user.Users;

public final class DeleteUser implements Command {
    public final String name;
    public final ActorRef<ActionPerformed> replyTo;

    public DeleteUser(String name, ActorRef<ActionPerformed> replyTo) {
        this.name = name;
        this.replyTo = replyTo;
    }

    public void onDeleteUser(Users users) {
        users.getUsers().removeIf(user -> user.name.equals(this.name));
        this.replyTo.tell(new ActionPerformed(String.format("User %s deleted.", this.name)));
    }
}
