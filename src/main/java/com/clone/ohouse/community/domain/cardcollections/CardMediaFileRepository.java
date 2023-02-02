package com.clone.ohouse.community.domain.cardcollections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

@Repository
public interface CardMediaFileRepository extends JpaRepository<CardMediaFile, Long> {
}
