package com.blogapp12.service.Impl;

import com.blogapp12.entity.Comment;
import com.blogapp12.entity.Post;
import com.blogapp12.payload.CommentDto;
import com.blogapp12.payload.PostDto;
import com.blogapp12.payload.PostWithCommentDto;
import com.blogapp12.repository.CommentRepository;
import com.blogapp12.repository.PostRepository;
import com.blogapp12.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
//    @Autowired
    private CommentRepository commentRepository;
//    @Autowired
    private ModelMapper modelMapper;
//    @Autowired
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto,long postId) {
        Optional<Post> byId = postRepository.findById(postId);
        Post post=byId.get();
        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment SavedComment = commentRepository.save(comment);
        CommentDto Dto = mapToDto(SavedComment);
        return Dto;
    }
    public PostWithCommentDto getAllRegistrationByPostId(long id) {
        Post post = postRepository.findById(id).get();

        PostDto dto = new PostDto();
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setContent(post.getContent());

        List<Comment> comments= commentRepository.findByPostId(id);
        List<CommentDto> dtos = comments.stream().map(c -> mapToDto(c)).collect(Collectors.toList());
        PostWithCommentDto postWithCommentDto = new PostWithCommentDto();
        postWithCommentDto.setCommentDto(dtos);
        postWithCommentDto.setPost(dto);
        return postWithCommentDto;

    }
    Comment mapToEntity(CommentDto dto){
        Comment comment = modelMapper.map(dto, Comment.class);
        return comment;
    }
    CommentDto mapToDto(Comment comment){
        return modelMapper.map(comment,CommentDto.class);

    }
}
