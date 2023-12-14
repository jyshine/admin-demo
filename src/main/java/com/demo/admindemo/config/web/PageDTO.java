package com.demo.admindemo.config.web;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class PageDTO<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -6618206850210257861L;

    private List<T> list = new ArrayList<>();
    private Pageable page;
    private int totalCount;
    private boolean isLast;
    private boolean isFirst;
}
