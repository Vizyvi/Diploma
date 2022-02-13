package main.service;

import main.api.response.Response;
import main.api.response.ApiAuthResponseFalse;
import main.api.response.RegisterUserBean;
import main.entity.CaptchaCode;
import main.entity.User;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidateService {

    final CaptchaCodeRepository captchaCodeRepository;
    final UserRepository userRepository;

    public ValidateService(CaptchaCodeRepository captchaCodeRepository, UserRepository userRepository) {
        this.captchaCodeRepository = captchaCodeRepository;
        this.userRepository = userRepository;
    }

    public Response validateUser(RegisterUserBean userBean) {
        ApiAuthResponseFalse incorrent = new ApiAuthResponseFalse();
        boolean email = EmailValidator.getInstance().isValid(userBean.getEmail());
        if (!email) {
            incorrent.errors.put("email", "Неверный формат e-mail");
        } else {
            Optional<User> user = userRepository.findByEmail(userBean.getEmail());
            if (user.isEmpty()) {
                incorrent.errors.put("email", "Этот e-mail уже зарегистрирован");
            }
        }
        Pattern pattern = Pattern.compile("[\\da-zA-Zа-яёА-ЯЁ]+");
        Matcher matcher = pattern.matcher(userBean.getName());
        if (!matcher.matches()) {
            incorrent.errors.put("name", "Неверное имя пользователя");
        }
        if (userBean.getPassword().length() < 6) {
            incorrent.errors.put("password", "Пароль короче 6-ти символов");
        }
        String secret = userBean.getCaptchaSecret();
        CaptchaCode code = captchaCodeRepository.getCaptchaCodeBySecretCode(secret);
        if (code == null) {
            incorrent.errors.put("captcha", "Каптча не найдена!");
        } else if (!code.getCode().equals(userBean.getCaptcha())) {
            incorrent.errors.put("captcha", "Код с картинки введён неверно");
        }
        if (incorrent.errors.size() != 0) {
            return incorrent;
        } else {
            User user = new User(userBean.getName(), userBean.getEmail(), userBean.getPassword());
            userRepository.save(user);
            return new Response(true);
        }
    }

}
