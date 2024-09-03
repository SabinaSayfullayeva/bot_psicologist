package com.example.psicologist_bot.reposiyory;


import com.example.psicologist_bot.model.Answers;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Answers,Long> {

    @Query(value = "select user_state from users where chat_id=:chatId", nativeQuery = true)
    Optional<String> findUserStateByChatId(@Param("chatId") Long chatId);

    boolean existsByChatId(Long chatId);

    @Transactional
    @Modifying
    @Query(value = "update users set user_state=:state where chat_id=:chatId", nativeQuery = true)
    void updateState(@Param("chatId") Long chatId, @Param("state") String state);



}
