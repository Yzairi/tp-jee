package fr.lbenoit.formation.blog.dto;

import java.util.ArrayList;
import java.util.List;

public class PostDto {
    private Long id;
    private String titre;
    private String contenu;
    private List<CommentDto> commentaires = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public List<CommentDto> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<CommentDto> commentaires) {
        this.commentaires = commentaires;
    }
}
