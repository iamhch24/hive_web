'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var contentLayout = document.querySelector('.contentLayout');

var stompClient = null;	// 스톰프 클라이언트
var socket = null;		// 웹소켓 
var username = null;	// 현 사용자 이름을 셋팅한다.
var userid = null;		// 현 사용자 id를 셋팅한다.
var chanids = [];		// 채널 리스트 정보
var roomids = [];		// 룸 리스트 정보
var memberids = [];		// 워크 멤버 정보
var membernames = [];	// 워크 멤버 정보
var channelNo = null;	// 채널 선택시 채널 id를 셋팅한다.
var msgroomNo = null;	// 메시지룸을 선택시 룸 id를 셋팅한다.
var otherid = null; 	// 메시지룸을 선택시 대상 멤버의 id를 셋팅한다.
let unreadmsgbox = null;

var colors = [
	'#2196F3', '#32c787', '#00BCD4', '#ff5652',
	'#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect() {
	if(socket == null && username != null){
	socket = new SockJS('/hive');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, onConnected, onError);
	}
/*	alert("username -----> " + username);
	alert("roomNo-----> " + roomNo);
	alert("chanids-----> " + chanids);
	alert("roomids-----> " + roomids);
	alert("channelNo-----> " + channelNo);
	alert("msgroomNo-----> " + msgroomNo);*/
}


function onConnected() {
	// Subscribe to the Public Topic
	chanids.forEach(function(chanid) {
		stompClient.subscribe("/topic/channel/" + chanid, onMessageReceived);
	/*	console.log("onConnected:/topic/channel/" + chanid);*/
	});
	roomids.forEach(function(roomid) {
		stompClient.subscribe("/topic/msgroom/" + roomid, onMessageReceived);
	/*	console.log("onConnected:/topic/msgroom/" + roomid);*/
	});
	// Tell your username to the server
	/*connectingElement.classList.add('hidden');*/
}


function onError(error) {
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
}

function sendMessage(event) {
	if (channelNo) {
		var messageContent = messageInput.value.trim();
		if (messageContent && stompClient) {
			var chatMessage = {
				sender: username,
				content: messageInput.value,
				type: 'CHAT',
				chanid: channelNo,
				userid: userid,
				roomseq: null,
				otherid: null
			};
			stompClient.send("/app/channel.sendMessage/" + channelNo, {}, JSON.stringify(chatMessage));
			messageInput.value = '';
			messageInput.focus();
		}
	}
	if (msgroomNo) {
		var messageContent = messageInput.value.trim();
		if (messageContent && stompClient) {
			var chatMessage = {
				sender: username,
				content: messageInput.value,
				type: 'CHAT',
				chanid: null,
				userid: userid,
				roomseq: msgroomNo,
				otherid: otherid
			};
			stompClient.send("/app/msgroom.sendMessage/" + msgroomNo, {}, JSON.stringify(chatMessage));
			messageInput.value = '';
			messageInput.focus();
		}
	}
	event.preventDefault();
}


function onMessageReceived(payload) {
	
	var message = JSON.parse(payload.body);
	var messageElement = document.createElement('li');

	console.log(message);
	
	if ( message.chanid !== null && message.roomseq !== null){
		return;
	}
	
	if((channelNo !== null && message.chanid === channelNo) || (msgroomNo !== null && message.roomseq === msgroomNo ) ){
  
		console.log(message.chanid);
		console.log(channelNo);
		if (message.type === 'JOIN') {
			messageElement.classList.add('event-message');
			message.content = message.sender + ' joined!';
			var textElement = document.createElement('p');
			var messageText = document.createTextNode(message.content);
			textElement.appendChild(messageText);
	
			messageElement.appendChild(textElement);
	
			messageArea.appendChild(messageElement);
			contentLayout.scrollTop = contentLayout.scrollHeight;					

		} else if (message.type === 'LEAVE') {
			messageElement.classList.add('event-message');
			message.content = message.sender + ' left!';
			var textElement = document.createElement('p');
			var messageText = document.createTextNode(message.content);
			textElement.appendChild(messageText);
	
			messageElement.appendChild(textElement);
	
			messageArea.appendChild(messageElement);
			contentLayout.scrollTop = contentLayout.scrollHeight;					
				
		} 
		else {
			let html_str="";	
			let header_days = document.querySelectorAll('.header-day');
			let isToday = [...header_days].filter(e => e.innerText === '오늘');
			let avata_src = [...members].filter(e=>e.userid === message.userid);
			let d = new Date();
			let currentHour = ( d.getHours() ) < 12 ? `AM ${d.getHours()}` : d.getHours() > 12 ? `PM ${d.getHours()-12}` : `PM 12`;
			let currentMin = d.getMinutes();
			let today_time = `${currentHour}시 ${currentMin}분`;

			if(!(isToday.length>0)){
				var div = document.createElement('div');
				messageArea.appendChild(div);
				div.outerHTML = `<div class="header-day" style="text-align:center;background-color:#23272a;border-radius:10px">오늘</div>`;
			}
			var div2 = document.createElement('div');
			messageArea.appendChild(div2);
			var right = (userid===message.userid)? "right" : "left";
			
			html_str = `<div class="item chlist ${right}">`;
			html_str +=`<img class="ui avatar image ${right} floated" src="${avata_src[0].photo}" style="color:white;background:gray;text-align:center;border-radius:20%;width:50px;height:50px;">`;
		    html_str +=`<div class="content">`;
		    html_str +=`<div class="header" style="color:white;font-size:12px;font-weight:500;line-height:2">${message.sender} ${today_time}</div>`;
			html_str +=`<div class="description">${message.content}</div>`;
			html_str +="</div></div>";
			div2.outerHTML = html_str;
			contentLayout.scrollTop = contentLayout.scrollHeight;	
			
			var id = (channelNo !== null) ? channelNo : msgroomNo;
			resetunreadmsg(id);
			showunreadmsg();
		}
	}
	else{
		var id = (message.chanid !== null) ? message.chanid : message.roomseq;	
		addunreadmsg(id);
		showunreadmsg();
	}
}

function addunreadmsg(id){
	if(localStorage.getItem(id) ===null){
		localStorage.setItem(id,0);
	}
	var value = localStorage.getItem(id);
	localStorage.setItem(id,parseInt(value)+1);	
}

function resetunreadmsg(id){
	localStorage.setItem(id,0);
}

function showunreadmsg(){
//	alert("chanids-length:"+chanids.length);
	chanids.forEach(id=>{
		var cnt =  parseInt(localStorage.getItem(id));
		var target = document.querySelectorAll(`#channel-names a li label[data-id='${id}']`); 
		if(cnt>0){
			target[0].textContent=cnt;
			target[0].style.opacity="0.7";
		}
		else{
			target[0].style.opacity="0.0";
		}
	});
	
/*	roomids.forEach(id=>unreadmsgbox.set(id,0));*/	
	
}

function getAvatarColor(messageSender) {
	var hash = 0;
	for (var i = 0; i < messageSender.length; i++) {
		hash = 31 * hash + messageSender.charCodeAt(i);
	}
	var index = Math.abs(hash % colors.length);
	return colors[index];
}

messageForm.addEventListener('submit', sendMessage, true);