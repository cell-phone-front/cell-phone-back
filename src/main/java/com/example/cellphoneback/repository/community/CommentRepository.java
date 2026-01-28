package com.example.cellphoneback.repository.community;

import com.example.cellphoneback.entity.community.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByCommunityId(Integer communityId);

    long countByCommunityId(int id);
}
