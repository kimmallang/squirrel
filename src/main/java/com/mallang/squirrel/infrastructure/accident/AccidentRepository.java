package com.mallang.squirrel.infrastructure.accident;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccidentRepository extends JpaRepository<AccidentEntity, Long> {
}
