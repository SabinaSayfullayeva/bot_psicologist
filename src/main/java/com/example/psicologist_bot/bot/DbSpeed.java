package com.example.psicologist_bot.bot;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

@Component
@Data
public class DbSpeed {

    private final HashMap<Long,ArrayList<Timestamp>> timeListChoose = new HashMap<>();



}
