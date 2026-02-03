package com.example.cellphoneback.repository.community;

import com.example.cellphoneback.entity.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Integer> {

    @Modifying
    @Query("update  Community n set n.viewCount = n.viewCount +1 where n.id=:id")
    void increaseViewCount(@Param("id") Integer id);
}
