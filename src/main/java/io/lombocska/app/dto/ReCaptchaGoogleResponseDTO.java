package io.lombocska.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReCaptchaGoogleResponseDTO {
	private Boolean success;
	private Date timestamp;
	private String hostname;
	@JsonProperty("error-codes")
	private List<String> errorCodes;
}
