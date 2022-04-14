package org.thesis.woodindustryecommerce.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.thesis.woodindustryecommerce.model.Coupon;
import org.thesis.woodindustryecommerce.services.CouponService;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Slf4j
@Configuration
public class SchedulingConfiguration implements SchedulingConfigurer {

    @Autowired
    CouponService couponService;

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler() {
            private static final long serialVersionUID = -1L;
            @Override
            public void destroy(){
                this.getScheduledThreadPoolExecutor().setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
                super.destroy();
            }
        };
        scheduler.setThreadNamePrefix("TaskScheduler");
        scheduler.setPoolSize(10);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(20);
        return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
        taskRegistrar.addTriggerTask(() -> {
            log.debug("Checking coupons expiration date");
            List<Coupon> coupons = couponService.findAll();
            for(Coupon coupon: coupons){
                if(LocalDate.now().equals(coupon.getExpirationDate())){
                    couponService.delete(coupon.getId());
                }
            }

        }, triggerContext -> {
            Calendar nextExecutionTime = new GregorianCalendar();
            Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
            nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
            nextExecutionTime.add(Calendar.HOUR, 24); // runs every 24 hour
            return nextExecutionTime.getTime();
        });
    }
}