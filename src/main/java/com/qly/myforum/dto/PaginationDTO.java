package com.qly.myforum.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> Data;
    private boolean showFirstPage;
    private boolean ShowEndPage;
    private boolean showPreviousPage;
    private boolean showNextPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<Integer>();
    Integer totalPage;

    public void setPagination(Integer totalPage, Integer page) {
        this.showNextPage = true;
        this.page = page;
        this.totalPage = totalPage;
        pages.add(page);
        for (int i = 1; i >=0; i++) {
            if ((page - i) > 0) {
                pages.add(page - i);
            }
        }
        for (int i = 1; i <= 3; i++) {
            if ((page + i) <= totalPage) {
                pages.add(page + i);
            }
        }
        Collections.sort(pages);

        //是否展示上一页
        if (page == 1) {
            showPreviousPage = false;
        } else {
            showPreviousPage = true;
        }

        //是否展示下一页
        if (page == totalPage) {
            showNextPage = false;
        } else {
            showNextPage = true;
        }

//            是否显示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
//        是否显示最后一页
        if (pages.contains(totalPage)) {
            ShowEndPage = false;
        } else {
            ShowEndPage = true;
        }
    }
}
