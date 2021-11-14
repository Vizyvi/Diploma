package main.repository;

import main.entity.CaptchaCode;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaCodeRepository extends CrudRepository<CaptchaCode, Integer> {

    CaptchaCode getCaptchaCodeBySecretCode(String secret);
}
