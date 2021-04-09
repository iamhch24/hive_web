package com.ee.hive.controller;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.ee.hive.dao.ChanChatDao;
import com.ee.hive.dao.MsgChatDao;
import com.ee.hive.entities.ChanChat;
import com.ee.hive.entities.ChatMessage;
import com.ee.hive.entities.MsgChat;

@Controller
public class ChatController {

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private ChanChat chanchat;

	@MessageMapping("/channel.sendMessage/{roomNo}")
	@SendTo("/topic/channel/{roomNo}")
	public ChatMessage sendChannel(@DestinationVariable String roomNo, @Payload ChatMessage chatMessage) {
		System.out.println("/channel.sendMessage/{roomNo} : " + roomNo);
		chanchat.setChanid(chatMessage.getChanid());
		chanchat.setSendername(chatMessage.getSender());
		chanchat.setSenderid(chatMessage.getUserid());
		chanchat.setContent(chatMessage.getContent());
		ChanChatDao chanchatdao = sqlSession.getMapper(ChanChatDao.class);
		chanchatdao.insertRow(chanchat);
		return chatMessage;
	}

	@MessageMapping("/msgroom.sendMessage/{roomNo}")
	@SendTo("/topic/msgroom/{roomNo}")
	public ChatMessage sendMsgRoom(@DestinationVariable String roomNo, @Payload ChatMessage chatMessage) {
		System.out.println("/msgroom.sendMessage/{roomNo} : " + roomNo);
		MsgChat msgchat = new MsgChat();
		msgchat.setRoomseq(roomNo);
		msgchat.setContent(chatMessage.getContent());
		msgchat.setSendername(chatMessage.getSender());
		msgchat.setSenderid(chatMessage.getUserid());
		msgchat.setGetterid(chatMessage.getOtherid());
		MsgChatDao msgchatdao = sqlSession.getMapper(MsgChatDao.class);
		msgchatdao.insertRow(msgchat);
		return chatMessage;
	}

	@MessageMapping("/channel.addUser/{roomNo}")
	@SendTo("/topic/channel/{roomNo}")
	public ChatMessage addChannel(@DestinationVariable String roomNo, @Payload ChatMessage chatMessage,
			SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		System.out.println("/channel.addUser/{roomNo} : " + roomNo + ", username= " + chatMessage.getSender());
		return chatMessage;
	}

	@MessageMapping("/msgroom.addUser/{roomNo}")
	@SendTo("/topic/msgroom/{roomNo}")
	public ChatMessage addMsgRoom(@DestinationVariable String roomNo, @Payload ChatMessage chatMessage,
			SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		System.out.println("/msgroom.addUser/{roomNo} : " + roomNo + ", username= " + chatMessage.getSender());
		return chatMessage;
	}

}