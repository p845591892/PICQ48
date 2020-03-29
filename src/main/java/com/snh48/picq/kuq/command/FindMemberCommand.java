package com.snh48.picq.kuq.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.snh48.picq.core.Common;
import com.snh48.picq.entity.snh48.Member;
import com.snh48.picq.kuq.KuqManage;
import com.snh48.picq.service.ResourceManagementService;
import com.snh48.picq.utils.StringUtil;
import com.snh48.picq.vo.MemberVO;

import cc.moecraft.icq.command.CommandProperties;
import cc.moecraft.icq.command.interfaces.EverywhereCommand;
import cc.moecraft.icq.event.events.message.EventMessage;
import cc.moecraft.icq.user.User;

/**
 * 查询成员指令
 * 
 * @author shiro
 *
 */
@Component
public class FindMemberCommand implements EverywhereCommand {

	@Autowired
	private ResourceManagementService resourceManagementService;

	@Override
	public CommandProperties properties() {
		return new CommandProperties(Common.COMMAND_NAME_FIND_MEMBER, Common.COMMAND_ALIAS_FIND_MEMBER);
	}

	@Override
	public String run(EventMessage event, User sender, String command, ArrayList<String> args) {
		if (args == null || args.size() == 0) {
			return "缺少参数！" + Common.COMMAND_REPLAY_HELP;
		}
		// 设置查询参数
		String arg = args.get(0);
		MemberVO vo = new MemberVO();
		if (StringUtil.isChinese(arg)) {
			vo.setName(arg);
		} else if (StringUtil.isEnglish(arg)) {
			vo.setAbbr(arg);
		} else {
			return "参数错误！";
		}

		// 查询
		Page<Member> memberPage = resourceManagementService.getMembers(1, 20, vo);
		if (memberPage.isEmpty()) {
			return "未查找到相关成员。";
		}
		List<Member> members = memberPage.getContent();
		int size = members.size();
		if (size > 5) {
			event.respond("查找到至少5位相关成员。");
			List<Member> responMembers = new ArrayList<Member>();// 精
			members.stream().filter((p) -> p.getName().equals(arg) || p.getAbbr().equals(arg))
					.forEach((p) -> responMembers.add(p));
			respond(event, responMembers);

		} else {
			respond(event, members);
		}

		return null;
	}

	/**
	 * 回复成员资料
	 * 
	 * @param event 活动对象
	 * @param list  成员列表
	 */
	private void respond(EventMessage event, List<Member> list) {
		int size = list.size();
		if (size == 0) {
			event.respond("由于相关成员过多，请提高参数精确度。");
			return;
		} else {
			event.respond("最接近的成员有" + size + "位：");
		}
		list.forEach((p) -> {
			String message = KuqManage.memberMessageBuilder(p);
			event.respond(message, false);
		});
	}

}