package com.hello.service.impl;

import com.hello.pojo.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskService {

    @Autowired
    private JobServiceImpl jobService;

    // 每1秒钟执行一次
    @Scheduled(cron = "0/1 * * * * ?")
    public void task1() {
        String code = "task1";
        Runnable runnable = () -> System.out.println("task1: " + LocalDateTime.now());

        this.run(code, runnable);
    }

    // 执行定时任务
    public boolean run(String code, Runnable runnable) {
        // 1.获取任务
        Job job = jobService.getJob(code);

        // 2.任务为空，就创建
        if (job == null) {
            try {
                job = jobService.initJob(code);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(code + "： 任务创建失败");
                return false;
            }
        }

        // 3.任务是否可以调度
        boolean schedule = jobService.isSchedule(job);
        if (!schedule) {
            System.out.println(code + "： 任务不能调度");
            return false;
        }

        // 4.是否可以拿到锁（定时任务特性：不需要重试拿锁，拿不到就不执行，让拿到的机器执行）
        boolean tryLock = jobService.tryLock(job);
        if (!tryLock) {
            System.out.println(code + "： 获取锁失败");
            return false;
        }

        // 5.执行任务
        try {
            runnable.run();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(code + "： 任务执行异常");
            return false;
        } finally {
            // 6.释放锁
            jobService.unLock(job);
        }
    }

}
