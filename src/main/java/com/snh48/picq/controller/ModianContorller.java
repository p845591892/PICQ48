package com.snh48.picq.controller;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snh48.picq.annotation.Log;
import com.snh48.picq.annotation.OperationType;
import com.snh48.picq.entity.modian.MoDianPoolProject;
import com.snh48.picq.repository.modian.MoDianPoolProjectRepository;
import com.snh48.picq.service.MoDianService;
import com.snh48.picq.vo.ResultVO;

/**
 * @Description: 摩点项目控制层类
 * @author JuFF_白羽
 * @date 2018年9月17日 上午10:25:25
 */
@RestController
@RequestMapping("/modian")
public class ModianContorller {

	/**
	 * 摩点集资项目表DAO组件
	 */
	@Autowired
	private MoDianPoolProjectRepository moDianPoolProjectRepository;

	/**
	 * 摩点项目操作服务类
	 */
	@Autowired
	private MoDianService moDianService;

	/**
	 * @Description: 新增一条摩点项目
	 * @author JuFF_白羽
	 * @param id URL中的ID
	 */
	@Log(desc = "新增摩点监控对象", type = OperationType.ADD)
	@PostMapping("/add")
	public ResultVO addMoDianPoolProject(Long id) {
		ResultVO result = new ResultVO();
		if (id != null) {
			MoDianPoolProject moDianPoolProject = moDianPoolProjectRepository.save(new MoDianPoolProject(id));
			if (moDianPoolProject.getId() == id) {
				result.setStatus(HttpsURLConnection.HTTP_OK);
			} else {
				result.setStatus(500);
				result.setCause("新增失败");
			}
		} else {
			result.setStatus(400);
			result.setCause("ID不能为空");
		}
		return result;
	}

	/**
	 * @Description: 删除摩点项目
	 * @author JuFF_白羽
	 * @param id ID集合
	 */
	@Log(desc = "删除摩点监控对象", type = OperationType.DEL)
	@PostMapping("/delete")
	public ResultVO deleteMoDianPoolProject(String id) {
		ResultVO result = new ResultVO();
		if (id != null) {
			result.setStatus(moDianService.deleteMoDianPoolProject(id));
		} else {
			result.setStatus(400);
			result.setCause("ID不能为空");
		}
		return result;
	}

}
