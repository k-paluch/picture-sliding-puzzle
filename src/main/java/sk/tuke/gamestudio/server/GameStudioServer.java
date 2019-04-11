package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import sk.tuke.gamestudio.server.service.*;

@SpringBootApplication
@EntityScan({"sk.tuke.gamestudio.server.entity"})
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean(name="scoreServiceServer")
    public ScoreService scoreService() {return new ScoreServiceJPA();}
    @Bean(name="commentServiceServer")
    public RatingService ratingService() {return new RatingServiceJPA();}
    @Bean(name="ratingServiceServer")
    public CommentService commentService() {return new CommentServiceJPA();}
}