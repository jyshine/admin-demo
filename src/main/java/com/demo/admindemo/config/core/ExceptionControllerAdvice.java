package com.demo.admindemo.config.core;

import com.demo.admindemo.config.web.Path;
import com.demo.admindemo.config.web.RestResponse;
import com.demo.admindemo.exception.BaseException;
import com.demo.admindemo.exception.BizException;
import com.demo.admindemo.exception.ForbiddenException;
import com.demo.admindemo.exception.InterfaceException;
import com.demo.admindemo.exception.UnauthorizedException;
import com.demo.admindemo.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final String SLASH = "/";

    @Autowired
    private MessageComponent messageComponent;

    @Value("${careforus.exception.responses-system-message}")
    private boolean responsesSystemMessage;

    /**
     * Not Found (NotFoundException)에 대한 핸들러
     */
    @ExceptionHandler(NotFoundException.class)
    public Object notFoundHandle(NotFoundException ex, HttpServletRequest request, HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("common.exception.notfound"));
        }

        info.setHttpStatus(HttpStatus.NOT_FOUND);
        info.setTemplateName(String.valueOf(java.net.HttpURLConnection.HTTP_NOT_FOUND));// 404

        return makeResponse(info, request, response);
    }

    /**
     * Not Found (NoHandlerFoundException)에 대한 핸들러
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object notFoundNoHandlerHandle(NoHandlerFoundException ex, HttpServletRequest request,
                                          HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("common.exception.notfound"));
        }
        info.setHttpStatus(HttpStatus.NOT_FOUND);
        info.setTemplateName(String.valueOf(java.net.HttpURLConnection.HTTP_NOT_FOUND));// 404

        return makeResponse(info, request, response);
    }

    /**
     * Validation Error (ValidationException, MethodArgumentNotValidException,
     * BindException) 에 대한 핸들러.
     */
    @ExceptionHandler({ ValidationException.class, MethodArgumentNotValidException.class, BindException.class })
    public Object validationHandle(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("common.exception.validation"));
        }
        info.setHttpStatus(HttpStatus.BAD_REQUEST);
        info.setTemplateName("4xx");

        return makeResponse(info, request, response);
    }

    /**
     * Unauthorized (UnauthorizedException) 에 대한 핸들러.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public Object unauthorizedHandle(UnauthorizedException ex, HttpServletRequest request,
                                     HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("common.exception.unauthorized"));
        }
        info.setHttpStatus(HttpStatus.UNAUTHORIZED);
        info.setTemplateName(String.valueOf(java.net.HttpURLConnection.HTTP_UNAUTHORIZED));// 401

        return makeResponse(info, request, response);
    }

    /**
     * Forbidden (ForbiddenException) 에 대한 핸들러.
     */
    @ExceptionHandler(ForbiddenException.class)
    public Object forbiddenHandle(ForbiddenException ex, HttpServletRequest request, HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("common.exception.forbidden"));
        }
        info.setHttpStatus(HttpStatus.FORBIDDEN);
        info.setTemplateName("403");

        return makeResponse(info, request, response);
    }

    /**
     * Business Error (BizException) 에 대한 핸들러.
     */
    @ExceptionHandler(BizException.class)
    public Object bizHandle(BizException ex, HttpServletRequest request, HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("common.exception.biz="));
        }

        // 강제 ok설정이면, status code 는 ok로.
        if (info.isForcesOK()) {
            info.setHttpStatus(HttpStatus.OK);
        } else {
            info.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        info.setTemplateName("5xx");

        return makeResponse(info, request, response);
    }

    /**
     * Interface Error (InterfaceException) 에 대한 핸들러.
     */
    @ExceptionHandler(InterfaceException.class)
    public Object interfaceHandle(InterfaceException ex, HttpServletRequest request,
                                  HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("common.exception.interface"));
        }
        info.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        info.setTemplateName("5xx");

        return makeResponse(info, request, response);
    }

    /**
     * BaseException 에 대한 핸들러. 정상적이라면 BaseException 타입이 throw 되서는 안됨.
     */
    @ExceptionHandler(BaseException.class)
    public Object baseExceptionHandle(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        ExceptionInfo info = new ExceptionInfo(ex);

        if (StringUtils.isBlank(info.getUserMessage())) {
            info.setUserMessage(messageComponent.getMessage("common.exception.server"));
        }
        info.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        info.setTemplateName("5xx");

        return makeResponse(info, request, response);
    }

    /**
     * Exception / RuntimeException 타입에 대한 핸들러.
     */
    @ExceptionHandler({ Exception.class, RuntimeException.class })
    public Object exceptionHandle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ExceptionInfo info = new ExceptionInfo(ex);

        info.setUserMessage(messageComponent.getMessage("common.exception.server"));
        info.setSystemMessage(ex.getMessage());
        info.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        info.setTemplateName("5xx");

        return makeResponse(info, request, response);
    }

    /**
     * response를 위한 객체 생성.
     */
    private Object makeResponse(ExceptionInfo info, HttpServletRequest request, HttpServletResponse response) {
        log.error("\n" +
                        "{}" +
                        " - userMessage: {}\n" +
                        " - systemMessage: {}\n" +
                        " - httpStatus: {}\n" +
                        " - code: {}\n",
                ExceptionUtils.getStackTrace(info.getE()), info.getUserMessage(), info.getSystemMessage(),
                info.getHttpStatus(), info.getCode());

        response.setStatus(info.getHttpStatus().value());

        MediaType requestMediaType = null;
        if (request.getContentType() != null) {
            requestMediaType = MediaType.valueOf(request.getContentType());
        }

        String apiPath = request.getContextPath() + Path.API + SLASH;
        // /api 로 시작하거나, content-type이 application/json 이면. rest-api
        if (request.getServletPath().startsWith(apiPath)
                || MediaType.APPLICATION_JSON.isCompatibleWith(requestMediaType)) {

            RestResponse restResponse = new RestResponse().userMessage(info.userMessage).code(info.getCode());

            // front에 systemMessage를 전달하도록 설정되어있는 경우만.
            if (responsesSystemMessage) {
                restResponse.setSystemMessage(info.getSystemMessage());
            }

            return new ResponseEntity<>(restResponse, info.getHttpStatus());
        }
        // 아니면 view
        else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("userMessage", info.getUserMessage());

            // front에 systemMessage를 전달하도록 설정되어있는 경우만.
            if (responsesSystemMessage) {
                mav.addObject("systemMessage", info.getSystemMessage());
            }

            mav.setViewName("skiaf/view/error/error-" + info.getTemplateName());

            return mav;
        }
    }

    @Getter
    @Setter
    private class ExceptionInfo {
        private Throwable e;
        private Throwable cause;
        private String userMessage;
        private String systemMessage;
        private boolean forcesOK = false;
        private String code;
        private HttpStatus httpStatus;
        private String templateName;

        public ExceptionInfo(Throwable e) {
            this.e = e;
            this.cause = e.getCause();
            if (e instanceof BaseException) {
                BaseException be = (BaseException) e;
                this.userMessage = be.getUserMessage();
                this.systemMessage = be.getSystemMessage();
                this.code = be.getCode();
            }
            // @validator에서 뱉는 Exception
            else if (e instanceof MethodArgumentNotValidException) {
                Pair<String, String> msg = setValidationError(((MethodArgumentNotValidException) e).getBindingResult());
                this.userMessage = msg.getFirst();
                this.systemMessage = msg.getSecond();
            }
            // @validator에서 뱉는 Exception
            else if (e instanceof BindException) {
                Pair<String, String> msg = setValidationError(((BindException) e).getBindingResult());
                this.userMessage = msg.getFirst();
                this.systemMessage = msg.getSecond();
            }

            if (e instanceof BizException) {
                this.forcesOK = ((BizException) e).isForcesOK();
            }
            if (cause instanceof BaseException) {
                BaseException be = (BaseException) cause;
                this.userMessage = be.getUserMessage();
                this.systemMessage = be.getSystemMessage();
                this.code = be.getCode();
            }
            if (cause instanceof BizException) {
                this.forcesOK = ((BizException) cause).isForcesOK();
            }

        }

        private Pair<String, String> setValidationError(BindingResult br) {

            FieldError fe = br.getFieldError();
            String errorDefaultMessage = null;
            String errorAttribute = null;
            if(fe != null) {
                errorDefaultMessage = fe.getDefaultMessage();
                errorAttribute = fe.getCodes()[0];
            }else {
                ObjectError oe = br.getGlobalError();
                errorDefaultMessage = oe.getDefaultMessage();
                errorAttribute = oe.getCode();
            }

            String key = errorDefaultMessage;
            Object[] args = null;

            if (StringUtils.indexOf(key, ',') > -1) {
                String[] errorMessageArr = key.split(",");
                key = errorMessageArr[0].trim();
                args = new Object[errorMessageArr.length - 1];
                System.arraycopy(errorMessageArr, 1, args, 0, errorMessageArr.length - 1);
            }

            String errorMessage = messageComponent.getMessage(key, args);
            if(errorMessage == null) {
                errorMessage = errorDefaultMessage;
            }

            if (StringUtils.lastIndexOf(errorAttribute, '.') > -1) {
                errorMessage = "[" + errorAttribute.substring(StringUtils.lastIndexOf(errorAttribute, '.') + 1) + "] " + errorMessage;
            }

            return Pair.of(errorMessage, errorAttribute);
        }
    }
}
