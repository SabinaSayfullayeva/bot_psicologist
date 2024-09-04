package com.example.psicologist_bot.util;

public interface Utils {

    String SHOW_CONSULTATIONS = "Kansultatsiyalarni ko'rish";
    String CHANGE_CONSULT = "Kansultatsiyalarni o'zgartirish"; //Canseled, Missed,PENDING bo'lganlar kirmaydi,Faqat Tasdiqlanganlar
    String CANCEL_CONSULT = "Kansultatsiyalarni bekor qilish";
    String EDIT_ADMIN_NAME = "Admin ismi"; // xavfsizlikni taminlash uchun,Log lar yozishda admin ismi bilan yozishda, yangi admin tayinlansa
    String CHANGE_LANGUAGE = "🌏Tilni sozlash"; // yangi til qo'shilganda
    String BOT_DOCUMENT = "Bot uchun qo'llanma";
//    Botni qulayligi uchun, Malumotlarni tozalab, hamma ko'rib turadigan guruhga beck up qilish, yoki loglarni tashlash,
//    va botdagi Konsultatsiyalarni delete qilish
    String BACK_UP ="🗑Ma'lumotlarini tozalash";
    String[][] mainMenuAdmin = {
            {SHOW_CONSULTATIONS},
            {CHANGE_CONSULT},
            {CANCEL_CONSULT},
            {EDIT_ADMIN_NAME,CHANGE_LANGUAGE},
            {BOT_DOCUMENT},
            {BACK_UP}
    };

    String ORQAGA ="orqaga";


    String SHOW_HISTORY = "Bachasi";
    String SHOW_APPROVED = "Tasdiqlanganlar";
    String SHOW_CANCELED = "Bekor qilinganlar";
    String SHOW_PENDING = "KO'RIB CHIQILMAGANLAR";
    String [][] show_consultations={
        {SHOW_HISTORY,SHOW_APPROVED},
        {SHOW_CANCELED,SHOW_PENDING},
        {ORQAGA}
    };
    String SEARCH_WITH_ID = "RAQAM BILAN IZLASH";
    String [][] show_in={
            {SEARCH_WITH_ID,ORQAGA}
    };


    String [][] menu_in={
            {SEARCH_WITH_ID,ORQAGA}
    };

    String ADD_TIME = "Vaqt qo'shish";
    String YUBORISH = "Yuborish";
    String BEKOR_QILISH = "Bekor qilish";
    String [][] time_in={
            {ADD_TIME,YUBORISH},
            {BEKOR_QILISH}
    };

    String NEW_CONSULT = "Yanngi konsultation";
    String MY_CONSULT = "Mening konsultationlarim";
    String [][] user_menu={
            {NEW_CONSULT,MY_CONSULT},
    };

    String[][] orqaga = {
            {ORQAGA}
    };

}
