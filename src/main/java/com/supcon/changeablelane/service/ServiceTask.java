//package com.supcon.changeablelane.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//
///**
// * @Author caowenbo
// * @create 2021/2/24 14:37
// */
//@Component
//@Service
//public class ServiceTask {
//
//    @Autowired
//    private OsService osService;
//
//    @Scheduled(cron = "*/10 * * * * *")
//    public void refreshTokenTask() {
//        osService.refreshToken();
//    }
//
//    //项目启动时，从os平台获取可变车道牌信息，并保存进数据库
//    @PostConstruct
//    public void init(){
//        //获取可变车道牌信息
//        //获取诱导屏信息
//
//    }
//
//}
