package com.example.psicologist_bot.service;

import com.example.psicologist_bot.model.Answers;
import com.example.psicologist_bot.model.User;
import com.example.psicologist_bot.reposiyory.AnswersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswersService {
    private final AnswersRepository answersRepository;

    public void updateAnswer1(Long chatId,String answer1){
        Optional<Answers> byChatId = answersRepository.findByChatId(chatId);
        if (byChatId.isEmpty()){
            Answers answers=new Answers();
            answers.setChatId(chatId);
            answers.setAnswer1(answer1);
            answersRepository.save(answers);
        }else {
        Answers answers = byChatId.get();
        answers.setAnswer1(answer1);
        answersRepository.save(answers);}
    }

    public void updateAnswer2(Long chatId,String answer2){
        Optional<Answers> byChatId = answersRepository.findByChatId(chatId);
        if (byChatId.isEmpty()){
            Answers answers=new Answers();
            answers.setChatId(chatId);
            answers.setAnswer2(answer2);
            answersRepository.save(answers);
        }
        else {
        Answers answers = byChatId.get();
        answers.setAnswer2(answer2);
        answersRepository.save(answers);}
    }


    public void updateAnswer3(Long chatId,String answer3){
        Optional<Answers> byChatId = answersRepository.findByChatId(chatId);
        if (byChatId.isEmpty()){
            Answers answers=new Answers();
            answers.setChatId(chatId);
            answers.setAnswer3(answer3);
            answersRepository.save(answers);
        }
        else {
        Answers answers = byChatId.get();
        answers.setAnswer3(answer3);
        answersRepository.save(answers);}
    }
}
