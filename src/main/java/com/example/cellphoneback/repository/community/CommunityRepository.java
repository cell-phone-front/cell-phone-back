package com.example.cellphoneback.repository.community;

import com.example.cellphoneback.entity.community.CommunityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<CommunityEntity, String> {
}
