package com.tuofan.core;

import lombok.Data;

@Data
public class PageQ {
    private int current = 1;
    private int size = 100;
}
