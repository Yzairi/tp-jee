package fr.lbenoit.formation.blog.controller;

import java.util.List;
import java.util.stream.Collectors;

import fr.lbenoit.formation.blog.dto.CommentDto;
import fr.lbenoit.formation.blog.dto.PostDto;
import fr.lbenoit.formation.blog.model.Comment;
import fr.lbenoit.formation.blog.model.Post;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/posts")
public class PostController {

    @Inject
    PostRepository repository;

    private PostDto toDto(Post entity) {
        if (entity == null) {
            return null;
        }
        PostDto dto = new PostDto();
        dto.setId(entity.getId());
        dto.setTitre(entity.getTitre());
        dto.setContenu(entity.getContenu());
        dto.setCommentaires(entity.getCommentaires()
                .stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList()));
        return dto;
    }

    private Post toEntity(PostDto dto) {
        if (dto == null) {
            return null;
        }
        Post entity = new Post();
        entity.setTitre(dto.getTitre());
        entity.setContenu(dto.getContenu());
        dto.getCommentaires().forEach(commentDto -> {
            Comment comment = toCommentEntity(commentDto, entity);
            entity.getCommentaires().add(comment);
        });
        return entity;
    }

    private CommentDto toCommentDto(Comment entity) {
        if (entity == null) {
            return null;
        }
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setAuteur(entity.getAuteur());
        dto.setContenu(entity.getContenu());
        return dto;
    }

    private Comment toCommentEntity(CommentDto dto, Post post) {
        Comment entity = new Comment();
        entity.setAuteur(dto.getAuteur());
        entity.setContenu(dto.getContenu());
        entity.setPost(post);
        return entity;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<PostDto> all() {
        return repository.listAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public PostDto getById(@PathParam("id") Long id) {
        return repository.findByIdOptional(id)
                .map(this::toDto)
                .orElseThrow(() -> new WebApplicationException("Post not found", Response.Status.NOT_FOUND));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public PostDto create(PostDto dto) {
        Post entity = toEntity(dto);
        repository.persist(entity);
        return toDto(entity);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public PostDto update(@QueryParam("id") Long id, PostDto dto) {
        Post existing = repository.findById(id);
        if (existing == null) {
            throw new WebApplicationException("Post not found", Response.Status.NOT_FOUND);
        }
        existing.setTitre(dto.getTitre());
        existing.setContenu(dto.getContenu());
        existing.getCommentaires().clear();
        dto.getCommentaires().forEach(commentDto -> {
            Comment comment = toCommentEntity(commentDto, existing);
            existing.getCommentaires().add(comment);
        });
        return toDto(existing);
    }

    @DELETE
    @Transactional
    public void delete(@QueryParam("id") Long id) {
        Post existing = repository.findById(id);
        if (existing == null) {
            throw new WebApplicationException("Post not found", Response.Status.NOT_FOUND);
        }
        repository.delete(existing);
    }
}
