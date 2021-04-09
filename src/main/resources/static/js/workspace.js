function appendchannelmodal() {
	$('.appendchannelmodal').modal('show');
}

function appendchannelmodalcancel() {
	$('.appendchannelmodal').modal('hide');
}


function appendmsgroommodal() {
	$('.appendmsgroommodal').modal('show');
}

function appendmsgroommodalcancel() {
	$('.appendmsgroommodal').modal('hide');
}

function updatemembermodal() {
	$('.updatemembermodal').modal('show');
}

function updateMemberCancelBtn() {
	$('.updatemembermodal').modal('hide');
}

function updateMemberBtn() {
	var pwd1 = $('#pwd1').val();
	var pwd2 = $('#pwd2').val();
	if (pwd1 == pwd2) {
		$('#updateUser').attr('action', 'updateUser');
		$('#updateUser').submit();
	} else {
		$('.notsamepassword').modal('show');
	}
}

function updateworkspacemodal(item) {
	if ((event.button == 2) || (event.which == 3)) {
		$.ajax({
			type: "POST",
			data: { "workid": item },
			datatype: "json",
			url: "updateWorkSpaceAjax",
			success: function(data) {
				$('#name').val(data.name);
				$('#owneremail').val(data.owneremail);
				$('#workid').val(data.workid);
				$('.updateworkspacemodal').modal('show');
			},
			error: function(xhr, status, error) {
				alert('ajax error: ' + xhr.status + error);
			}
		});

	}
}

function updateWorkSpaceCancelBtn() {
	$('.updateworkspacemodal').modal('hide');
}

function withdrawworkspace() {
	$('.withdrawworkspacemodal').modal('show');
	$(".contextmenu").hide();	
}

function withdrawWorkCancelBtn() {
	$('.withdrawworkspacemodal').modal('hide');
}

function updatechannelmodal() {
	$('.updatechannelmodal').modal('show');
}

function updateChannelCancelBtn() {
	$('.updatechannelmodal').modal('hide');
}

function withdrawchannel() {
	$('.withdrawchannelmodal').modal('show');
}

function withdrawChanCancelBtn() {
	$('.withdrawchannelmodal').modal('hide');
}

function viewphoto() {
	$('.avatasmodal').modal('show');
}

function viewphotocancel() {
	$('.avatasmodal').modal('hide');
}

function selectAvata(value) {
	var src = value.src;
	document.querySelectorAll('.avata.image').forEach(item => {
		item.classList.remove('avata-box');
	});
	value.classList.toggle('avata-box');
	$('#avata').val(src);
}


function popupcontextmenu(item) {
	if ((event.button == 2) || (event.which == 3)) {
		//Show contextmenu:
		$(".worklistBtn.ownerN").contextmenu(function(e) {
			//Get window size:
			var winWidth = $(document).width();
			var winHeight = $(document).height();
			//Get pointer position:
			var posX = e.pageX;
			var posY = e.pageY;
			//Get contextmenu size:
			var menuWidth = $(".contextmenu").width();
			var menuHeight = $(".contextmenu").height();
			//Security margin:
			var secMargin = 10;
			//Prevent page overflow:
			if (posX + menuWidth + secMargin >= winWidth
				&& posY + menuHeight + secMargin >= winHeight) {
				//Case 1: right-bottom overflow:
				posLeft = posX - menuWidth - secMargin + "px";
				posTop = posY - menuHeight - secMargin + "px";
			}
			else if (posX + menuWidth + secMargin >= winWidth) {
				//Case 2: right overflow:
				posLeft = posX - menuWidth - secMargin + "px";
				posTop = posY + secMargin + "px";
			}
			else if (posY + menuHeight + secMargin >= winHeight) {
				//Case 3: bottom overflow:
				posLeft = posX + secMargin + "px";
				posTop = posY - menuHeight - secMargin + "px";
			}
			else {
				//Case 4: default values:
				posLeft = posX + secMargin + 13 + "px";
				posTop = posY + secMargin - 55 + "px";
			};
			//Display contextmenu:
			$(".contextmenu").css({
				"left": posLeft,
				"top": posTop
			}).show();
			//Prevent browser default contextmenu.
			return false;
		});
		//Hide contextmenu:
		$("#withdrawworkid").val(item);
	}
}

$(document).on('click',function(event){
	if( $(".contextmenu") !== event.target ){
		$(".contextmenu").hide();
	}
});

/*우클릭 기본기능 중지*/
document.addEventListener('contextmenu', function() {
	event.preventDefault();
});

/*$.ajax({
	type: 'POST',
	data: { "src": src },
	datatype: 'json',
	url: 'updateAvata',
	success: function(data) {
		$('.avatasmodal').modal('show');
	},
	error: function(xhr, status, error) {
		alert('ajax error: ' + xhr.status + error);
	}
});*/




/*function numberMaxLength(e){
	 if(e.value.length > e.maxLength){
			e.value = e.value.slice(0, e.maxLength);
		}
}*/
/*function appendchannelbtn(){
	var channelname = $('#channelname').val();
	$("input[name='checkbox']:checked").each(function(i) {
		var checkArr = [];
		var channelid = $(this).val();	
		checkArr.push(channelid);       // 체크된 것만 값을 뽑아서 배열에 push	    
		  });
	$.ajax({
		type: 'POST',
		contentType : 'application/json; charset=UTF-8',
		data: { "channelname" : channelname, "checkArr" : checkArr },
		datatype: 'json',
		url: 'appendChannelAjax',
		success: function(data) {
			$('.appendchannelmodal').modal('hide');
		},
		error: function(xhr, status, error) {
			alert('ajax error: ' + xhr.status + error);
		}
	});
}*/

$(document).ready(function() {
	$('.ui.menu a.item').on('click', function() {
		$(this).addClass('active').siblings().removeClass('active');
	});

	$('.listName').click(function() {
		if ($(".display").is(":visible")) {
			$('.listName').find($(".down")).removeClass('caret down icon').addClass('caret right icon');
			$(".display").slideUp();
		}
		else {
			$('.listName').find($(".down")).removeClass('caret right icon').addClass('caret down icon');
			$(".display").slideDown();
		}
	});

	$('#viewphoto').on('click', function() {
		$('input[type=file]').click();
		return false;
	});

	$('#imgfile').on('change', function(event) {
		var imgpath = URL.createObjectURL(event.target.files[0]);
		$('#viewphoto').attr('src', imgpath);
	});



	/*버튼 활성화 = 페이지 넘김으로 해*/
	/*$('.worklistBtn').each(function(index){
	$(this).attr('btn-index', index);
	}).click(function(){
		var index = $(this).attr('btn-index');
		$('.worklistBtn[btn-index=' + index + ']').addClass('clicked_worklistBtn');
		$('.worklistBtn[btn-index!=' + index + ']').removeClass('clicked_worklistBtn');
	});*/

	/*$('.login').on('click', function() {
		$('.ui.modal.loginModal').modal('show');
		
		$('#loginCancelBtn').on('click', function() {
			$('.ui.modal.loginModal').modal('hide');	
			return;
		});

		$('#loginBtn').on('click', function() {
			var email = $('#email').val();
			var pwd = $('#pwd').val();
		
			$.ajax({
				type:'POST',
				data : {email:email, pwd:pwd},
				datatype:'json',
				url : 'login',	
				success : function(){
					location.reload();
				},
				error : function(xhr,  error){
					alert('ajax error: ' + xhr.status + error);
				}
			});
		});
	});*/


});