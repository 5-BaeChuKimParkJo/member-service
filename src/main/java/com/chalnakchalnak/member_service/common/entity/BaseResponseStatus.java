package com.chalnakchalnak.member_service.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    // 400 Bad Request - 잘못된 파라미터
    BAD_REQUEST_INVALID_PARAM(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다. 파라미터를 확인해주세요."),

    // 404 Not Found - 잘못된 경로 요청
    NOT_FOUND(HttpStatus.NOT_FOUND, 404, "요청한 리소스를 찾을 수 없습니다."),

    // 405 Method Not Allowed - 허용되지 않은 HTTP 메서드
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 405, "허용되지 않은 HTTP 메서드입니다."),

    // 500 Internal Server Error - 서버 내부 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500,"서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요."),

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
    SSE_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 901, "알림 전송에 실패하였습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, 902, "유효하지 입력입니다"),
    FAILED_TO_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, 903, "저장에 실패했습니다."),

    // Users
    FAILED_TO_LOGIN(HttpStatus.UNAUTHORIZED, 2000, "아이디 또는 패스워드를 다시 확인하세요."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, 2001, "존재하는 닉네임입니다."),
    NO_EXISTS_MEMBER(HttpStatus.NOT_FOUND, 2002, "존재하지 않는 회원입니다."),
    DUPLICATE_MEMBER(HttpStatus.CONFLICT, 2003, "이미 존재하는 회원입니다."),
    ALREADY_USED_NICKNAME(HttpStatus.CONFLICT, 2004, "현재 사용 중인 닉네임과 동일합니다."),

    // S3
    S3_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, 2005, "파일 업로드에 실패하였습니다."),
    UNABLE_TO_CALCULATE_HMAC(HttpStatus.INTERNAL_SERVER_ERROR, 2006, "HMAC을 계산할 수 없습니다"),


    GRADE_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 2007, "등급 서버에서 데이터 불러오기에 실패했습니다."),
    FAILED_TO_READ_GRADE_EVENT(HttpStatus.INTERNAL_SERVER_ERROR, 2008, "등급 서비스의 이벤트 읽기에 실패했습니다."),
    FAILED_SEND_MESSAGE_TO_DLQ(HttpStatus.INTERNAL_SERVER_ERROR, 2009, "메세지를 DLQ로 보내는데 실패했습니다"),
    ;

    private final HttpStatusCode httpStatusCode;
    private final int code;
    private final String message;
}
