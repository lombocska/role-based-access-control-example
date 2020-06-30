package io.lombocska.app.service;

public interface CaptchaService {

	boolean validateCaptcha(String captchaResponse);

}
