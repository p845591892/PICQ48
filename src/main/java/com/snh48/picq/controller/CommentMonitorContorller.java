package com.snh48.picq.controller;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.entity.modian.CommentMonitor;
import com.snh48.picq.repository.modian.CommentMonitorRepostiory;
import com.snh48.picq.vo.ResultVO;

/**
 * 
 * Copyright: Copyright (c) 2018 LanRu-Caifu
 * 
 * @ClassName: CommentMonitorApi.java
 * @Description: 摩点项目监控数据操作接口
 *
 * @version: v1.0.0
 * @author: JuFF_白羽
 * @date: 2018年9月20日 下午10:18:20
 *
 */
@RestController
@RequestMapping("/modian-monitor")
public class CommentMonitorContorller {

	/**
	 * 摩点项目评论监控配置表DAO组件
	 */
	@Autowired
	private CommentMonitorRepostiory commentMonitorRepostiory;

	/**
	 * 
	 * @Description: 新增一条摩点项目监控数据
	 * @author: JuFF_白羽
	 * @date: 2018年9月20日 下午10:33:07
	 */
	@PostMapping("/add")
	public ResultVO addCommentMonitor(CommentMonitor commentMonitor) {
		ResultVO result = new ResultVO();
		if (commentMonitor.getCommunityId() != null && commentMonitor.getProjectId() != null) {
			CommentMonitor newCommentMonitor = commentMonitorRepostiory.save(commentMonitor);
			if (newCommentMonitor != null) {
				result.setStatus(HttpsURLConnection.HTTP_OK);
			} else {
				result.setStatus(HttpsURLConnection.HTTP_INTERNAL_ERROR);
				result.setCause("新增失败");
			}
		} else {
			result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
			result.setCause("QQ号或者项目ID不能为空");
		}
		return result;
	}

	/**
	 * @Title: deleteRoomMonitor
	 * @Description: 删除一条摩点项目监控配置
	 * @author JuFF_白羽
	 * @param id 配置ID
	 */
	@PostMapping("/delete")
	public ResultVO deleteRoomMonitor(Long id) {
		ResultVO result = new ResultVO();
		if (id != null) {
			commentMonitorRepostiory.deleteById(id);
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
			result.setCause("ID不能为空");
		}
		return result;
	}

}
