package com.chuan.service.impl;

import com.chuan.service.MainService;
import org.simpleframework.annotation.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MainServiceImpl implements MainService {
    public List<String> getMenus() {
        return Arrays.asList("hhhh");
    }
}
