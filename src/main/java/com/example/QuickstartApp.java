package com.example;

import akka.NotUsed;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.server.Route;
import com.example.api.user.Command;
import com.example.api.user.UserRegistry;
import com.example.api.user.UserRoutes;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;

//https://developer.lightbend.com/guides/akka-http-quickstart-java/
public class QuickstartApp {
  static void startHttpServer(Route route, ActorSystem<?> system) {
    CompletionStage<ServerBinding> futureBinding = Http.get(system).newServerAt("localhost", 8080).bind(route);

    futureBinding.whenComplete((binding, exception) -> {
      if (binding != null) {
        InetSocketAddress address = binding.localAddress();
        system.log().info("Server online at http://{}:{}/",
          address.getHostString(),
          address.getPort());
      } else {
        system.log().error("Failed to bind HTTP endpoint, terminating system", exception);
        system.terminate();
      }
    });
  }

  public static void main(String[] args) throws Exception {
    Behavior<NotUsed> rootBehavior = Behaviors.setup(context -> {
      ActorRef<Command> userRegistryActor = context.spawn(UserRegistry.create(), "UserRegistry");

      System.out.println(context.getSystem());
      System.out.println(userRegistryActor);
      System.out.println(Arrays.toString(context.getChildren().toArray()));
      UserRoutes userRoutes = new UserRoutes(context.getSystem(), userRegistryActor);
      startHttpServer(userRoutes.userRoutes(), context.getSystem());

      return Behaviors.empty();
    });

    // boot up server using the route as defined below
    ActorSystem system = ActorSystem.create(rootBehavior, "HelloAkkaHttpServer");
    System.out.println(system);
  }

}


