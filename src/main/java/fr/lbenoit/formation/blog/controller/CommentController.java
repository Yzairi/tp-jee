package fr.lbenoit.formation.blog.controller;

import java.util.List;
import java.util.stream.Collectors;

import fr.lbenoit.formation.blog.dto.CommentDto;
import fr.lbenoit.formation.blog.model.Comment;
import fr.lbenoit.formation.blog.model.Post;
import fr.lbenoit.formation.blog.persistence.CommentRepository;
import fr.lbenoit.formation.blog.persistence.PostRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/posts/{postId}/comments")
public class CommentController {

    @Inject
    PostRepository postRepository;

    @Inject
    CommentRepository commentRepository;

    private CommentDto toDto(Comment entity) {
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setAuteur(entity.getAuteur());
        dto.setContenu(entity.getContenu());
        return dto;
    }

    private Post findPostOrThrow(Long postId) {
        return postRepository.findByIdOptional(postId)
                .orElseThrow(() -> new WebApplicationException("Post not found", Response.Status.NOT_FOUND));
    }

    private Comment findCommentOrThrow(Long commentId) {
        return commentRepository.findByIdOptional(commentId)
                .orElseThrow(() -> new WebApplicationException("Comment not found", Response.Status.NOT_FOUND));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<CommentDto> list(@PathParam("postId") Long postId) {
        Post post = findPostOrThrow(postId);
        return post.getCommentaires()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CommentDto create(@PathParam("postId") Long postId, CommentDto dto) {
        Post post = findPostOrThrow(postId);
        Comment entity = new Comment();
        entity.setAuteur(dto.getAuteur());
        entity.setContenu(dto.getContenu());
        entity.setPost(post);
        commentRepository.persist(entity);
        post.getCommentaires().add(entity);
        return toDto(entity);
    }

    @PUT
    @Path("/{commentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CommentDto update(@PathParam("postId") Long postId, @PathParam("commentId") Long commentId, CommentDto dto) {
        Post post = findPostOrThrow(postId);
        Comment comment = findCommentOrThrow(commentId);
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new WebApplicationException("Comment does not belong to post", Response.Status.BAD_REQUEST);
        }
        comment.setAuteur(dto.getAuteur());
        comment.setContenu(dto.getContenu());
        return toDto(comment);
    }

    @DELETE
    @Path("/{commentId}")
    @Transactional
    public void delete(@PathParam("postId") Long postId, @PathParam("commentId") Long commentId) {
        Post post = findPostOrThrow(postId);
        Comment comment = findCommentOrThrow(commentId);
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new WebApplicationException("Comment does not belong to post", Response.Status.BAD_REQUEST);
        }
        commentRepository.delete(comment);
        post.getCommentaires().remove(comment);
    }
}
