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

}
