package com.dizertatie.backend.user.pojo;

import java.util.Collections;
import java.util.List;

public class ResponsePageList<T> {

    private int nrOfElements;
    private List<T> pageList;

    public ResponsePageList() {
        nrOfElements = 0;
        pageList = Collections.emptyList();
    }

    public ResponsePageList(int nrOfElements, List<T> pageList) {
        this.nrOfElements = nrOfElements;
        this.pageList = pageList;
    }

    public int getNrOfElements() {
        return nrOfElements;
    }

    public void setNrOfElements(int nrOfElements) {
        this.nrOfElements = nrOfElements;
    }

    public List<T> getPageList() {
        return pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }

}
