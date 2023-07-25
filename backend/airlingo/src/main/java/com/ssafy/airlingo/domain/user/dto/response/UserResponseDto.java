package com.ssafy.airlingo.domain.user.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssafy.airlingo.domain.language.dto.request.LanguageDto;
import com.ssafy.airlingo.domain.language.entity.Language;
import com.ssafy.airlingo.domain.user.entity.UserState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDto {
	private long userId;
	private String userNickname;
	private String userLoginId;
	private String userGoogleId;
	private String userPassword;
	private String userEmail;
	private String userImgUrl;
	private String userBio;
	private int userMileage;
	private int userTotalMileage;
	private double userRating;
	private double userTotalRating;
	private int userStudyCount;
	private int userComplain;
	private int userPassportStyle;
	private UserState userState;
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Language userNativeLanguage;
	private List<LanguageDto> userLanguages;
}
