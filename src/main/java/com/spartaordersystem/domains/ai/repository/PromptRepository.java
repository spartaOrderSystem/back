package com.spartaordersystem.domains.ai.repository;

import com.spartaordersystem.domains.ai.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, UUID> {
}
