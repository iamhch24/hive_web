package com.ee.hive.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ee.hive.dao.DocumentDao;
import com.ee.hive.dao.UserDao;
import com.ee.hive.dao.WorkSpaceDao;
import com.ee.hive.entities.Document;
import com.ee.hive.entities.DocumentRollUp;
import com.ee.hive.entities.User;
import com.ee.hive.entities.WorkSpace;

@Controller
public class DocumentController {

	@Autowired
	private SqlSession sqlSession;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(Model model, HttpSession session) {
		return "test";
	}

	@RequestMapping(value = "/document", method = RequestMethod.GET)
	public String document(Model model, HttpSession session, @RequestParam(required = false) String cmd) {
		String userid = (String) session.getAttribute("sessionUserid");
		String workid = (String) session.getAttribute("sessionWorkid");
		/* 워크스페이스 리스트 */
		callWorkList(model, userid);
//		/* 채널 리스트 */
//		callChannelList(model, userid);
//		/* 채널 내용 */
//		callChatRoom(model, userid);
//		/* 채팅보내는 장소를 채널룸으로 설정, 채팅창 상단에 채널 타이틀 표시 */
//		callChannelChat(model, userid);
//		/* 채팅 설정 스크립트 */
//		callWebsocket(model, userid, workid);
		/* 사용자가 최근에 방문한 워크스페이스의 멤버들을 뿌려줌 채널추가 테이블에 */

		callWorkUser(model, userid);
		if (cmd == null)
			callDocumentListFromMe(model, userid, workid);
		else if (cmd.equals("ToMe")) {
			callDocumentListToMe(model, userid, workid);
		} else {
			callDocumentListFromMe(model, userid, workid);
		}

		SimpleDateFormat format1 = new SimpleDateFormat("MM월 dd일");
		Date time = new Date();
		String today_mmdd = format1.format(time);
		model.addAttribute("today_mmdd", today_mmdd);
		model.addAttribute("cmd", cmd);
		return "document/document";
	}

