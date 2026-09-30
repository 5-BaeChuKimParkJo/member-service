package com.chalnakchalnak.member_service.auth.adapter.out.sms.coolsms;

import com.chalnakchalnak.member_service.auth.application.port.out.SmsPort;
import com.chalnakchalnak.member_service.auth.common.exception.BaseException;
import com.chalnakchalnak.member_service.auth.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "verification", name = "mode", havingValue = "sms", matchIfMissing = true)
@RequiredArgsConstructor
public class CoolSmsAdapter implements SmsPort {

    private final DefaultMessageService messageService;

    @Value("${coolsms.sender-phone-number}")
    private String senderPhoneNumber;
    public void sendSms(String phoneNumber, String Code) {
        Message message = new Message();
        message.setTo(phoneNumber);
        message.setFrom(senderPhoneNumber);
        message.setText("[찰낙찰낙]\n본인확인 인증번호 [" + Code + "]를 입력해주세요.");

        try {
            messageService.send(message);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FAILED_SEND_SMS);
        }
    }

}
