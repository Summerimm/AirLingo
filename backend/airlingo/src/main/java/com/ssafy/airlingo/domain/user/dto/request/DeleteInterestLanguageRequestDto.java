package com.ssafy.airlingo.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "(DeleteInterestLanguageRequestDto) 관심 언어 삭제요청 DTO")
public class DeleteInterestLanguageRequestDto {

	@NotNull
	@Schema(description = "유저 ID")
	private Long userId;

	@Schema(description = "언어 ID")
	private Long languageId;

}
