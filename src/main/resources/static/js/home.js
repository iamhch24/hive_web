function loginajax() {
	var email = $("#email").val();
	var pwd = $("#pwd").val();
	var dispaylogin = document.getElementById("dispaylogin");
	
	$.ajax({
		type: 'POST',
		data: { "email": email, "pwd": pwd },
		datatype: 'json',
		url: 'loginAjax',
		success: function(data) {
			if (data == "n") {
				dispaylogin.style.display = "block";
			} else {
				$("#loginform").attr("action", "loginUp");
				$("#loginform").submit();
			}
		},
		error: function(xhr, status, error) {
			alert('ajax error: ' + xhr.status + error);
		}
	});
}

function insertuserajax() {
	var email = $("#email").val();
	var pwd = $("#pwd").val();
	var pwd2 = $("#pwd2").val();
	var erroremail = document.getElementById("erroremail");
	var errorpwd = document.getElementById("errorpwd");
	var errorempty = document.getElementById("errorempty");
	$.ajax({
		type: 'POST',
		data: { "email": email, "pwd": pwd, "pwd2":pwd2 },
		datatype: 'json',
		url: 'insertUserAjax',
		success: function(data) {
			if(data == "e"){
				errorpwd.style.display = "none";
				errorempty.style.display = "none";
				erroremail.style.display = "block";
			}else if(data == "p"){
				errorempty.style.display = "none";
				erroremail.style.display = "none";
				errorpwd.style.display = "block";
			}else if(data == "n"){
				erroremail.style.display = "none";
				errorpwd.style.display = "none";
				errorempty.style.display = "block";
			}else{
				$("#inseruserform").attr("action", "insertUserSave");
				$("#inseruserform").submit();
			}
		},
		error: function(xhr, status, error) {
			alert('ajax error: ' + xhr.status + error);
		}
	});
	/*var dispayinsert = document.getElementById("dispayinsert");*/
}

$(document).ready(function(){

})