package com.tuofan.core;

import lombok.Data;

import java.util.List;

@Data
public class SearchQ extends PageQ{

    private List<Integer> schoolIds;

    private List<Integer> classIds;

    private List<Integer> subjectIds;
}
