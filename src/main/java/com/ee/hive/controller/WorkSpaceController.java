package com.ee.hive.controller;

import java.text.SimpleDateFormat;	
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ee.hive.dao.AvatasDao;
import com.ee.hive.dao.ChanChatDao;
import com.ee.hive.dao.ChanMemberDao;
import com.ee.hive.dao.ChannelDao;
import com.ee.hive.dao.MsgChatDao;
import com.ee.hive.dao.MsgRoomDao;
import com.ee.hive.dao.UserDao;
import com.ee.hive.dao.WorkMemberDao;
import com.ee.hive.dao.WorkSpaceDao;
import com.ee.hive.entities.Avatas;
import com.ee.hive.entities.ChanChat;
import com.ee.hive.entities.ChanMember;
import com.ee.hive.entities.Channel;
import com.ee.hive.entities.MsgChat;
import com.ee.hive.entities.MsgRoom;
import com.ee.hive.entities.User;
import com.ee.hive.entities.WorkMember;
import com.ee.hive.entities.WorkSpace;

@Controller
public class WorkSpaceController {
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	Channel channel;
	@Autowired
	WorkSpace workspace;
	@Autowired
	MsgRoom msgroom;
	@Autowired
	User user;

	/* 홈페이지 */
	@RequestMapping(value = "/")
	public String Home() {
		return "index";
	}

	/* 로그인 */
	@RequestMapping(value = "/loginUp", method = RequestMethod.POST)
	public String loginUp(Model model, @RequestParam String email, HttpSession session) {
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		WorkSpaceDao workspacedao = sqlSession.getMapper(WorkSpaceDao.class);
		ChannelDao channelDao = sqlSession.getMapper(ChannelDao.class);
		User user = userdao.selectEmail(email);
		String userid = user.getUserid();
		String username = user.getName();
		String workid = user.getLastwork();
		String workname = workspacedao.selectWorkName(workid);
		String chanid = user.getLastchannel();
		String channame = channelDao.selectName(chanid);
		String ownerid = workspacedao.selectOwnerid(workid);
		session.setAttribute("sessionUserid", userid);
		session.setAttribute("sessionUsername", username);
		session.setAttribute("sessionEmail", email);
		session.setAttribute("sessionWorkid", workid);
		session.setAttribute("sessionWorkname", workname);
		session.setAttribute("sessionChanid", chanid);
		session.setAttribute("sessionChanname", channame);
		session.setAttribute("sessionAvata", user.getPhoto());
		if(user.getLastmessage() != null) {
			session.setAttribute("sessionMessage", user.getLastmessage());
		}
		if(userid.equals(ownerid)) {
			session.setAttribute("sessionOwner", "Y");
		}else {
			session.setAttribute("sessionOwner", "N");
		}
		if(user.getLastwork() == null) {
			return "join/work_insert";
		}
		if(user.getLastchannel() == null) {
			return "join/channel_insert";
		}
		return "redirect:channel";
	}

	@RequestMapping(value = "/loginAjax", method = RequestMethod.POST)
	@ResponseBody
	public String loginAjax(Model model, @RequestParam String email, @RequestParam String pwd) {
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		User user = userdao.selectEmail(email);
		if (user == null) {
			return "n";
		} else {
			boolean passchk = BCrypt.checkpw(pwd, user.getPwd());
			if (passchk) {
				return "y";
			} else {
				return "n";
			}
		}
	}

	/* 이메일과 비번 입력해서 회원가입 */
	@RequestMapping(value = "/insertUserSave", method = RequestMethod.POST)
	public String insertUserSave(Model model, @RequestParam String email, @RequestParam String pwd,
			@RequestParam String name, HttpSession session) {
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		String userid = UUID.randomUUID().toString();
		String encodepassword = hashPassword(pwd);
		user.setUserid(userid);
		user.setEmail(email);
		user.setPwd(encodepassword);
		user.setName(name);
		userdao.insertRow(user);
		session.setAttribute("sessionUserid", userid);
		if (name != "") {
			session.setAttribute("sessionUsername", name);
		} else {
			session.setAttribute("sessionUsername", "이름 없음");
		}
		return "join/work_insert";
	}

	@RequestMapping(value = "/insertUserAjax", method = RequestMethod.POST)
	@ResponseBody
	public String inserUserAjax(Model model, @RequestParam String email, @RequestParam String pwd,
			@RequestParam String pwd2) {
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		User user = userdao.selectEmail(email);
		if (!email.equals("") && !pwd.equals("") && !pwd2.equals("")) {
			if (user == null && pwd.equals(pwd2)) {
				return "y";
			} else if (user == null && !pwd.equals(pwd2)) {
				return "p";
			} else {
				return "e";
			}
		} else {
			return "n";
		}
	}

