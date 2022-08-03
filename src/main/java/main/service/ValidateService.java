package main.service;

import main.api.response.Response;
import main.api.response.ErrorsResponse;
import main.api.response.RegisterUserBean;
import main.entity.CaptchaCode;
import main.entity.User;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidateService {
    private static final long MAXSIZE = 5L * 1024 * 1024;

    final CaptchaCodeRepository captchaCodeRepository;
    final UserService userService;

    public ValidateService(CaptchaCodeRepository captchaCodeRepository, UserService userService) {
        this.captchaCodeRepository = captchaCodeRepository;
        this.userService = userService;
    }

    public Response validateUser(RegisterUserBean userBean) {
        ErrorsResponse incorrent = new ErrorsResponse();
        boolean email = EmailValidator.getInstance().isValid(userBean.getEmail());
        if (!email) {
            incorrent.errors.put("email", "Неверный формат e-mail");
        } else {
            Optional<User> user = userService.findByEmail(userBean.getEmail());
            if (user.isPresent()) {
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
            userService.save(user);
            return Response.success();
        }
    }

    public Response validateProfile(String name, String email, String password, MultipartFile photo) {
        ErrorsResponse errorsResponse = new ErrorsResponse();
        boolean isEmailCorrect = EmailValidator.getInstance().isValid(email);
        if (!isEmailCorrect) {
            errorsResponse.errors.put("email", "Неверный формат e-mail");
        } else {
            User currentUser = userService.getCurrentUser();
            Optional<User> user = userService.findByEmail(email);
            if (!currentUser.getEmail().equals(email) && user.isPresent()) {
                errorsResponse.errors.put("email", "Этот e-mail уже зарегистрирован");
            }
        }
        Pattern pattern = Pattern.compile("[\\da-zA-Zа-яёА-ЯЁ]+");
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            errorsResponse.errors.put("name", "Неверное имя пользователя");
        }
        if (password != null && password.length() < 6) {
            errorsResponse.errors.put("password", "Пароль короче 6-ти символов");
        }
        if(photo != null) {
            String extension = FilenameUtils.getExtension(photo.getOriginalFilename());
            if (!"jpg".equals(extension) && !"png".equals(extension)) {
                errorsResponse.errors.put("photo", "Неверный формат файла!");
            } else if (photo.getSize() > MAXSIZE) {
                errorsResponse.errors.put("photo", "Размер файла превышает допустимый размер!");
            }
        }
        return errorsResponse.errors.isEmpty() ? Response.success() : errorsResponse;
    }

}
