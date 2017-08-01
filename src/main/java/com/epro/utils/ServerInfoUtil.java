package com.epro.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 服务器信息获取类
 * @author Grant.You
 *
 */
@Component
public class ServerInfoUtil {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServerInfoUtil.class);
	
	private static Sigar sigar = SigarUtil.getInstance();

	/**
	 * 获取物理内存使用占比
	 */
	public static double getMemUsedPercent() { 

		Mem mem; 
		try { 
			mem = sigar.getMem();
			return mem.getUsedPercent();
		} catch (SigarException e) { 
			LOGGER.error("请联系管理员！", e);  
		}

		return 100;
	}
	
	/**
	 * 获取物理内存空闲率
	 */
	public static double getMemFreePercent() { 

		Mem mem; 
		try { 
			mem = sigar.getMem();
			return mem.getFreePercent();
		} catch (SigarException e) { 
			LOGGER.error("请联系管理员！", e); 
		}

		return 0;
	}
 
	/**
	 * CPU总使用占用率（多块cpu取平均值）
	 * @return
	 */
	public static double getCPUUsedPercent() { 
		// 方式二，不管是单块CPU还是多CPU都适用 
		CpuPerc[] cpuList = null; 

		try { 
			cpuList = sigar.getCpuPercList(); 
		} catch (SigarException e) { 
			LOGGER.error("请联系管理员！", e); 
		} 

		if(cpuList.length == 0)
		{
			return 100;
		}

		double total = 0;

		for (CpuPerc cpu : cpuList)
		{
			total += cpu.getCombined();
		}
		
		double proportion = total/cpuList.length*100;
		return proportion;	
	}
	
	/**
	 * CPU平均空闲率（多块cpu取平均值）
	 * @return
	 */
	public static double getCPUFreePercent() { 
		// 方式二，不管是单块CPU还是多CPU都适用 
		CpuPerc[] cpuList = null; 

		try { 
			cpuList = sigar.getCpuPercList(); 
		} catch (SigarException e) { 
			LOGGER.error("请联系管理员！", e); 
		} 

		if(cpuList.length == 0)
		{
			return 0;
		}

		double total = 0;

		for (CpuPerc cpu : cpuList)
		{
			total += cpu.getIdle();
		}
		
		double proportion = total/cpuList.length*100;
		return proportion;	
	}
	
	/**
	 * 取到当前机器的IP地址
	 * @return
	 */
	public static String getDefaultIpAddress() {
		String address = null;

		address = getNetIPAddress();
		if (!("127.0.0.1".equals(address))) {
			return address;
		}
		try {
			address = InetAddress.getLocalHost().getHostAddress();
			LOGGER.debug("-----sigar--------Address:" + sigar.getNetInterfaceConfig().getAddress());
			LOGGER.debug("-------------Address:" + address);

			if (!("127.0.0.1".equals(address))) {
				return address;
			}
			sigar.getNetInterfaceConfig().getHwaddr();
			address = sigar.getNetInterfaceConfig().getAddress();
		} catch (UnknownHostException e) {
			try {
				address = sigar.getNetInterfaceConfig().getAddress();
			} catch (SigarException e1) {
				LOGGER.error("请联系管理员！", e1);
			}
		} catch (SigarException e) {
			address = "127.0.0.1";
		}

		return address;
	}
	/*public static String getDefaultIpAddress() { 
		String address = null; 
		try { 
			address = InetAddress.getLocalHost().getHostAddress(); 
			// 没有出现异常而正常当取到的IP时，如果取到的不是网卡循回地址时就返回 
			// 否则再通过Sigar工具包中的方法来获取 
			if (!NetFlags.LOOPBACK_ADDRESS.equals(address)) { 
				return address; 
			} 
			
			address = sigar.getNetInterfaceConfig().getAddress();
			
		} catch (UnknownHostException e) { 
			try {
				address = sigar.getNetInterfaceConfig().getAddress();
			} catch (SigarException e1) {
				LOGGER.error("请联系管理员！", e1); 
			}
		} catch (SigarException e) {
			address = NetFlags.LOOPBACK_ADDRESS; 
		}
		
		return address;
		
	}*/
	
	public static String getNetIPAddress()
	  {
	    String ipAddress = "127.0.0.1";

	    String[] ifNames = new String[0];
	    try {
	      ifNames = sigar.getNetInterfaceList();
	    } catch (SigarException e1) {
	      e1.printStackTrace();
	    }
	    for (int i = 0; i < ifNames.length; ++i) {
	      String name = ifNames[i];
	      try
	      {
	        NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
	        if ("eth1".equalsIgnoreCase(name)) {
	          ipAddress = ifconfig.getAddress();
	          LOGGER.debug("Address = " + ifconfig.getAddress());
	          return ipAddress;
	        }
	        LOGGER.debug("\nname = " + name);
	        LOGGER.debug("Address = " + ifconfig.getAddress());
	        LOGGER.debug("Netmask = " + ifconfig.getNetmask());
	        if ((ifconfig.getFlags() & 1L) <= 0L) {
	          LOGGER.debug("!IFF_UP...skipping getNetInterfaceStat");
	          return ipAddress;
	        }
	        try {
	          NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
	          LOGGER.debug("RxPackets = " + ifstat.getRxPackets());
	          LOGGER.debug("TxPackets = " + ifstat.getTxPackets());
	          LOGGER.debug("RxBytes = " + ifstat.getRxBytes());
	          LOGGER.debug("TxBytes = " + ifstat.getTxBytes());
	          LOGGER.debug("RxErrors = " + ifstat.getRxErrors());
	          LOGGER.debug("TxErrors = " + ifstat.getTxErrors());
	          LOGGER.debug("RxDropped = " + ifstat.getRxDropped());
	          label492: LOGGER.debug("TxDropped = " + ifstat.getTxDropped());
	        } catch (SigarNotImplementedException e) {
	        } catch (SigarException e) {
	          LOGGER.debug(e.getMessage());
	        }
	      }
	      catch (SigarException e1)
	      {
	      }

	    }

	    return ipAddress;
	  }

	/**
	 * 获取磁盘IO使用率
	 * @return
	 */
	public static double getIOProportion(){
		LOGGER.debug("开始收集磁盘IO使用率");  
        float ioUsage = 0.0f;  
        Process pro = null;  
        Runtime r = Runtime.getRuntime();  
        try {  
            String command = "iostat -d -x";  
            pro = r.exec(command);  
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));  
            String line = null;  
            int count =  0;  
            while((line=in.readLine()) != null){          
                if(++count >= 4){  
                    String[] temp = line.split("\\s+");  
                    if(temp.length > 1){  
                        float util =  Float.parseFloat(temp[temp.length-1]);  
                        ioUsage = (ioUsage>util)?ioUsage:util;  
                    }  
                }  
            }  
            if(ioUsage > 0){  
            	LOGGER.debug("本节点磁盘IO使用率为: " + ioUsage);      
                ioUsage /= 100;   
            }             
            in.close();  
            pro.destroy();  
        } catch (IOException e) {  
        	LOGGER.error(e.getMessage());  
        }     
        return ioUsage;  
	}
	
	/**
	 * 获取在线用户数，暂时不做
	 * @return
	 */
	public static int getOnlineterminals(){
		return 0;
	}
}
