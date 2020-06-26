package com.snh48.picq.https;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.snh48.picq.entity.taoba.TaobaDetail;
import com.snh48.picq.entity.taoba.TaobaJoin;

import lombok.extern.log4j.Log4j2;

/**
 * 桃叭接口工具
 * 
 * @author shiro
 *
 */
@Log4j2
@Transactional
public class TaobaTool extends JsonPICQ48 {

	/**
	 * 获取桃叭集资项目信息。通过Https的方式发送请求，获得json结果，解析后生成{@link TaobaDetail}对象。
	 * 
	 * @param id 项目ID
	 * @return {@link TaobaDetail}
	 */
	public static TaobaDetail getDetail(long id) {
		try {
			JSONObject detailObj = jsonDetail(id);
			TaobaDetail detail = new TaobaDetail();
			setTaobaDetail(detail, detailObj);
			return detail;
		} catch (Exception e) {
			log.error("获取桃叭项目信息失败，id={}，异常：{}", id, e.toString());
		}
		return null;
	}

	/**
	 * 获取桃叭集资项目详细列表。通过Https的方式发送请求，获得json结果，解析后生成{@link TaobaJoin}的集合。
	 * 
	 * @param ismore      是否更多
	 * @param limit       数据条数
	 * @param id          项目ID
	 * @param offset      已翻果条数
	 * @param requestTime 请求时间戳
	 * @return {@link TaobaJoin}的集合
	 */
	public static List<TaobaJoin> getTaobaJoin(boolean ismore, int limit, long id, int offset) {
		List<TaobaJoin> joins = new ArrayList<TaobaJoin>();
		try {
			JSONArray joinArray = jsonJoin(ismore, limit, id, offset);
			for (int i = 0; i < joinArray.length(); i++) {
				JSONObject joinObj = joinArray.getJSONObject(i);
				TaobaJoin join = new TaobaJoin();
				join.setDetailId(id); // 所属项目ID
				setTaobaJoin(join, joinObj);
				joins.add(join);
			}

		} catch (Exception e) {
			log.error("获取桃叭集资详情失败，ismore={}, limit={}, id={}, offset={}，异常：{}", ismore, limit, id, offset,
					e.toString());
		}
		return joins;
	}

}