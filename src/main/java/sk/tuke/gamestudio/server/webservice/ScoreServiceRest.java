package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/score")
public class ScoreServiceRest {
    //TODO: pridat deklaraciu premennej pre servisny komponent JPA, anotovanu @Autowired
    @Autowired
    private ScoreService scoreService;
    //http://localhost:8080/rest/score
    @POST
    @Consumes("application/json")
    public Response addScore(Score score) throws ScoreException, SQLException {
        //TODO: pridat score prostrednictvom servisneho komponentu JPA
        scoreService.addScore(score);
        return Response.ok().build();
    }

    //http://localhost:8080/rest/score/mines
    @GET
    @Path("/{game}")
    @Produces("application/json")
    public List<Score> getBestScores(@PathParam("game") String game) throws ScoreException {
        return scoreService.getBestScores("game");
    }
}
