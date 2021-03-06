package com.erepnikov.repository.comment;

import com.erepnikov.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByPostIdAndTypeOrderByIdDesc(Integer postId, String type);

    void deleteAllByTypeAndPostId(String type, Integer postId);
}
