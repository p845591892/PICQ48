package com.snh48.picq.service.impl;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.repository.modian.MoDianCommentRepository;
import com.snh48.picq.repository.modian.MoDianPoolProjectRepository;
import com.snh48.picq.service.MoDianService;

@Service
@Transactional
public class MoDianServiceImpl implements MoDianService {

	/**
	 * 摩点集资项目表DAO组件
	 */
	@Autowired
	private MoDianPoolProjectRepository moDianPoolProjectRepository;

	/**
	 * 摩点评论表DAO组件
	 */
	@Autowired
	private MoDianCommentRepository moDianCommentRepository;

	public Integer deleteMoDianPoolProject(String id) {
		String[] ids = id.split(",");
		for (String idStr : ids) {
			moDianCommentRepository.deleteByprojectId(Long.parseLong(idStr));
			moDianPoolProjectRepository.deleteById(Long.parseLong(idStr));
		}
		return HttpsURLConnection.HTTP_OK;
	}

}
