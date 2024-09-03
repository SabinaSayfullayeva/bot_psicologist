package com.example.psicologist_bot.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClickDTO {


    @JsonProperty(value = "click_trans_id")
    private Integer clickTransId;


    @JsonProperty(value = "service_id")
    private Integer serviceId;


    @JsonProperty(value = "click_paydoc_id")
    private Integer clickPaydocId;


    @JsonProperty(value = "merchant_trans_id")
    private String merchantTransId;


    @JsonProperty(value = "merchant_prepare_id")
    private Long merchantPrepareId;


    @JsonProperty(value = "merchant_confirm_id")
    private Long merchantConfirmId;


    @JsonProperty(value = "amount")
    private float amount;


    @JsonProperty(value = "action")
    private Integer action;


    @JsonProperty(value = "error")
    private Integer error;


    @JsonProperty(value = "error_note")
    private String errorNote;

    //Дата платежа. Формат «YYYY-MM-DD HH:mm:ss»
    @JsonProperty(value = "sign_time")
    private String signTime;

    //Проверочная строка, подтверждающая подлинность отправляемого запроса. ХЭШ MD5 из следующих параметров:
    //md5( click_trans_id + service_id + SECRET_KEY* + merchant_trans_id + amount + action + sign_time)
    //SECRET_KEY – уникальная строка, выдаваемая Поставщику при подключении.
    @JsonProperty(value = "sign_string")
    private String signString;
}
