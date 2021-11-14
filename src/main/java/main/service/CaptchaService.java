package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.bean.CaptchaBean;
import main.entity.CaptchaCode;
import main.repository.CaptchaCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;

@Service
public class CaptchaService {
    private static final String HEADER = "data:image/png;base64, ";
    @Autowired private CaptchaCodeRepository captchaCodeRepository;

    public CaptchaBean getCaptcha(){
        CaptchaBean bean = new CaptchaBean();
        Cage cage = new GCage();
        String token = cage.getTokenGenerator().next();
        byte[] draw = cage.draw(token);
        String encodedString = Base64.getEncoder().encodeToString(draw);
        bean.setImage(HEADER + encodedString);
        UUID secret = UUID.randomUUID();
        bean.setSecret(secret.toString());
        saveBean(secret.toString(), token);
        return bean;
    }

    private void saveBean(String secret, String token) {
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCode(token);
        captchaCode.setSecretCode(secret);
        captchaCode.setTime(LocalDate.now());
        captchaCodeRepository.save(captchaCode);
    }
}
