package com.chalnakchalnak.member_service.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    /**
     * 400 : security 에러
     */
    WRONG_JWT_TOKEN(HttpStatus.UNAUTHORIZED, 401, "다시 로그인 해주세요"),
    NO_SIGN_IN(HttpStatus.UNAUTHORIZED, 402, "로그인을 먼저 진행해주세요"),
    NO_ACCESS_AUTHORITY(HttpStatus.FORBIDDEN, 403, "접근 권한이 없습니다"),
    DISABLED_USER(HttpStatus.FORBIDDEN, 404, "비활성화된 계정입니다. 계정을 복구하시겠습니까?"),
    FAILED_TO_RESTORE(HttpStatus.INTERNAL_SERVER_ERROR, 405, "계정 복구에 실패했습니다. 관리자에게 문의해주세요."),

    /**
     * 900: 기타 에러
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 900, "Internal server error"),
    SSE_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 901, "알림 전송에 실패하였습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, 902, "유효하지 입력입니다"),
    FAILED_TO_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, 903, "저장에 실패했습니다."),

    /**
     * 2000: users service error
     */
    // token
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, 2001, "토큰이 유효하지 않습니다."),

    // Users
    FAILED_TO_LOGIN(HttpStatus.UNAUTHORIZED, 2102, "아이디 또는 패스워드를 다시 확인하세요."),

    /**
     * 3000: product service error
     */


    /**
     * 6000: gpt-api error
     */
    // S3
    S3_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, 7001, "파일 업로드에 실패하였습니다."),
    ;

    private final HttpStatusCode httpStatusCode;
    private final int code;
    private final String message;
}
