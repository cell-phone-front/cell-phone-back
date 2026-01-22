package com.example.cellphoneback.repository.community;

import com.example.cellphoneback.entity.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, String> {
}
