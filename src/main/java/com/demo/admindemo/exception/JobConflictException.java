package com.demo.admindemo.exception;

/**
 * <pre>
 * JobConflict 유형의 오류 - Job이 다른 lambda에서 동시에 실행된 경우.
 * client에 제공하는 용도로 사용되지 않음. 내부 처리용.
 * </pre>
 */
public class JobConflictException extends BaseException {

    private static final long serialVersionUID = -1444899815151635038L;

    public JobConflictException(String systemMessage, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace, String userMessage) {
        super(systemMessage, cause, enableSuppression, writableStackTrace, userMessage);
    }

    public static BaseExceptionBuilder withUserMessage(String userMessage) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(JobConflictException.class);
        builder.withUserMessage(userMessage);
        return builder;
    }

    public static BaseExceptionBuilder withUserMessage(String userMessageFormat, Object... objects) {
        return JobConflictException.withUserMessage(BaseExceptionBuilder.formatMessage(userMessageFormat, objects));
    }

    public static BaseExceptionBuilder withUserMessageKey(String userMessageKey) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(JobConflictException.class);
        builder.withUserMessageKey(userMessageKey);
        return builder;
    }

    public static BaseExceptionBuilder withUserMessageKey(String userMessageKey, Object... objects) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(JobConflictException.class);
        builder.withUserMessageKey(userMessageKey, objects);
        return builder;
    }

    public static BaseExceptionBuilder withSystemMessage(String systemMessage) {
        BaseExceptionBuilder builder = new BaseExceptionBuilder(JobConflictException.class);
        builder.withSystemMessage(systemMessage);
        return builder;
    }

    public static BaseExceptionBuilder withSystemMessage(String systemMessageFormat, Object... objects) {
        return JobConflictException.withSystemMessage(BaseExceptionBuilder.formatMessage(systemMessageFormat, objects));
    }
}