	@RequestMapping(value = "/saveWorkSpace", method = RequestMethod.POST)
	public String saveWorkSpace(Model model, @RequestParam String name, HttpSession session) {
		/* 워크스페이스 생성 */
		String userid = (String) session.getAttribute("sessionUserid");
		WorkSpaceDao workspacedao = sqlSession.getMapper(WorkSpaceDao.class);
		String workid = UUID.randomUUID().toString();
		WorkSpace workspace = new WorkSpace();
		workspace.setWorkid(workid);
		workspace.setName(name);
		workspace.setOwnerid(userid);
		workspacedao.insertRow(workspace);
		/* 라스트 워크 업데이트 */
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		HashMap<String, String> hashmap = new HashMap<>();
		hashmap.put("workid", workid);
		hashmap.put("userid", userid);
		userdao.updateLastWork(hashmap);
		session.setAttribute("sessionWorkid", workid);
		session.setAttribute("sessionWorkname", name);
		session.setAttribute("sessionOwner", "Y");
		/* 워크스페이스 구독 */
		WorkMemberDao workmemberdao = sqlSession.getMapper(WorkMemberDao.class);
		WorkMember workmember = new WorkMember();
		workmember.setUserid(userid);
		workmember.setWorkid(workid);
		workmemberdao.insertRow(workmember);
		return "join/channel_insert";
	}

	@RequestMapping(value = "/saveChannel", method = RequestMethod.POST)
	public String saveChannel(Model model, @RequestParam String name, HttpSession session) {
		/* 채널생성 */
		String userid = (String) session.getAttribute("sessionUserid");
		String workid = (String) session.getAttribute("sessionWorkid");
		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
		String chanid = UUID.randomUUID().toString();
		Channel channel = new Channel();
		channel.setChanid(chanid);
		channel.setName(name);
		channel.setWorkid(workid);
		channeldao.insertRow(channel);
		/* 업데이트 라스트 채널 */
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		HashMap<String, String> hashmap = new HashMap<>();
		hashmap.put("chanid", chanid);
		hashmap.put("userid", userid);
		userdao.updateLastChannel(hashmap);
		session.setAttribute("sessionChanid", chanid);
		session.setAttribute("sessionChanname", name);
		/* 채널 구독 */
		ChanMemberDao chanmemberdao = sqlSession.getMapper(ChanMemberDao.class);
		ChanMember chanmember = new ChanMember();
		chanmember.setUserid(userid);
		chanmember.setChanid(chanid);
		chanmemberdao.insertRow(chanmember);
		return "join/member_invite";
	}

	@RequestMapping(value = "/inviteUser", method = RequestMethod.POST)
	public String inviteUser(@RequestParam String email, @RequestParam String email2, @RequestParam String email3,
			HttpSession session) {
		String workid = (String) session.getAttribute("sessionWorkid");
		String Chanid = (String) session.getAttribute("sessionChanid");
		String userid = (String) session.getAttribute("sessionUserid");
		ArrayList<String> emails = new ArrayList<>();
		emails.add(email);
		emails.add(email2);
		emails.add(email3);
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		WorkMemberDao workmemberdao = sqlSession.getMapper(WorkMemberDao.class);
		ChanMemberDao chanmemberdao = sqlSession.getMapper(ChanMemberDao.class);
		MsgRoomDao msgroomdao = sqlSession.getMapper(MsgRoomDao.class);
		WorkMember workmember = new WorkMember();
		ChanMember chanmember = new ChanMember();
		MsgRoom msgroom = new MsgRoom();
		ArrayList<String> members = new ArrayList<>();
		for (String item : emails) {
			User other = userdao.selectEmail(item);
			if (other != null) {
				if (!other.getUserid().equals(userid)) {
					String otherid = other.getUserid();
					members.add(otherid);
					workmember.setUserid(otherid);
					workmember.setWorkid(workid);
					workmemberdao.insertRow(workmember);
					chanmember.setUserid(otherid);
					chanmember.setChanid(Chanid);
					chanmemberdao.insertRow(chanmember);
					msgroom.setWorkid(workid);
					msgroom.setUserid(userid);
					msgroom.setOtherid(otherid);
					msgroomdao.insertRow(msgroom);
				}
			}
		}
		ArrayList<String> copyids = new ArrayList<>();
		copyids.addAll(members);
		for(String otherid : members) {
			copyids.remove(otherid);
			if(!copyids.isEmpty()) {
				for(String copyid : copyids) {
					msgroom.setWorkid(workid);
					msgroom.setUserid(otherid);
					msgroom.setOtherid(copyid);
					msgroomdao.insertRow(msgroom);
				}
			}
		}
		return "redirect:channel";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:";
	}
	
