package com.example.psicologist_bot.service;

import com.example.psicologist_bot.model.enums.Language;
import com.example.psicologist_bot.model.User;
import com.example.psicologist_bot.model.enums.UserState;
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
        return UserState.valueOf(userRepository.findUserStateByChatId(chatId).orElse(String.valueOf(UserState.DEFAULT)));
    }

    public void save(User user) {
        Optional<User> optionalUser = userRepository.findByChatId(user.getChatId());
        if (optionalUser.isEmpty()) {
            userRepository.save(user);
            return;
        }
        throw new RuntimeException("User is already saved with this chatId");
    }

    public void updateFullname(Long chatId,String fullname){
        Optional<User> optionalUser = userRepository.findByChatId(chatId);
        User user = optionalUser.get();
        user.setFullName(fullname);
        userRepository.save(user);
    }


    public void updatePhoneNumber(Long chatId,String phoneNumber){
        Optional<User> optionalUser = userRepository.findByChatId(chatId);
        User user = optionalUser.get();
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }


    public void updateEmail(Long chatId,String mail){
        Optional<User> optionalUser = userRepository.findByChatId(chatId);
        User user = optionalUser.get();
        user.setEmail(mail);
        userRepository.save(user);
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

   public User getByChatId(Long chatId){
        return userRepository.findByChatId(chatId).get();
    }
}
