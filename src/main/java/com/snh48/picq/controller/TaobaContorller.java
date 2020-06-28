package com.snh48.picq.controller;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.annotation.Log;
import com.snh48.picq.annotation.OperationType;
import com.snh48.picq.entity.taoba.TaobaMonitor;
import com.snh48.picq.service.TaobaService;
import com.snh48.picq.vo.ResultVO;

/**
 * 桃叭项目操作接口
 * 
 * @author shiro
 *
 */
@RestController
@RequestMapping("/taoba")
public class TaobaContorller {

	@Autowired
	private TaobaService taobaService;

	/**
	 * 新增一条桃叭监控配置
	 */
	@Log(desc = "新增桃叭监控配置", type = OperationType.ADD)
	@PostMapping(value = "/monitor/add")
	public ResultVO addMonitor(TaobaMonitor monitor) {
		ResultVO result = new ResultVO();
		taobaService.saveMonitor(monitor);
		result.setStatus(HttpsURLConnection.HTTP_OK);
		result.setCause("success");
		return result;
	}

	/**
	 * 删除一条桃叭监控配置并刷新缓存
	 * 
	 * @param monitorId 监控配置ID
	 * @param detailId  项目ID
	 */
	@Log(desc = "删除桃叭监控配置", type = OperationType.DEL)
	@PostMapping(value = "/monitor/delete")
	public ResultVO addMonitor(long monitorId, long detailId) {
		ResultVO result = new ResultVO();
		taobaService.deleteMonitor(monitorId, detailId);
		result.setStatus(HttpsURLConnection.HTTP_OK);
		result.setCause("success");
		return result;
	}

	/**
	 * 新增一个桃叭项目
	 */
	@Log(desc = "新增桃叭项目", type = OperationType.ADD)
	@PostMapping(value = "/add")
	public ResultVO add(String detailUrl) {
		ResultVO result = new ResultVO();
		int flag = taobaService.add(detailUrl);
		if (flag == 1) {
			result.setStatus(HttpsURLConnection.HTTP_OK);
			result.setCause("success");
		} else if (flag == 2) {
			result.setStatus(HttpsURLConnection.HTTP_INTERNAL_ERROR);
			result.setCause("链接格式有误！");
		}
		return result;
	}

	/**
	 * 删除桃叭集资项目
	 * 
	 * @param detailIds 项目ID，多个ID用逗号间隔
	 */
	@Log(desc = "删除桃叭项目", type = OperationType.DEL)
	@PostMapping("/delete")
	public ResultVO delete(String detailIds) {
		ResultVO result = new ResultVO();
		if (detailIds != null) {
			taobaService.delete(detailIds);
			result.setStatus(HttpsURLConnection.HTTP_OK);
			result.setCause("success");
		} else {
			result.setStatus(HttpsURLConnection.HTTP_INTERNAL_ERROR);
			result.setCause("ID不能为空");
		}
		return result;
	}

}
