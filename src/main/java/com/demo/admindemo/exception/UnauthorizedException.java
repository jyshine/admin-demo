package com.demo.admindemo.exception;

/**
 * <pre>
 * Unauthorized 유형의 오류 - 로그인되어 있지 않음.
 * client에 response될 때는 HTTP STATUS CODE가 401로 리턴됨.
 * </pre>
 */
public class UnauthorizedException extends BaseException {

    private static final long serialVersionUID = -335644651698981748L;

    public UnauthorizedException(String systemMessage, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace, String userMessage) {
        super(systemMessage, cause, enableSuppression, writableStackTrace, userMessage);
    }

    public static BaseExceptionBuilder withUserMessage(String userMessage) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(UnauthorizedException.class);
        builder.withUserMessage(userMessage);
        return builder;
    }

    public static BaseExceptionBuilder withUserMessage(String userMessageFormat, Object... objects) {
        return UnauthorizedException.withUserMessage(BaseExceptionBuilder.formatMessage(userMessageFormat, objects));
    }
    
    public static BaseExceptionBuilder withUserMessageKey(String userMessageKey) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(UnauthorizedException.class);
        builder.withUserMessageKey(userMessageKey);
        return builder;
    }

    public static BaseExceptionBuilder withUserMessageKey(String userMessageKey, Object... objects) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(UnauthorizedException.class);
        builder.withUserMessageKey(userMessageKey, objects);
        return builder;
    }

    public static BaseExceptionBuilder withSystemMessage(String systemMessage) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(UnauthorizedException.class);
        builder.withSystemMessage(systemMessage);
        return builder;
    }

    public static BaseExceptionBuilder withSystemMessage(String systemMessageFormat, Object... objects) {
        return UnauthorizedException
                .withSystemMessage(BaseExceptionBuilder.formatMessage(systemMessageFormat, objects));
    }
}
