package com.spartaordersystem.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*  400 BAD_REQUEST : 잘못된 요청  */
    ILLEGAL_ARGUMENT_ERROR(400, "잘못된 파라미터 전달"),
    CATEGORY_IS_DELETED(400, "이미 삭제된 카테고리입니다."),
    INVALID_PAGE_OR_SIZE(400,  "유효하지 않은 페이지입니다."),

    /*  401 UNAUTHORIZED : 인증 안됨  */
    UNAUTHORIZED(401, "인증되지 않았습니다."),

    /*  403 FORBIDDEN : 권한 없음  */
    FORBIDDEN(403, "권한이 없습니다."),

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    ACCESS_DENIED(404, "접근 권한이 없습니다."),
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(404, "카테고리를 찾을 수 없습니다."),
    STORE_NOT_FOUND(404, "카테고리를 찾을 수 없습니다."),
    MENU_NOT_FOUND(404, "메뉴를 찾을 수 없습니다."),

    /*  408 REQUEST_TIMEOUT : 요청에 대한 응답 시간 초과  */
    TIMEOUT_ERROR(408, "응답시간을 초과하였습니다."),

    /*  409 CONFLICT : Resource 중복  */
    ALREADY_EXIST_USERID(409, "이미 존재하는 USERID 입니다."),
    ALREADY_EXIST_CATEGORY(409, "이미 존재하는 카테고리 입니다."),
    ALREADY_EXIST_ADDRESS(409, "이미 주소가 존재하고 있습니다."),

    /*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
    INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다."),

    /*  502 BAD_GATEWAY  연결 실패   */
    FAIL_TO_CONNECT_GITHUB(502, "깃허브 API 연결에 실패하였습니다.")
    ;

    private final Integer httpStatus;
    private final String message;
}

