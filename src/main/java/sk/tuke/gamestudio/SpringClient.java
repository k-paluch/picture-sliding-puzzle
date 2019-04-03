package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.consoleui.ConsoleUI;
import sk.tuke.gamestudio.server.service.*;

@Configuration
@SpringBootApplication
class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui) { return args -> ui.run(); }

    @Bean
    public ConsoleUI consoleUI() { return new ConsoleUI(); }

    @Bean
    public RatingService ratingService() { return new RatingServiceJPA(); }
    @Bean
    public ScoreService scoreService() { return new ScoreServiceJPA(); }
    @Bean
    public CommentService commentService() { return new CommentServiceJPA(); }
}