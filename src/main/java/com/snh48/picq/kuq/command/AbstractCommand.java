package com.snh48.picq.kuq.command;

import java.util.List;

import cc.moecraft.icq.event.events.message.EventMessage;

/**
 * 通用指令方法
 * 
 * @author shiro
 *
 */
public abstract class AbstractCommand {

	protected abstract <T> void respond(EventMessage event, List<T> list);

	protected abstract <T> void respond(EventMessage event, T t);

}
