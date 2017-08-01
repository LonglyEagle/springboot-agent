package com.epro.utils;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.epro.constant.DefaultConfigEnum;
import com.epro.domain.ServerInfo;

/**
 * 服务器信息根据权重排序
 * @author Grant.You
 *
 */
@Component
public class ServerInfoComparator implements Comparator<ServerInfo> {
	
	@Override
	public int compare(ServerInfo o1, ServerInfo o2) {
		int[] wInt = getWeight();
		int result = sort(o1.getCpu(),o2.getCpu()) * wInt[0] 
				+ sort(o1.getMemory(),o2.getMemory()) * wInt[1]
				+ sort(o1.getIo(),o2.getIo()) * wInt[2];
		return result;
	}
	
	private int sort(double d1,double d2){
		if (d1 < d2)
			return -1;
		if (d1 > d2)
			return 1;
		return 0;
	}
	
	private int[] getWeight(){
		
		int [] wInt = new int[]{1,1,1};
		if(StringUtils.isBlank(DefaultConfigEnum.WEIGHT)){
			return wInt;
		}
		
		String[] wStr = DefaultConfigEnum.WEIGHT.split(":");
		
		for(int i=0;i < wStr.length;i++){
			wInt[i] = Integer.parseInt(wStr[i]);
		}
		
		return wInt;
	}

}
