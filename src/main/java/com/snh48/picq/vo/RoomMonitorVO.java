package com.snh48.picq.vo;

import java.io.Serializable;

import com.snh48.picq.entity.QQCommunity;
import com.snh48.picq.entity.snh48.RoomMonitor;

import lombok.Data;

/**
 * 
 * Copyright: Copyright (c) 2018 LanRu-Caifu
 * 
 * @ClassName: RoomMonitorVO.java
 * @Description: QQ群监控口袋房间的VO类
 *               <p>
 *               包含了QQ群监控口袋房间表和QQ群表的字段。
 *
 * @version: v1.0.0
 * @author: JuFF_白羽
 * @date: 2018年7月13日 上午12:05:53
 *
 */
@Data
public class RoomMonitorVO implements Serializable {

	private static final long serialVersionUID = 5281200514738222683L;

	/**
	 * QQ群监控口袋房间表
	 */
	private RoomMonitor roomMonitor;

	/**
	 * （yyh）QQ群信息
	 */
	private QQCommunity qqCommunity;

	public RoomMonitorVO() {
	}

	public RoomMonitorVO(RoomMonitor roomMonitor) {
		QQCommunity qqCommunity = new QQCommunity();
		this.roomMonitor = roomMonitor;
		this.qqCommunity = qqCommunity;
	}

	public RoomMonitorVO(QQCommunity qqCommunity) {
		RoomMonitor roomMonitor = new RoomMonitor();
		this.roomMonitor = roomMonitor;
		this.qqCommunity = qqCommunity;
	}

	public RoomMonitorVO(RoomMonitor roomMonitor, QQCommunity qqCommunity) {
		this.roomMonitor = roomMonitor;
		this.qqCommunity = qqCommunity;
	}

}
