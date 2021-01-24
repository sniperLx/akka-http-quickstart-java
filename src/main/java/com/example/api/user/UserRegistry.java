package com.example.api.user;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.japi.function.Function;
import com.example.api.user.msg.CreateUser;
import com.example.api.user.msg.DeleteUser;
import com.example.api.user.msg.GetUser;
import com.example.api.user.msg.GetUsers;

//#user-registry-actor
public class UserRegistry extends AbstractBehavior<Command> {
  //#user-case-classes

  private Users users;

  private UserRegistry(ActorContext<Command> context) {
    super(context);
    this.users = new Users();
  }

  public static Behavior<Command> create() {
    return Behaviors.setup(UserRegistry::new);
  }

  @Override
  public Receive<Command> createReceive() {
    //receive message and route to handler
    Behavior<Command> registry = this;
    return newReceiveBuilder()
      .onMessage(GetUsers.class, new Function<GetUsers, Behavior<Command>>() {
        @Override
        public Behavior<Command> apply(GetUsers param) throws Exception {
          param.onGetUsers(users);
          return registry;
        }
      })
      .onMessage(CreateUser.class, new Function<CreateUser, Behavior<Command>>() {
        @Override
        public Behavior<Command> apply(CreateUser param) throws Exception {
          param.onCreateUser(users);
          return registry;
        }
      })
      .onMessage(GetUser.class, new Function<GetUser, Behavior<Command>>() {
        @Override
        public Behavior<Command> apply(GetUser param) throws Exception {
          param.onGetUser(users);
          return registry;
        }
      })
      .onMessage(DeleteUser.class, new Function<DeleteUser, Behavior<Command>>() {
        @Override
        public Behavior<Command> apply(DeleteUser param) throws Exception {
          param.onDeleteUser(users);
          return registry;
        }
      })
      .build();
  }
}
//#user-registry-actor