package com.example.api.user.msg;

import com.example.api.user.Command;

public final class ActionPerformed implements Command {
    public final String description;

    public ActionPerformed(String description) {
        this.description = description;
    }
}
