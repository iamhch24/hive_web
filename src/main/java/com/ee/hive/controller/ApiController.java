package com.ee.hive.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ee.hive.dao.ChanChatDao;
import com.ee.hive.dao.ChannelDao;
import com.ee.hive.dao.MsgRoomDao;
import com.ee.hive.dao.UserDao;
import com.ee.hive.dao.WorkSpaceDao;
import com.ee.hive.entities.ChanChat;
import com.ee.hive.entities.Channel;
import com.ee.hive.entities.MsgRoom;
import com.ee.hive.entities.ResponseData;
import com.ee.hive.entities.User;
import com.ee.hive.entities.WorkSpace;

@RestController
public class ApiController {

	@Autowired
	private SqlSession sqlSession;

	@GetMapping("/api")
	public String Home() {
		return "Welcome to Hive-API-Server";
	}

	@GetMapping("/api/users")
	public List<User> listUsers() {
		List<User> users = new ArrayList<>();
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		users = userDao.selectAll();
		return users;
	}

	@GetMapping("/api/workspaces")
	public List<WorkSpace> listWorkSpaces() {
		List<WorkSpace> workspaces = new ArrayList<>();
		WorkSpaceDao workSpaceDao = sqlSession.getMapper(WorkSpaceDao.class);
		workspaces = workSpaceDao.selectAll();

		return workspaces;
	}

	@GetMapping("/api/channels")
	public List<Channel> listChannels() {
		List<Channel> channels = new ArrayList<>();
		ChannelDao channelDao = sqlSession.getMapper(ChannelDao.class);
		channels = channelDao.selectAll();

		return channels;
	}

	@GetMapping("/api/msgrooms")
	public List<MsgRoom> listMsgrooms() {
		List<MsgRoom> msgrooms = new ArrayList<>();
		MsgRoomDao msgRoomDao = sqlSession.getMapper(MsgRoomDao.class);
		msgrooms = msgRoomDao.selectAll();

		return msgrooms;
	}

	@PostMapping("/app/login")
	public Map<String, String> user_login(@RequestParam String email, @RequestParam String pwd) {
		System.out.println("/app/login : email = " + email);

		String success = "";
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		User user = userdao.selectEmail(email);
		if (user == null) {
			success = "failed";
		} else {
			boolean passchk = BCrypt.checkpw(pwd, user.getPwd());
			if (passchk) {
				success = "success";
			} else {
				success = "failed";
			}
		}
		Map<String, String> result = new HashMap<>();

		if ("success".equals(success)) {

			String workid = user.getLastwork();
			WorkSpaceDao workdao = sqlSession.getMapper(WorkSpaceDao.class);
			String workname = workdao.selectWorkName(workid);
			String worktitle = (workname.length() > 2) ? workname.substring(0, 2) : workname;

			result.put("UserEmail", email);
			result.put("UserId", user.getUserid());
			result.put("UserName", user.getName());
			result.put("UserAvata", user.getPhoto());
			result.put("LastWorkId", workid);
			result.put("LastWorkName", workname);
			result.put("LastWorkTitle", worktitle);
			System.out.println("/app/login : userName = " + user.getName());
			System.out.println("/app/login : workName = " + workname);

		}

		result.put("isSuccess", success);

		return result;
	}

	// 아직 미완성
	@PostMapping("/app/register")
	public Map<String, String> user_register(@RequestParam String email, @RequestParam String pwd) {
		System.out.println("/app/register : email = " + email);
		Map<String, String> result = new HashMap<>();
		result.put("UserEmail", email);
		result.put("UserPwd", pwd);
		result.put("UserName", "admin");
		result.put("isSuccess", "success");

		return result;
	}

	@PostMapping("/app/workspaces")
	public ResponseData<WorkSpace> workspaces(@RequestParam String UserId) {

		System.out.println("/app/workspaces userid = " + UserId);

		if (UserId == null)
			return null;

		ResponseData<WorkSpace> result = new ResponseData<>();

		WorkSpaceDao workspacedao = sqlSession.getMapper(WorkSpaceDao.class);
		ArrayList<WorkSpace> workspaces = workspacedao.selectWorkspace(UserId);

		for (WorkSpace workspace : workspaces) {
			String wsname = workspace.getName();
			String title = (wsname.length() > 2) ? workspace.getName().substring(0, 2) : wsname;
			workspace.setTitle(title);
		}
		if (workspaces.size() > 0)
			result.setSuccess("success");
		else
			result.setSuccess("failed");

		result.setData(workspaces);

		System.out.println("/app/workspaces size = " + workspaces.size());
		return result;
	}

	@PostMapping("/app/channels")
	public ResponseData<Channel> channels(@RequestParam String UserId, @RequestParam String WorkId) {
		System.out.println("/app/channels userid = " + UserId);

		if (UserId == null)
			return null;

		ResponseData<Channel> result = new ResponseData<>();

		ChannelDao channeldao = sqlSession.getMapper(ChannelDao.class);
		ArrayList<Channel> channels = channeldao.selectChannel(UserId, WorkId);

		if (channels.size() > 0)
			result.setSuccess("success");
		else
			result.setSuccess("failed");
		result.setData(channels);

		System.out.println("/app/channels size = " + channels.size());
		return result;
	}

	@PostMapping("/app/workmembers")
	public ResponseData<User> workmembers(@RequestParam String UserId, @RequestParam String WorkId) {
		System.out.println("/app/workmembers userid = " + UserId);

		if (UserId == null)
			return null;

		ResponseData<User> result = new ResponseData<>();

		UserDao userdao = sqlSession.getMapper(UserDao.class);
		ArrayList<User> members = userdao.selectMemberIDNamePhoto(WorkId);

		if (members.size() > 0)
			result.setSuccess("success");
		else
			result.setSuccess("failed");
		result.setData(members);

		System.out.println("/app/workmembers size = " + members.size());
		return result;
	}

	@PostMapping("/app/chanchats")
	public ResponseData<ChanChat> chanchats(@RequestParam String UserId, @RequestParam String ChanId) {

		System.out.println("/app/chanchats userid = " + UserId + ", chanid = " + ChanId);

		if (UserId == null)
			return null;

		ResponseData<ChanChat> result = new ResponseData<>();

		ChanChatDao chanchatdao = sqlSession.getMapper(ChanChatDao.class);
		ArrayList<ChanChat> chanchats = chanchatdao.selectMessage(ChanId);

		if (chanchats.size() > 0)
			result.setSuccess("success");
		else
			result.setSuccess("failed");

		result.setData(chanchats);

		System.out.println("/app/chanchats size = " + chanchats.size());
		return result;
	}

	/* 암호화 */
	private String hashPassword(String plainTextPassword) {
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}
}
