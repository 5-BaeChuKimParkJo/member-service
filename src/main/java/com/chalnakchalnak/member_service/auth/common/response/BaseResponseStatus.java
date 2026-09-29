package com.chalnakchalnak.member_service.auth.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    /**
     * 4000 : auth-service 에러
     */

    /**
     * 4000~4099 : security 에러
     */
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, 4000, "토큰이 유효하지 않습니다"),
    NO_SIGN_IN(HttpStatus.UNAUTHORIZED, 4001, "로그인을 먼저 진행해주세요"),

    /**
     * 4100~4199: Request 유효성 에러
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST,  4100, "잘못된 요청입니다."),
    DUPLICATED_MEMBER_ID(HttpStatus.BAD_REQUEST, 4101, "이미 존재하는 아이디입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, 4102, "이미 존재하는 닉네임입니다."),
    DUPLICATED_PHONE_NUMBER(HttpStatus.BAD_REQUEST,  4103, "이미 존재하는 휴대폰 번호입니다."),


    /**
     * 4200~4299: 인증 관련 에러
     */
    EXPIRED_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, 4200, "인증번호가 만료되었습니다. 다시 인증코드를 요청해주세요."),
    FAIL_VERIFIED_CODE(HttpStatus.BAD_REQUEST,  4201, "인증번호가 일치하지 않습니다."),
    SEND_LIMITED(HttpStatus.TOO_MANY_REQUESTS,  4202, "인증코드 발송은 3분에 1회 가능합니다. 잠시 후 다시 시도해주세요."),
    VERIFICATION_LIMITED(HttpStatus.TOO_MANY_REQUESTS,  4203, "5회 실패하여 인증코드가 만료되었습니다. 다시 인증코드를 요청해주세요."),
    FAILED_TO_LOGIN(HttpStatus.UNAUTHORIZED,  4204, "아이디 또는 패스워드를 다시 확인하세요."),
    SIGN_UP_NOT_VERIFIED(HttpStatus.UNAUTHORIZED,  4205, "회원가입을 위해 본인인증을 진행해주세요."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,  4206, "유효하지 않은 리프레시 토큰입니다."),
    FIND_MEMBER_ID_NOT_VERIFIED(HttpStatus.UNAUTHORIZED,  4207, "아이디 찾기를 위한 본인 인증을 먼저 진행해주세요."),
    RESET_PASSWORD_NOT_VERIFIED(HttpStatus.UNAUTHORIZED,  4208, "비밀번호 재설정을 위한 본인 인증을 먼저 진행해주세요."),

    /**
     * 4300~4399: feign client error(member-service)
     */
    MEMBER_SERVICE_CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 4300, "member-service 회원 생성 요청에 실패하였습니다."),
    MEMBER_SERVICE_EXISTS_NICKNAME_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 4301, "member-service 닉네임 중복 확인 요청에 실패하였습니다."),
    DEFAULT_GRADE_NOT_CONFIGURED(HttpStatus.INTERNAL_SERVER_ERROR, 4302, "기본 등급이 설정되지 않았습니다."),

    /**
     * 4900~4999 : 기타 에러
     */
    BAD_REQUEST_INVALID_PARAM(HttpStatus.BAD_REQUEST, 4900, "잘못된 요청입니다. 파라미터를 확인해주세요."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 4901, "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 4902, "허용되지 않은 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 4903,"서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요."),
    S3_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, 4904, "파일 업로드에 실패하였습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, 4905, "유효하지 입력입니다"),
    SSE_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 4906, "알림 전송에 실패하였습니다."),
    FAILED_SEND_SMS(HttpStatus.INTERNAL_SERVER_ERROR,  4907, "SMS 전송에 실패하였습니다."),
    LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,  4908, "로그인에 실패하였습니다."),
    USER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR,  4909, "존재하지 않는 유저입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.INTERNAL_SERVER_ERROR,  4910, "비밀번호가 일치하지 않습니다.");


    private final HttpStatusCode httpStatusCode;
    private final int code;
    private final String message;

}
