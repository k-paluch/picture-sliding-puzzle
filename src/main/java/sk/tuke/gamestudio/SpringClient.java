package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.consoleui.ConsoleUI;
import sk.tuke.gamestudio.server.service.*;

@Configuration
@SpringBootApplication
@ComponentScan(basePackages = { "sk.tuke.gamestudio" },
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "sk.tuke.gamestudio.server.*"))

public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui) { return args -> ui.run(); }

    @Bean
    public ConsoleUI consoleUI() { return new ConsoleUI(); }

    @Bean
    public RatingService ratingService() { return new RatingServiceRestClient(); }
    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }
    @Bean
    public CommentService commentService() { return new CommentServiceRestClient(); }
}