package com.jfinal.ext.plugin.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobB implements Job {
    static int callTime = 0;
    static int l = 0;
    int [] ii = new int[]{};

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        callTime++;
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " JobB works,callTime is: " + callTime);
    }

}
