package com.example.psicologist_bot.reposiyory;

import com.example.psicologist_bot.model.Answers;
import com.example.psicologist_bot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswersRepository extends JpaRepository<Answers,Long> {
    Optional<Answers> findByChatId(Long chatId);
}
