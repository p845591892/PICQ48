package com.snh48.picq.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description: 口袋房间消息VO
 * @author JuFF_白羽
 * @date 2019年4月10日 下午6:16:01
 */
@Data
public class RoomMessageVO implements Serializable {

	private static final long serialVersionUID = 7519962805620855349L;

	private String id;
	private String msgTime;
	private String senderName;
	private Long senderId;
	private Long roomId;
	private String msgObject;
	private String msgContent;
	private String isSend;
	private String searchText;

}
