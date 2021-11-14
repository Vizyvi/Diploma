package main.controller;

import main.api.bean.CaptchaBean;
import main.api.response.AuthCheckResponse;
import main.api.response.RegisterUserBean;
import main.service.CaptchaService;
import main.service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired private CaptchaService captchaService;
    @Autowired private ValidateService validateService;

    @PostMapping(value = "/register")
        public AuthCheckResponse createUser(@RequestBody RegisterUserBean bean) {
        return validateService.validateUser(bean);
    }

    @GetMapping(value = "/captcha")
    public CaptchaBean getCaptcha() {
        return captchaService.getCaptcha();
    }
}
