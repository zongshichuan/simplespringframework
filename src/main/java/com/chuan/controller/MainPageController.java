package com.chuan.controller;

import com.chuan.service.MainService;
import lombok.Getter;
import org.simpleframework.annotation.Controller;
import org.simpleframework.inject.annotation.Autowired;

@Controller
@Getter
public class MainPageController {

    @Autowired
    private MainService mainService;
}
