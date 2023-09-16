package com.mallang.squirrel.infrastructure.humor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumorRepository extends JpaRepository<HumorEntity, Long> {
}
