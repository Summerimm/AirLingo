package com.ssafy.airlingo.domain.language.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.airlingo.domain.language.dto.request.EvaluateUserRequestDto;
import com.ssafy.airlingo.domain.language.dto.response.LearningLanguageNumberResponseDto;
import com.ssafy.airlingo.domain.language.dto.response.LearningLanguageTimeResponseDto;
import com.ssafy.airlingo.domain.language.dto.response.LearningNumberResponseDto;
import com.ssafy.airlingo.domain.language.dto.response.LearningStatisticResponseDto;
import com.ssafy.airlingo.domain.language.dto.response.LearningTimeResponseDto;
import com.ssafy.airlingo.domain.language.entity.Grade;
import com.ssafy.airlingo.domain.language.entity.Language;
import com.ssafy.airlingo.domain.language.entity.Record;
import com.ssafy.airlingo.domain.language.entity.UserLanguage;
import com.ssafy.airlingo.domain.language.repository.GradeRepository;
import com.ssafy.airlingo.domain.language.repository.LanguageRepository;
import com.ssafy.airlingo.domain.language.repository.RecordRepository;
import com.ssafy.airlingo.domain.language.repository.UserLanguageRepository;
import com.ssafy.airlingo.domain.study.entity.Study;
import com.ssafy.airlingo.domain.study.repository.StudyRepository;
import com.ssafy.airlingo.domain.user.entity.User;
import com.ssafy.airlingo.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

	private final RecordRepository recordRepository;
	private final UserRepository userRepository;
	private final UserLanguageRepository userLanguageRepository;
	private final LanguageRepository languageRepository;
	private final GradeRepository gradeRepository;
	private final StudyRepository studyRepository;

	@Transactional
	@Override
	public boolean evaluateUser(EvaluateUserRequestDto evaluateUserRequestDto) {
		log.info("RecordServiceImpl_evaluateUser || 유저 실력/별점(매너) 평가");

		User evaluatedUser = userRepository.findById(evaluateUserRequestDto.getUserId()).get();
		Language language = languageRepository.findById(evaluateUserRequestDto.getLanguageId()).get();
		Grade grade = gradeRepository.findById(evaluateUserRequestDto.getGradeId()).get();
		Study study = studyRepository.findById(evaluateUserRequestDto.getStudyId()).get();
		recordRepository.save(createNewRecordAndRenewUserRating(evaluatedUser, language, grade, study,
			evaluateUserRequestDto.getRating()));

		updateUserLanguage(evaluatedUser, language);
		return true;
	}

	@Override
	public Record createNewRecordAndRenewUserRating(User user, Language language, Grade grade, Study study,
		float rating) {
		log.info("RecordServiceImpl_createNewRecordAndRenewUserRating || 평가 기록 생성 및 평가 기록 유저에 반영");
		user.renewRatingAndStudyCount(rating);
		return Record.createNewRecord(user, language, grade, study);
	}

	private void updateUserLanguage(User user, Language language) {
		log.info("updateUserLanguage");
		Grade grade = gradeRepository.findById(recordRepository.getMostFrequentGradeIdForLanguage(
			language.getLanguageId())).get();
		UserLanguage userLanguage = userLanguageRepository.findByUserAndLanguage(user, language);
		if (userLanguage == null) {
			userLanguageRepository.save(UserLanguage.builder().
				language(language)
				.user(user)
				.grade(grade)
				.build());
			return;
		}
		userLanguage.updateGrade(grade);
	}

	@Override
	public LearningStatisticResponseDto getStatistic(Long userId) {
		User user = userRepository.findById(userId).get();
		return LearningStatisticResponseDto.builder()
			.timeResponse(getTimeResponseDto(user))
			.numberResponse(getNumberResponseDto(user))
			.build();
	}

	private LearningTimeResponseDto getTimeResponseDto(User user) {
		List<LearningLanguageTimeResponseDto> languageTimes = recordRepository.getLearningLanguageTimeStatistics(user);
		return LearningTimeResponseDto.builder()
			.learningLanguageResponseList(languageTimes)
			.totalStudyTime((int)languageTimes.stream().mapToLong(languageTime -> languageTime.getTotalTime()).sum())
			.build();
	}

	private LearningNumberResponseDto getNumberResponseDto(User user) {
		List<LearningLanguageNumberResponseDto> languageNumbers = recordRepository.getLearningLanguageNumberStatistics(
			user);
		return LearningNumberResponseDto.builder()
			.languageNumberResponseDtoList(languageNumbers)
			.totalStudyNumber(
				(int)languageNumbers.stream().mapToLong(languageNumber -> languageNumber.getTotalNumber()).sum())
			.build();
	}
}