	@RequestMapping(value = "/appendWorkspace", method = RequestMethod.GET)
	public String appendWorkspace(HttpSession session) {
		return "join/work_insert";
	}

	@RequestMapping(value = "/channel", method = RequestMethod.GET)
	public String channel(Model model, HttpSession session) {
		if (session.getAttribute("sessionUserid") == null){
			return "index";
		}
		String userid = (String) session.getAttribute("sessionUserid");
		String workid = (String) session.getAttribute("sessionWorkid");
		String chanid = (String) session.getAttribute("sessionChanid");
		/* 워크스페이스 리스트 */
		callWorkList(model, userid);
		/* 채널 리스트 */
		callChannelList(model, userid);
		/* 채널 내용, 채팅창 상단에 오늘 날짜 표시*/
		callChatRoom(model, userid);
		/* 채팅보내는 장소를 채널룸으로 설정, 채팅창 상단에 채널 타이틀 표시 */
		callChannelChat(model, userid);
		/* 채팅 설정 스크립트 */
		callWebsocket(model, userid, workid);
		/* 사용자가 최근에 방문한 워크스페이스의 멤버들을 뿌려줌 채널추가 테이블에 */
		callWorkUser(model, userid);
		/* 기본 사용자 정보들을 뿌려줌(테이블에) */
		callUser(model, userid);
		/* 워크스페이스 수정 테이블 정보 */
		callWorkspace(model, workid);
		/* 채널 수정 테이블 정보 */
		callChannel(model,chanid);
		/*아바타목록 불러오기*/
		callAvatas(model);
		return "channels/channel";
	}
	
	/* 채널추가 
	 * channelname = 모달창에서 입력한 추가될 채널의 이름
	 * useridsinmodal = channel.html 모달창에서 체크된(선택된) 유저아이디들 */
	@RequestMapping(value = "/appendChannel", method = RequestMethod.POST)
	public String appendChannel(Model model, @RequestParam String channelname, @RequestParam ArrayList<String> useridsinmodal,
			HttpSession session) {
		if (session.getAttribute("sessionUserid") == null){
			return "index";
		}
		String userid = (String) session.getAttribute("sessionUserid");
		String workid = (String) session.getAttribute("sessionWorkid");
		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class); 
		String chanid = UUID.randomUUID().toString(); 
		Channel channel = new Channel();
		if(channelname.equals("")) {
			channel.setName("이름 없음");
		} else {
			channel.setName(channelname);
		}
		channel.setChanid(chanid); 
		channel.setWorkid(workid);
		channeldao.insertRow(channel);
		ChanMemberDao chanmemberdao = sqlSession.getMapper(ChanMemberDao.class);
		chanmemberdao.appendChanMember(chanid, userid);   //본인 추가
		if(useridsinmodal.size() != 1) {
			useridsinmodal.remove("0");
			for(String itemids : useridsinmodal) {
				chanmemberdao.appendChanMember(chanid, itemids);  //체크된 멤버 추가
			}
		} 
		
