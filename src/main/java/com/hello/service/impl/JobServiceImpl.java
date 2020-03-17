package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.mapper.JobMapper;
import com.hello.pojo.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tianye
 * @since 2020-03-17
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IService<Job> {

    // 超时默认5分钟
    private static final int time_out = 5;

    @Autowired
    private JobMapper jobMapper;

    // 是否超时（防止拿到锁后宕机，一直释放不了锁，参考zookeeper的临时非顺序节点）
    public boolean isTimeout(Job job) {
        // 如果是锁定的状态，且最后锁定的时间小于当前时间超过5分钟，视为异常锁定，需解锁
        LocalDateTime lastUpdateTime = job.getLastUpdateTime();
        LocalDateTime plusTime = lastUpdateTime.plusMinutes(job.getTimeOut());
        // 1：大于，0等于，-1小于
        return plusTime.compareTo(LocalDateTime.now()) < 1;
    }

    // 释放锁
    public void unLock(Job job) {
        Job jobDO = jobMapper.selectById(job.getId());
        if (jobDO.getJobLock()) {
            jobDO.setJobLock(false);
            jobDO.setLastUpdateTime(LocalDateTime.now());
            jobMapper.updateById(jobDO);
        }
    }

    // 获取任务调度权限（获取锁）
    public boolean tryLock(Job job) {
        // 乐观锁更新拿锁
        QueryWrapper<Job> wrapper = new QueryWrapper<>();
        wrapper.eq("id", job.getId());
        wrapper.eq("version", job.getVersion());

        //版本加1
        job.setJobLock(true);
        job.setLastUpdateTime(LocalDateTime.now());
        job.setVersion(job.getVersion() + 1);
        int update = jobMapper.update(job, wrapper);

        return update > 0;
    }

    // 任务是否可以调度
    public boolean isSchedule(Job job) {
        if (job == null) {
            return false;
        }
        if (!job.getJobEnable()) {
            return false;
        }
        if (job.getJobLock()) {
            if (this.isTimeout(job)) {
                // 已经超时（介入解锁）
                this.unLock(job);
                return true;
            }
            return false;
        }
        return true;
    }

    // 获取任务
    public Job getJob(String code) {
        QueryWrapper<Job> wrapper = new QueryWrapper<>();
        wrapper.eq("job_code", code);
        List<Job> jobs = jobMapper.selectList(wrapper);
        // 数据库已对编号字段做唯一约束
        return jobs == null || jobs.isEmpty() ? null : jobs.get(0);
    }

    // 第一次初始化任务
    public Job initJob(String code) {
        Job job = new Job();
        job.setId(null);
        job.setJobCode(code);
        job.setJobName(code);
        job.setDescription(code);
        job.setJobEnable(true);
        job.setJobLock(false);
        job.setTimeOut(time_out);
        job.setCreateTime(LocalDateTime.now());
        job.setLastUpdateTime(LocalDateTime.now());
        job.setVersion(0L);
        jobMapper.insert(job);
        return job;
    }

}
