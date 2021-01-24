package com.example.api.user;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import akka.http.javadsl.marshallers.jackson.Jackson;

import static akka.http.javadsl.server.Directives.*;
import static akka.http.javadsl.server.Directives.get;

import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Directives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.japi.function.Function;
import com.example.api.user.msg.ActionPerformed;
import com.example.api.user.msg.CreateUser;
import com.example.api.user.msg.DeleteUser;
import com.example.api.user.msg.GetUser;
import com.example.api.user.msg.GetUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Routes can be defined in separated classes like shown in here
 */
//#user-routes-class
public class UserRoutes {
  //#user-routes-class
  private final static Logger log = LoggerFactory.getLogger(UserRoutes.class);
  private final ActorRef<Command> userRegistryActor;
  private final Duration askTimeout;
  private final Scheduler scheduler;

  public UserRoutes(ActorSystem<?> system, ActorRef<Command> userRegistryActor) {
    this.userRegistryActor = userRegistryActor;
    scheduler = system.scheduler();
    askTimeout = system.settings().config().getDuration("my-app.routes.ask-timeout");
  }

  private CompletionStage<GetUserResponse> getUser(String name) {
    //send ask type message GetUser to userRegistryActor
    return AskPattern.ask(userRegistryActor, new Function<ActorRef<GetUserResponse>, Command>() {
      @Override
      public Command apply(ActorRef<GetUserResponse> ref) throws Exception {
        log.info("sender: {}, receiver: {}",userRegistryActor, ref);
        return new GetUser(name, ref);
      }
    }, askTimeout, scheduler);
  }

  private CompletionStage<ActionPerformed> deleteUser(String name) {
    return AskPattern.ask(userRegistryActor, ref -> new DeleteUser(name, ref), askTimeout, scheduler);
  }

  private CompletionStage<Users> getUsers() {
    return AskPattern.ask(userRegistryActor, GetUsers::new, askTimeout, scheduler);
  }

  private CompletionStage<ActionPerformed> createUser(User user) {

    return AskPattern.ask(userRegistryActor, new Function<ActorRef<ActionPerformed>, Command>() {
      @Override
      public Command apply(ActorRef<ActionPerformed> ref) throws Exception, Exception {
        log.info("sender: {}, receiver: {}",userRegistryActor, ref);
        return new CreateUser(user, ref);
      }
    }, askTimeout, scheduler);
  }

  /**
   * This method creates one route (of possibly many more that will be part of your Web App)
   */
  //#all-routes
  //todo 当有大量api时，这种风格很难维护
  public Route userRoutes() {
    return pathPrefix("users", () ->
      concat(
        pathEnd(() -> concat(
          get(() -> onSuccess(getUsers(), users -> complete(StatusCodes.OK, users, Jackson.marshaller()))),
          post(() -> entity(Jackson.unmarshaller(User.class), user -> onSuccess(createUser(user), performed -> {
            log.info("Create result: {}", performed.description);
            return complete(StatusCodes.CREATED, performed, Jackson.marshaller());
          }))))),
        path(PathMatchers.segment(), (String name) -> concat(
          get(() -> onSuccess(getUser(name), performed -> {
              System.out.println(PathMatchers.segment().toString());
              log.info("Get User: {},  {}", name, performed.maybeUser.toString());
              if (performed.maybeUser.isPresent()) {
                return complete(StatusCodes.OK, performed.maybeUser.get(), Jackson.marshaller());
              } else {
                return complete(StatusCodes.BAD_REQUEST, "user not exist", Jackson.marshaller());
              }
            }
          )),
          delete(() -> onSuccess(deleteUser(name), performed -> {
              log.info("Delete result: {}", performed.description);
              return complete(StatusCodes.OK, performed, Jackson.marshaller());
            }
          ))))
      )
    );
  }
  //#all-routes

}
