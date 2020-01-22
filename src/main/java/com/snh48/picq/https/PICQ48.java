package com.snh48.picq.https;

import java.util.List;

/**
 * 爬取数据总接口。
 * 
 * @author shiro
 *
 */
public interface PICQ48 {
	
	<T> List<T> get(Class<T> clazz);

}