		return "redirect:channel";
	}

	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public String message(Model model, HttpSession session) {
		if (session.getAttribute("sessionUserid") == null){
			return "index";
		}
		String userid = (String) session.getAttribute("sessionUserid");
		String workid = (String) session.getAttribute("sessionWorkid");
		/* 워크스페이스 리스트 */
		callWorkList(model, userid);
		/* 메시지 리스트 */
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		MsgRoomDao msgroomdao = sqlSession.getMapper(MsgRoomDao.class);
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.putIfAbsent("workid", workid); /* 워크스페이스에 방문을 한번도 안하면 에러남 */
		hash.putIfAbsent("userid", userid);
		ArrayList<MsgRoom> msgrs = msgroomdao.selectList(hash);
		ArrayList<MsgRoom> msgrooms = new ArrayList<MsgRoom>();
		String otherid;
		for (MsgRoom msgr : msgrs) {
			if (userid.equals(msgr.getOtherid())) {	// 사용자 아이디가 otherid라면 otherids에 userid를 추가 
				otherid = msgr.getUserid();
			} else {
				otherid = msgr.getOtherid();
			}
			MsgRoom msgroom = new MsgRoom();
			User user = userdao.selectOne(otherid);
			msgroom.setSeq(msgr.getSeq());
			msgroom.setOthername(user.getName());
			msgrooms.add(msgroom);
		}
		model.addAttribute("msgrooms", msgrooms);
		/* 메시지 내용 */
		String roomseq = userdao.selectLastMsgRoom(userid);
		MsgRoom checkroom = msgroomdao.selectOne(roomseq);
		if (checkroom != null) {
			MsgChatDao msgchatdao = sqlSession.getMapper(MsgChatDao.class);
			ArrayList<MsgChat> msgchats = msgchatdao.selectMessage(roomseq);
			model.addAttribute("msgchats", msgchats);
		/* 채팅보내는 장소를 메시지룸으로 설정, 채팅창 상단에 메시지 타이틀 표시 */
			MsgRoom titlemsgroom = msgroomdao.selectmsgroom(roomseq);
			String otherid2;
			if(userid.equals(titlemsgroom.getUserid())) {
				otherid2 = titlemsgroom.getOtherid();
			} else {
				otherid2 = titlemsgroom.getUserid();
			}
			User user = userdao.selectOne(otherid2);
			model.addAttribute("msgroomname", user.getName());
			model.addAttribute("channelNo", null);
			model.addAttribute("msgroomNo", roomseq);
			model.addAttribute("otherid", user.getUserid()); 
		}
		// 채팅창에 '오늘' 표시
		SimpleDateFormat format1 = new SimpleDateFormat ( "MM월 dd일");
		Date time = new Date();		
		String today_mmdd = format1.format(time);
		model.addAttribute("today_mmdd", today_mmdd);
		/* 채팅 설정 스크립트 */
		callWebsocket(model, userid, workid);
		/* 사용자가 최근에 방문한 워크스페이스의 멤버들을 뿌려줌 메시지추가 테이블에 */
		HashMap<String, String> hashmap = new HashMap<>();
		hashmap.put("workid", workid);
		hashmap.put("userid", userid);
		ArrayList<User> users = userdao.selectMsgUser(hashmap);
		ArrayList<User> members = userdao.selectMemberIDNamePhoto(workid);
		model.addAttribute("users", users);
		model.addAttribute("members", members);
		/* 사용자 회원정보 수정 */
		callUser(model, userid);
		/* 아바타 목록 */
		callAvatas(model);
		/* 워크스페이스 수정 테이블 정보 */
		callWorkspace(model, workid);
		/* 채널 수정에 필요한 정보 */
		String chanid = userdao.selectLastChannel(userid);
		callChannel(model, chanid);
		return "messages/message";
	}
	
	@RequestMapping(value = "/choiceWork", method = RequestMethod.POST)
	public String choiceWork(Model model, @RequestParam String workid, HttpSession session) {
		if (session.getAttribute("sessionUserid") == null){
			return "index";
		}
		String userid = (String) session.getAttribute("sessionUserid");
		/* 업데이트 라스트 워크 */
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		WorkSpaceDao workspacedao = sqlSession.getMapper(WorkSpaceDao.class);
		ChanMemberDao chanmemberdao = sqlSession.getMapper(ChanMemberDao.class);
		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.putIfAbsent("userid", userid);
		hashmap.putIfAbsent("workid", workid);
		String workname = workspacedao.selectWorkName(workid);
		String ownerid = workspacedao.selectOwnerid(workid);	
		String chanid = chanmemberdao.selectChanid(hashmap);
		String channame = channeldao.selectName(chanid);
		userdao.updateLastWork(hashmap);
		session.setAttribute("sessionWorkid", workid);
		session.setAttribute("sessionWorkname", workname);
		if(userid.equals(ownerid)) {
			session.setAttribute("sessionOwner", "Y");
		}else {
			session.setAttribute("sessionOwner", "N");
		}
		hashmap.put("chanid", chanid);
		userdao.updateLastChannel(hashmap);
		session.setAttribute("sessionChanid", chanid);
		session.setAttribute("sessionChanname", channame);
		return "redirect:channel";
	}

	@RequestMapping(value = "/choiceChannel", method = RequestMethod.GET)
	public String choiceChannel(Model model, @RequestParam String chanid, HttpSession session) {
		if (session.getAttribute("sessionUserid") == null){
			return "index";
		}
		String userid = (String) session.getAttribute("sessionUserid");
		/* 업데이트 라스트 채널 */
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
		HashMap<String, String> hashmap = new HashMap<String, String>();
		String channelname = channeldao.selectName(chanid);
		hashmap.putIfAbsent("userid", userid);
		hashmap.putIfAbsent("chanid", chanid);
		userdao.updateLastChannel(hashmap);
		session.setAttribute("sessionChanid", chanid);
		session.setAttribute("sessionChanname", channelname);
		return "redirect:channel";
	}

	@RequestMapping(value = "/choiceMessage", method = RequestMethod.GET)
	public String choiceMessage(Model model, @RequestParam String roomseq, HttpSession session) {
		if (session.getAttribute("sessionUserid") == null){
			return "index";
		}
		String userid = (String) session.getAttribute("sessionUserid");
		/* 업데이트 라스트 채널 */
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.putIfAbsent("userid", userid);
		hashmap.putIfAbsent("roomseq", roomseq);
		userdao.updateLastMsgRoom(hashmap);
		session.setAttribute("sessionMsgRoomseq", roomseq);
		return "redirect:message";
	}
	
	@RequestMapping(value="/updateUser", method = RequestMethod.POST)
	public String updateUser(Model model, @ModelAttribute User user, @RequestParam String pwd1, HttpSession session) {
		if (session.getAttribute("sessionUserid") == null){
			return "index";
		}
		String userid = (String) session.getAttribute("sessionUserid");
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		User olduser = userdao.selectOne(userid);
		if(user.getName().equals("")) {
			user.setName(olduser.getName());
		}
		if(user.getPhone().equals("")) {
			user.setPhone(olduser.getPhone());
		}
		if(user.getJob().equals("")) {
			user.setJob(olduser.getJob());
		}
		
		user.setUserid(userid);
		userdao.updateUser(user);
		if(!pwd1.equals("")) {
			String pwd = hashPassword(pwd1);	
			HashMap<String, String> hashmap = new HashMap<>();
			hashmap.put("userid", userid);
			hashmap.put("pwd", pwd);
			userdao.updatePwd(hashmap);
		}
		return "redirect:channel";
	}
	
	@RequestMapping(value="/updateWorkspace", method = RequestMethod.POST)
	public String updateWorkspace(Model model, @ModelAttribute WorkSpace workspace, @RequestParam String inviteemail,
			@RequestParam String withdrawpwd, @RequestParam String deletepwd,HttpSession session) {
		if (session.getAttribute("sessionUserid") == null){
			return "index";
		}
		String workid = workspace.getWorkid();
		String userid = (String) session.getAttribute("sessionUserid");
		String useremail = (String) session.getAttribute("sessionEmail");
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		WorkSpaceDao workspacedao = sqlSession.getMapper(WorkSpaceDao.class);
		WorkMemberDao workmemberdao = sqlSession.getMapper(WorkMemberDao.class);
		MsgRoomDao msgroomdao = sqlSession.getMapper(MsgRoomDao.class);
		WorkSpace oldworkspace = workspacedao.selectOne(workid);
		if(workspace.getName().equals("")) {
			workspace.setName(oldworkspace.getName());
		}
		if(workspace.getOwneremail().equals("")) {
			workspace.setOwneremail(useremail);
		} else {
			user = userdao.selectEmail(workspace.getOwneremail());
			if(user == null) {
				workspace.setOwneremail(useremail);
			}
		}
		if(!inviteemail.equals("")) {
			user = userdao.selectEmail(inviteemail);
			if(user != null && !useremail.equals(inviteemail)) {
				HashMap<String, String> hashmap = new HashMap<String, String>();
				hashmap.put("workid", workid);
				hashmap.put("inviteemail", inviteemail);
				hashmap.put("userid", userid);
				WorkMember workmember = workmemberdao.selectMember(hashmap);
				if(workmember == null) {
					ArrayList<String> memberids = workmemberdao.selectAllId(workid);
					for (String memberid : memberids) {
						hashmap.put("memberid", memberid);
						msgroomdao.insertmul(hashmap);
					}
					workmemberdao.inviteMember(hashmap);
				}
			}
		}
		if(!withdrawpwd.equals("")) {
			String pwd = userdao.selectPwd(userid);
			if(BCrypt.checkpw(withdrawpwd, pwd)) {
				HashMap<String, String> hashmap = new HashMap<String, String>();
				hashmap.put("workid", workid);
				hashmap.put("userid", userid);
				workmemberdao.deleteWorkMember(hashmap);
				workspacedao.updateWorkOwnerid(workid);
				msgroomdao.deleteRoom(hashmap);
				
			}
		}
		if(!deletepwd.equals("")) {
			String pwd = userdao.selectPwd(userid);
			if(BCrypt.checkpw(deletepwd, pwd)) {
				int workcount = workmemberdao.selectCount(userid);
				if(workcount > 1) {
					ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
					workspacedao.deleteWork(workid);
					WorkSpace newworkspace = workspacedao.selectWorkNameId(userid);
					HashMap<String, String> hashmap = new HashMap<>();
					hashmap.put("workid", newworkspace.getWorkid());
					hashmap.put("userid", userid);
					Channel newchannel = channeldao.selectChanNameId(hashmap);
					hashmap.put("chanid", newchannel.getChanid());
					userdao.updateLastWork(hashmap);
					userdao.updateLastChannel(hashmap);
					session.setAttribute("sessionWorkid", newworkspace.getWorkid());
					session.setAttribute("sessionWorkname", newworkspace.getName());
					session.setAttribute("sessionChanid", newchannel.getChanid());
					session.setAttribute("sessionChanname", newchannel.getName());
					if(!userid.equals(newworkspace.getOwnerid())){
						session.setAttribute("sessionOwner", "N");
					} else {
						session.setAttribute("sessionOwner", "Y");
					}
					HashMap<String, String> newhashmap = new HashMap<>();
					newhashmap.put("workid", workid);
					newhashmap.put("userid", userid);
					msgroomdao.deleteRoom(newhashmap);
				}
			}
		}
		workspace.setWorkid(workid);
		workspacedao.updateWorkspaceNameOwnerid(workspace);
		msgroom.setUserid(userid);
		
		return "redirect:channel";
	}
	
	@RequestMapping(value="/updateWorkSpaceAjax", method = RequestMethod.POST)
	@ResponseBody
	public WorkSpace updateWorkSpaceAjax(@RequestParam String workid, HttpSession session) {
		String owneremail = (String) session.getAttribute("sessionEmail");
		WorkSpaceDao workspacedao = sqlSession.getMapper(WorkSpaceDao.class);
		WorkSpace workspace = workspacedao.selectOne(workid);
		workspace.setOwneremail(owneremail);
		return workspace;
	}
	
	@RequestMapping(value="/withdrawWorkspace", method = RequestMethod.POST)
	public String withdrawWorkspace(Model model, HttpSession session, @RequestParam String workid) {
		if (session.getAttribute("sessionUserid") == null){
			return "index";
		}
		String userid = (String) session.getAttribute("sessionUserid");
		WorkMemberDao workmemberdao = sqlSession.getMapper(WorkMemberDao.class);
		WorkSpaceDao workspacedao = sqlSession.getMapper(WorkSpaceDao.class);
		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		MsgRoomDao msgroomdao = sqlSession.getMapper(MsgRoomDao.class);
		int workcount = workmemberdao.selectCount(userid);
		if (workcount > 1) {
			HashMap<String, String> hashmap = new HashMap<String, String>();
			hashmap.put("workid", workid);
			hashmap.put("userid", userid);
			workmemberdao.deleteWorkMember(hashmap);
			msgroomdao.deleteRoom(hashmap);
		} else {
			return "redirect:channel";
		}
		WorkSpace workspace = workspacedao.selectWorkNameId(userid);
		
		HashMap<String, String> hashmapchan = new HashMap<>();
		hashmapchan.put("workid", workspace.getWorkid());
		hashmapchan.put("userid", userid);
		Channel channel = channeldao.selectChanNameId(hashmapchan);
		hashmapchan.put("chanid", channel.getChanid());
		userdao.updateLastWork(hashmapchan);
		userdao.updateLastChannel(hashmapchan);
		session.setAttribute("sessionWorkid", workspace.getWorkid());
		session.setAttribute("sessionWorkname", workspace.getName());
		session.setAttribute("sessionChanid", channel.getChanid());
		session.setAttribute("sessionChanname", channel.getName());
		return "redirect:channel";
	}
	
	@RequestMapping(value="/updateChannel", method = RequestMethod.POST)
	public String updateChannel(@ModelAttribute Channel channel, @RequestParam String inviteemail,
			@RequestParam String deletepwd, Model model, HttpSession session) {
		if (session.getAttribute("sessionUserid") == null){
			return "index";
		}
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		WorkMemberDao workmemberdao = sqlSession.getMapper(WorkMemberDao.class);
		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
		ChanMemberDao chanmemberdao = sqlSession.getMapper(ChanMemberDao.class);
		String userid = (String) session.getAttribute("sessionUserid");
		String workid = (String) session.getAttribute("sessionWorkid");
		String chanid = (String) session.getAttribute("sessionChanid");
		String useremail = (String) session.getAttribute("sessionEmail");
		Channel oldchannel = channeldao.selectOne(chanid);
		if(channel.getName().equals("")) {
			channel.setName(oldchannel.getName());
		}
		if(channel.getDescription().equals("")) {
			channel.setDescription(oldchannel.getDescription());
		}
		if(!inviteemail.equals("") && !inviteemail.equals(useremail)) {
			HashMap<String, String> hashmapworkmember = new HashMap<String, String>();
			hashmapworkmember.put("workid", workid);
			hashmapworkmember.put("inviteemail", inviteemail);
			WorkMember workmember = workmemberdao.selectMember(hashmapworkmember);
			if(workmember != null) {
				HashMap<String, String> hashmapchanmember = new HashMap<String, String>();
				hashmapchanmember.put("chanid", chanid);
				hashmapchanmember.put("inviteemail", inviteemail);
				ChanMember chanmember = chanmemberdao.selectMember(hashmapchanmember);
				if(chanmember == null) {
					chanmemberdao.inviteMember(hashmapchanmember);
				}
			}
		}
		if(!deletepwd.equals("")) {
			String pwd = userdao.selectPwd(userid);
			if(BCrypt.checkpw(deletepwd, pwd)) {
				HashMap<String, String> hashmap = new HashMap<String, String>();
				hashmap.put("workid", workid);
				hashmap.put("userid", userid);
				int chancount = chanmemberdao.selectCount(hashmap);
				if(chancount > 1) {
					channeldao.deleteChannel(chanid);
					Channel newchannel = channeldao.selectChanNameId(hashmap);
					hashmap.put("chanid", newchannel.getChanid());
					userdao.updateLastChannel(hashmap);
					session.setAttribute("sessionChanid", newchannel.getChanid());
					session.setAttribute("sessionChanname", newchannel.getName());
					return "redirect:channel";
				}
			}
		}
		channel.setChanid(chanid);
		channeldao.updateNameDescription(channel);
		session.setAttribute("sessionChanname", channel.getName());
		return "redirect:channel";
	}
	
	@RequestMapping(value="/withdrawChannel", method = RequestMethod.GET)
	public String withdrawChannel(Model model, HttpSession session) {
		if (session.getAttribute("sessionUserid") == null){
			return "index";
		}
		String userid = (String) session.getAttribute("sessionUserid");
		String chanid = (String) session.getAttribute("sessionChanid");
		String workid = (String) session.getAttribute("sessionWorkid");
		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
		ChanMemberDao chanmemberdao = sqlSession.getMapper(ChanMemberDao.class);
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("workid", workid);
		hashmap.put("userid", userid);
		int chancount = chanmemberdao.selectCount(hashmap);
		if (chancount > 1) {
			UserDao userdao = sqlSession.getMapper(UserDao.class);
			HashMap<String, String> hashmapmember = new HashMap<>();
			hashmapmember.put("chanid", chanid);
			hashmapmember.put("userid", userid);
			chanmemberdao.deleteMember(hashmapmember);
			Channel channel = channeldao.selectChanNameId(hashmap);
			hashmapmember.put("chanid", channel.getChanid());
			userdao.updateLastChannel(hashmapmember);
			session.setAttribute("sessionChanid", channel.getChanid());
			session.setAttribute("sessionChanname", channel.getName());
		}
		return "redirect:channel";
	}
	
	@RequestMapping(value="/updateAvata", method = RequestMethod.POST)
	public String updateAvata(Model model, HttpSession session, @RequestParam String avata) {
		String userid = (String) session.getAttribute("sessionUserid");
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		String[] srcsplit = avata.split("/");
		String src = "/" + srcsplit[3] + "/" +srcsplit[4] + "/" +srcsplit[5];
		HashMap<String, String> hashmap = new HashMap<>();
		hashmap.put("avata", src);
		hashmap.put("userid", userid);
		userdao.updateAvata(hashmap);
		session.setAttribute("sessionAvata", src);
		return "redirect:channel";
	}
	
	/* 사용자가 가입한 워크스페이스들 표시 */
	private Model callWorkList(Model model, String userid) {
		WorkSpaceDao workspacedao = sqlSession.getMapper(WorkSpaceDao.class);
		ArrayList<WorkSpace> workspaces = workspacedao.selectWorkspace(userid);
		for (WorkSpace workspace : workspaces) {
			String wsname = workspace.getName();
			String title = (wsname.length() > 2) ? workspace.getName().substring(0, 2) : wsname;
			workspace.setTitle(title);
			String owneremail = workspacedao.selectOwneremail(workspace.getWorkid());
			workspace.setOwneremail(owneremail);
		}
		return model.addAttribute("workspaces", workspaces);
	}

	/* 사용자가 가입하고 워크스페이스 내에 있는 채널들을 표시 */
	private void callChannelList(Model model, String userid) {
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		String workid = userdao.selectLastWork(userid);
		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
		ArrayList<Channel> channels = channeldao.selectChannel(userid, workid);
		model.addAttribute("channels", channels);
	}

	/* 이전에 보내고 받았던 채팅들을 받아서 채팅창에 뿌려줌, 채팅창에 '오늘' 표시 */
	private void callChatRoom(Model model, String userid) {
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		String chanid = userdao.selectLastChannel(userid);
		ChanChatDao chanchatdao = sqlSession.getMapper(ChanChatDao.class);
		ArrayList<ChanChat> chanchats = chanchatdao.selectMessage(chanid);
		//채팅창에 오늘 날짜와 비교하여 오늘로 표시
		SimpleDateFormat format1 = new SimpleDateFormat ( "MM월 dd일");
		Date time = new Date();		
		String today_mmdd = format1.format(time);
		model.addAttribute("chanchats", chanchats);
		model.addAttribute("today_mmdd", today_mmdd);
		return ;
	}

	/* 우측상단에 채팅방 타이블들 개시하고 채팅보내는 장소를 채팅방으로 설정 */
	private void callChannelChat(Model model, String userid) {
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		String chanid = userdao.selectLastChannel(userid);
		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
		String channelname = channeldao.selectName(chanid);
		// 마지막에 접속한 채널의 타이틀
		model.addAttribute("channelname", channelname);
		// 메시지를 보내는 장소를 채널방으로 설정
		model.addAttribute("channelNo", chanid);
		model.addAttribute("msgroomNo", null);
		return;
	}

	/* 사용자가 가입한 채널들과 개인 채팅 아이디들을 구독 */
	private void callWebsocket(Model model, String userid, String workid) {
		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
		MsgRoomDao msgroomdao = sqlSession.getMapper(MsgRoomDao.class);
		ArrayList<String> chanids = channeldao.selectChannelID(userid, workid);
		ArrayList<String> roomids = msgroomdao.selectRoomID(userid, workid);
		model.addAttribute("chanids", chanids);
		model.addAttribute("roomids", roomids);
		return ;
	}

	/* 암호화 */
	private String hashPassword(String plainTextPassword) {
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}

	/* 스크립트에 멤버 아이디, 이름 설정 + 채널 추가 모달에 유저정보를 넣어줌 */
	private void callWorkUser(Model model, String userid) {
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		String workid = userdao.selectLastWork(userid);
		System.out.println("--------> " + userid);
		ArrayList<User> users = userdao.selectWorkMember(workid, userid);
		ArrayList<User> members = userdao.selectMemberIDNamePhoto(workid);
		model.addAttribute("users", users);
		model.addAttribute("members", members);
	}
	
	/* 사용자의 비밀번호를 제외한 정보들 */
	private void callUser(Model model, String userid) {
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		User user = userdao.selectOne(userid);
		model.addAttribute("user", user);
	}
	
	/* 워크스페이스 수정 테이블에 보낼 정보 */
	private void callWorkspace(Model model, String workid) {
		WorkSpaceDao workspacedao = sqlSession.getMapper(WorkSpaceDao.class);
		WorkSpace workspace = workspacedao.selectTableWork(workid);
		model.addAttribute("workspace", workspace);
	}
	
	/* 채널 수정 테이블에 보낼 정보 */
	private void callChannel(Model model, String chanid) {
		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
		Channel channel = channeldao.selectOne(chanid);
		model.addAttribute("channel", channel);
	}
	
	private void callAvatas(Model model) {
		AvatasDao avatasdao = sqlSession.getMapper(AvatasDao.class);
		ArrayList<Avatas> avatas = avatasdao.selectAll();
		model.addAttribute("avatas", avatas);
	}
}