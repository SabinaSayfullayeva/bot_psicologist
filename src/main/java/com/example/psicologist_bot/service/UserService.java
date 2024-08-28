package com.example.psicologist_bot.service;

import com.example.psicologist_bot.model.Language;
import com.example.psicologist_bot.model.User;
import com.example.psicologist_bot.model.UserState;
import com.example.psicologist_bot.reposiyory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ExecutorService executorService = Executors.newCachedThreadPool();


    public UserState getUserState(Long chatId) {
        return userRepository.findUserStateByChatId(chatId).orElse(UserState.DEFAULT);
    }

    public void save(User user) {
        Optional<User> optionalUser = userRepository.findByChatId(user.getChatId());
        if (optionalUser.isEmpty()) {
            userRepository.save(user);
            return;
        }
        throw new RuntimeException("User is already saved with this chatId");
    }

    public boolean existsByChatId(Long chatId) {
        return userRepository.existsByChatId(chatId);
    }

    public Future<Language> getLanguage(Long chatId) {
        return executorService.submit(userRepository.findByChatId(chatId)
                .orElseThrow(() -> new RuntimeException("User is not found with this chatId"))::getLanguage);
    }

    public void changeLanguage(Long chatId, String language) {
        if (existsByChatId(chatId))
            userRepository.updateLanguage(chatId, language);
        else
            throw new RuntimeException("User is not found with this chatId: " + chatId);
    }

    public UserState updateUserState(Long chatId, UserState state) {
        if (existsByChatId(chatId)) {
            userRepository.updateState(chatId, state.name());
            return state;
        }
        return getUserState(chatId);
    }
}
