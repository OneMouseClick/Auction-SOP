package edu.rutmiit.demo.auctionrest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.server.webmvc.GraphiQlHandler;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GraphiQlConfiguration {

    @Bean
    @Order(0)
    public RouterFunction<ServerResponse> graphiQlRouterFunction() {
        RouterFunctions.Builder builder = RouterFunctions.route();

        // Загружаем нашу HTML-страницу из classpath (из папки static)
        ClassPathResource graphiQlPage = new ClassPathResource("static/graphiql.html");

        // Создаем обработчик. Второй аргумент (пустая строка) — это путь,
        // по которому доступно наше GraphQL API (мы оставили его по умолчанию: /graphql)
        GraphiQlHandler graphiQLHandler = new GraphiQlHandler("/graphql", "", graphiQlPage);

        // Привязываем обработчик к адресу /my-graphiql
        builder = builder.GET("/my-graphiql", graphiQLHandler::handleRequest);

        return builder.build();
    }
}