package fr.lbenoit.formation.blog.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import fr.lbenoit.formation.blog.model.Post;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/posts")
public class PostController {

    HashMap<Integer, Post> table = new HashMap<>();

    public PostController() {
        Post p = new Post();
        p.setId(12);
        p.setTitre("Mon  titre");
        p.setContenu("Mon contenu");

        table.put(12, p);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post getBillet(@PathParam("id") int identifiant) {
        return table.get(identifiant);
    
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Post creerBillet(Post nouveauBillet) {
        Random r = new Random();
        int prochainId = r.nextInt(100);

        nouveauBillet.setId(prochainId);
        table.put(prochainId, nouveauBillet);
        return nouveauBillet;
    
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Post> getTousBillet() {
        return table.values();
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void supprimerBillet(@PathParam("id") int identifiant) {
        table.remove(identifiant);
    }

}
