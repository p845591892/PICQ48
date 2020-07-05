package com.snh48.picq.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.snh48.picq.vo.MtboxVO;

/**
 * @Description: 页面跳转请求DAO组件
 * @author JuFF_白羽
 * @date 2018年9月26日 上午10:58:12
 */
@Mapper
public interface WebDao {

	List<MtboxVO> findActiveRoom(Map<String, Date> params);

	List<MtboxVO> findActiveMembers(Map<String, Date> params);

	List<MtboxVO> findModianDetailsTableByIds(String[] ids);

	List<MtboxVO> findTaobaJoinTableByIds(String[] ids);

	List<MtboxVO> findBar();

}
