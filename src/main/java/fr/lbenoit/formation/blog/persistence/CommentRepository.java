package fr.lbenoit.formation.blog.persistence;

import fr.lbenoit.formation.blog.model.Comment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {
}
