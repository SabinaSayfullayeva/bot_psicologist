package com.example.psicologist_bot.reposiyory;

import com.example.psicologist_bot.model.Answers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswersRepository extends JpaRepository<Answers,Long> {
}
