package com.epro.utils;

import java.io.File;
import java.net.URL;

import org.hyperic.sigar.Sigar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SigarUtil {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SigarUtil.class);
	
	private static class SigarUtilHolder{
		private static final SigarUtil INSTANCE = new SigarUtil();
		private static final Sigar Sigar = new Sigar();
	}
	
	private SigarUtil (){
		try {
			//String file = Resources.getResource("sigar/.sigar_shellrc").getFile();
			URL url = this.getClass().getResource("/");
			//File classPath = new File(url.getFile()).getParentFile();
			File classPath = new File(url.getFile());
			String path = System.getProperty("java.library.path");
			if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows) {
				path += ";" + classPath.getCanonicalPath();
			} else {
				path += ":" + classPath.getCanonicalPath();
			}
			System.setProperty("java.library.path", path);
			LOGGER.debug("java.classpath:"+classPath.getCanonicalPath());
			LOGGER.debug("java.library.path:"+path);
		} catch (Exception e) {
			LOGGER.error("请联系管理员！", e);
		}
	}
	public static final Sigar getInstance(){
		return SigarUtilHolder.Sigar;
	}
	public static final SigarUtil getSigarUtilInstance(){
		return SigarUtilHolder.INSTANCE;
	}
}
