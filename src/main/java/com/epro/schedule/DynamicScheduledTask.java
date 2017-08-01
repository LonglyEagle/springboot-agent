package com.epro.schedule;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import com.epro.constant.DefaultConfigEnum;
import com.epro.domain.ServerInfo;
import com.epro.service.ServerInfoService;
import com.epro.utils.ServerInfoUtil;

/**
 * 动态设置周期的定时任务
 * @author Grant.You
 *
 */
@Component
public class DynamicScheduledTask implements SchedulingConfigurer {
	
	@Autowired
	private ServerInfoService serverInfoService;
	
	//定时任务执行间隔时间(单位：ms)
	private long interval;
	
	//服务器类型，区分master和slave
	private String serverType;
	
	//服务器端口
	private String serverPort;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		
		taskRegistrar.addTriggerTask(new Runnable(){
			@Override
			public void run() {
				
				runTask();
				
			}}, new Trigger() {
				
				@Override
				public Date nextExecutionTime(TriggerContext triggerContext) {
					PeriodicTrigger trigger = new PeriodicTrigger(interval);
					Date nextExecDate = trigger.nextExecutionTime(triggerContext);
					return nextExecDate;
				}
			});
		
	}

	/**
	 * 定时任务执行内容
	 * master服务器不执行定时任务
	 * slave服务器定时向数据库插入服务器信息
	 */
	private void runTask(){
		if (serverType != null && "master".equals(serverType)) {
			return;
		}
		
		ServerInfo serverInfo = new ServerInfo();
		serverInfo.setSlaveAgentNo(DefaultConfigEnum.SLAVEAGENTNO);
		serverInfo.setIp(ServerInfoUtil.getDefaultIpAddress());
		serverInfo.setPort(serverPort);
		serverInfo.setCpu(ServerInfoUtil.getCPUUsedPercent());
		serverInfo.setMemory(ServerInfoUtil.getMemUsedPercent());
		serverInfo.setIo(0);
		serverInfo.setOnlineterminals(ServerInfoUtil.getOnlineterminals());
		//serverInfo.setLatestTime(new Timestamp(System.currentTimeMillis()));
		serverInfoService.addServerInfo(serverInfo);
	}

	//入参的时间间隔是以秒为单位的
	public void setInterval(long interval) {
		this.interval = interval * 1000;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	
}