	@RequestMapping(value = "/saveDocument", method = RequestMethod.POST)
	public String saveDocument(Model model, HttpSession session, @ModelAttribute Document docu,
			@RequestParam(value = "b_attachfile", required = false) MultipartFile b_attachfile,
			@RequestParam(value = "b_attachname", required = false) String b_attachname) throws Exception {

		System.out.println(b_attachname);
		if (b_attachfile != null) {

			String filename = b_attachfile.getOriginalFilename();
			/*
			 * String path =
			 * "C:\\Users\\IT-5C\\Downloads\\hive0402_0000\\hive\\src\\main\\resources\\static\\uploadattaches\\";
			 */
			String realpath = "uploadattaches/"; // static 아래에 폴더 생성
//			String realpath = "/uploadattaches/"; // static 아래에 폴더 생성			

			System.out.println(filename);
			System.out.println(docu.getFilepath());

			if (!docu.getFilepath().equals(realpath + filename)) {
				if (!filename.equals("")) {
					byte bytes[] = b_attachfile.getBytes();
					try {
						BufferedOutputStream output = new BufferedOutputStream(
								new FileOutputStream(realpath + filename));
						output.write(bytes);
						output.flush();
						output.close();
						docu.setFilepath(realpath + filename);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}

		String cmd = docu.getCmd();
		DocumentDao docudao = sqlSession.getMapper(DocumentDao.class); // 세션에 매핑
		if (docu.getMode().equals("add"))
			docudao.insertRow(docu);
		else if (docu.getMode().equals("update"))
			docudao.updateRow(docu);
		return "redirect:/document?cmd=" + cmd;
	}

	@RequestMapping(value = "/deleteDocument", method = RequestMethod.GET)
	public String deleteDocument(Model model, HttpSession session, @RequestParam String seq) {
		System.out.println(seq);
		DocumentDao docudao = sqlSession.getMapper(DocumentDao.class); // 세션에 매핑
		docudao.deleteRow(seq);
		return "redirect:/document";
	}
	
	@RequestMapping(value = "/fileDownload")
	@ResponseBody
	public void fileDownload(@RequestParam String b_attach, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setCharacterEncoding("UTF-8");
		File file = new File(b_attach);
//		File file = new File("C:\\Users\\IT-5C\\Downloads\\hive0402_0000\\hive\\src\\main\\resources\\static" + b_attach);

		String oriFileName = file.getName();

		String filePath = "uploadattaches/";
//		String filePath = "C:\\Users\\IT-5C\\Downloads\\hive0402_0000\\hive\\src\\main\\resources\\static\\uploadattaches\\";		
		InputStream in = null;
		OutputStream os = null;
		File newfile = null;
		boolean skip = false;
		String client = "";
		// 파일을 읽어 스트림에 담기
		try {
			newfile = new File(filePath, oriFileName);
			in = new FileInputStream(newfile);
		} catch (FileNotFoundException fe) {
			skip = true;
		}
		client = request.getHeader("User-Agent");
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Description", "HTML Generated Data");
		if (!skip) {
			// IE
			if (client.indexOf("MSIE") != -1) {
				response.setHeader("Content-Disposition", "attachment; filename=\""
						+ java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
				// IE 11 이상.
			} else if (client.indexOf("Trident") != -1) {
				response.setHeader("Content-Disposition", "attachment; filename=\""
						+ java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
			} else {
				// 한글 파일명 처리
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + new String(oriFileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
				response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
			}
			response.setHeader("Content-Length", "" + file.length());
			os = response.getOutputStream();
			byte b[] = new byte[(int) file.length()];
			int leng = 0;
			while ((leng = in.read(b)) > 0) {
				os.write(b, 0, leng);
			}
		} else {
			response.setContentType("text/html;charset=UTF-8");
			System.out.println("<script language='javascript'>alert('파일을 찾을 수 없습니다');history.back();</script>");
			System.out.println("skip...........");
		}
		in.close();
		os.close();
	}

	private Model callWorkList(Model model, String userid) {
		WorkSpaceDao workspacedao = sqlSession.getMapper(WorkSpaceDao.class);
		ArrayList<WorkSpace> workspaces = workspacedao.selectWorkspace(userid);
		for (WorkSpace workspace : workspaces) {
			String wsname = workspace.getName();
			String title = (wsname.length() > 2) ? workspace.getName().substring(0, 2) : wsname;
			workspace.setTitle(title);
		}
		return model.addAttribute("workspaces", workspaces);
	}

	private void callWorkUser(Model model, String userid) {
		UserDao userdao = sqlSession.getMapper(UserDao.class);
		String workid = userdao.selectLastWork(userid);
		ArrayList<User> users = userdao.selectWorkMember(workid, userid);
		ArrayList<User> members = userdao.selectMemberIDNamePhoto(workid);
		model.addAttribute("users", users);
		model.addAttribute("members", members);
	}

	private void callDocumentListFromMe(Model model, String userid, String workid) {
		System.out.println("callFromMe : userid=" + userid + ", workid=" + workid);
		DocumentDao docudao = sqlSession.getMapper(DocumentDao.class); // 세션에 매핑
		ArrayList<Document> documents = docudao.selectFromMe(userid, workid);
		model.addAttribute("documents", documents);
		DocumentRollUp docurollup = docudao.rollupFromMe(userid, workid);
		model.addAttribute("docurollup", docurollup);
	}


	private void callDocumentListToMe(Model model, String userid, String workid) {
		System.out.println("callToMe : userid=" + userid + ", workid=" + workid);
		DocumentDao docudao = sqlSession.getMapper(DocumentDao.class); // 세션에 매핑
		ArrayList<Document> documents = docudao.selectToMe(userid, workid);
		model.addAttribute("documents", documents);
		DocumentRollUp docurollup = docudao.rollupToMe(userid, workid);
		model.addAttribute("docurollup", docurollup);
	}
}
