package com.snh48.picq.controller;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.annotation.Log;
import com.snh48.picq.annotation.OperationType;
import com.snh48.picq.entity.weibo.DynamicMonitor;
import com.snh48.picq.service.DynamicMonitorService;
import com.snh48.picq.vo.ResultVO;

/**
 * @Description: 微博动态监控数据操作接口
 * @author JuFF_白羽
 * @date 2018年9月21日 上午11:21:14
 */
@RestController
public class DynamicMonitorContorller {

	@Autowired
	private DynamicMonitorService dynamicMonitorService;

	/**
	 * @Description: 新增一条微博动态监控数据
	 * @author: JuFF_白羽
	 * @date: 2018年9月20日 下午10:33:07
	 */
	@Log(desc = "新增微博监控配置", type = OperationType.ADD)
	@PostMapping("/dynamic-monitor/add")
	public ResultVO addDynamicMonitor(DynamicMonitor dynamicMonitor) {
		ResultVO result = new ResultVO();
		if (dynamicMonitor.getCommunityId() != null && dynamicMonitor.getUserId() != null) {
			dynamicMonitorService.addDynamicMonitor(dynamicMonitor);
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
			result.setCause("QQ号或者用户ID不能为空");
		}
		return result;
	}

	/**
	 * @Title: deleteRoomMonitor
	 * @Description: 删除一条微博动态监控配置
	 * @author JuFF_白羽
	 * @param id 配置ID
	 */
	@Log(desc = "删除微博监控配置", type = OperationType.DEL)
	@PostMapping("/dynamic-monitor/delete")
	public ResultVO deleteDynamicMonitor(Long id) {
		ResultVO result = new ResultVO();
		if (id != null) {
			dynamicMonitorService.deleteDynamicMonitor(id);
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			result.setStatus(HttpsURLConnection.HTTP_INTERNAL_ERROR);
			result.setCause("ID不能为空");
		}
		return result;
	}

	/**
	 * @Description: 新增一条微博用户到微博监控
	 * @author JuFF_白羽
	 * @param containerId 容器ID
	 */
	@Log(desc = "新增微博监控对象", type = OperationType.ADD)
	@PostMapping("/weibo/add")
	public ResultVO addWeiboUser(Long containerId) {
		ResultVO result = new ResultVO();
		if (containerId == null) {
			result.setStatus(HttpsURLConnection.HTTP_BAD_REQUEST);
			result.setCause("缺失参数");
		} else {
			dynamicMonitorService.addWeiboUser(containerId);
			result.setStatus(HttpsURLConnection.HTTP_OK);
		}
		return result;
	}

	/**
	 * @Description: 根据微博ID删除监控微博信息以及相关配置，多个id可使用“,”连接
	 * @author JuFF_白羽
	 * @param id 微博用户ID
	 */
	@Log(desc = "删除微博监控对象及配置", type = OperationType.DEL)
	@PostMapping("/weibo/delete")
	public ResultVO deleteWeiboUser(String id) {
		ResultVO result = new ResultVO();
		if (id != null) {
			String[] ids = id.split(",");
			for (String userId : ids) {
				dynamicMonitorService.deleteWeiboUser(userId);
			}
			result.setStatus(HttpsURLConnection.HTTP_OK);
		} else {
			result.setStatus(400);
			result.setCause("ID不能为空");
		}
		return result;
	}

}
