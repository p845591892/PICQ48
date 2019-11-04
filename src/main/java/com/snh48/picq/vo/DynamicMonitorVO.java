package com.snh48.picq.vo;

import java.io.Serializable;

import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.entity.weibo.DynamicMonitor;

import lombok.Data;

/**
 * @ClassName: DynamicMonitorVO
 * @Description: 微博动态监控配置VO类
 *               <p>
 *               包含了微博动态监控和QQ表字段。
 * @author lcy
 * @date 2018年8月1日 上午11:53:35
 */
@Data
public class DynamicMonitorVO implements Serializable {

	private static final long serialVersionUID = 19306377431891742L;

	/**
	 * 微博动态监控配置
	 */
	private DynamicMonitor dynamicMonitor;

	/**
	 * （yyh）QQ群信息
	 */
	private QQCommunity qqCommunity;

	public DynamicMonitorVO() {
	}

	public DynamicMonitorVO(DynamicMonitor dynamicMonitor) {
		QQCommunity qqCommunity = new QQCommunity();
		this.dynamicMonitor = dynamicMonitor;
		this.qqCommunity = qqCommunity;
	}

	public DynamicMonitorVO(QQCommunity qqCommunity) {
		DynamicMonitor dynamicMonitor = new DynamicMonitor();
		this.dynamicMonitor = dynamicMonitor;
		this.qqCommunity = qqCommunity;
	}

	public DynamicMonitorVO(DynamicMonitor dynamicMonitor, QQCommunity qqCommunity) {
		this.dynamicMonitor = dynamicMonitor;
		this.qqCommunity = qqCommunity;
	}

}
