package com.demo.admindemo.config.web;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

public class RestResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -7857710275656843711L;

    @Getter
    private T data;

    @Getter
    private RestResponseMeta meta = new RestResponseMeta();

    public RestResponse() {}

    public RestResponse(T data) {
        this.data = data;
        if (data instanceof PageDTO) {
            this.data = (T) ((PageDTO) data).getList();
            this.meta.setPage(((PageDTO) data).getPage());
            this.meta.setTotalCount(((PageDTO) data).getTotalCount());
            this.meta.setIsLast(((PageDTO) data).isLast());
            this.meta.setIsFirst(((PageDTO) data).isFirst());
        }
    }

    /**
     * useMessage
     */
    public void setUserMessage(String message) {
        this.meta.setUserMessage(message);
    }

    /**
     * systemMessage
     */
    public void setSystemMessage(String systemMessage) {
        this.meta.setSystemMessage(systemMessage);
    }

    /**
     * code
     */
    public void setCode(String code) {
        this.meta.setCode(code);
    }

    /**
     * totalCount
     */
    public void setTotalCount(Integer totalCount) {
        this.meta.setTotalCount(totalCount);
    }

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Public Method for chaining
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
    public RestResponse userMessage(String userMessage) {
        this.setUserMessage(userMessage);
        return this;
    }

    public RestResponse systemMessage(String systemMessage) {
        this.setSystemMessage(systemMessage);
        return this;
    }

    public RestResponse code(String code) {
        this.setCode(code);
        return this;
    }

    @Getter
    @Setter
    private class RestResponseMeta implements Serializable {
        @Serial
        private static final long serialVersionUID = 144476231638185217L;

        private String userMessage = "";
        private String systemMessage = "";
        private String code = "";
        private Pageable page;
        private int totalCount;
        private Boolean isLast;
        private Boolean isFirst;
    }
}
