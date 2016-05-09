<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
	<link rel="stylesheet" href="css/style_tf.css" />
	<link rel="stylesheet" href="resource/bootstrap-3.3.6-dist/css/bootstrap.css" />
    <script src="js/jquery.js"></script>
	<title>Dekiru Nihongo DB Supporter</title>
</head>
<body>
	<script>
	$('document').ready(function(e) {
		var key = [];
		var selected = $('ul').children('.selected');
		var selected_div = "#input1"
		$('li').click(function() {
			selected.removeClass("selected");
			$(selected_div).addClass("hidden");
			selected = $(this);
			selected_div = "#input" + (selected.index() + 1);
			selected.addClass("selected")
			$(selected_div).removeClass("hidden");
		});
	});
	</script>
	<div id="mpanel">
    	<div id="mmenu">
        	<ul style="list-style-type:none">
            	<li class="selected"x>Nhập</li>
                <li>Nhập từ Quizlet</li>
                <li>Nhập từ Word/Excel</li>
            </ul>
        </div>
        <div id="cpanel">
        	<div id="input1">
            	<script>
					$(document).ready(function(e) {
						var key = [];
						var keys = [];
						var value = [];
						var completed = 0;
						var dialog = document.querySelector('#dl');
						$('#add').click(function() {
							key.push('{"n":"' + $('#n').val() + '", "m":"' + $('#m').val() + '"}');
							$('#list').append($('<option>', {
								text: $('#n').val() + ": " + $('#m').val()
							}));
							$('#n').val("");
							$('#m').val("");
						});
						
						$('#done').click(function() {
							var s = '{"res": [';
							var i = 0;
							for (i = 0;i<key.length-1;i++) s += key[i] + ",";
							s += key[i] + "]}";
							$.post("cf.php", {
								r: "add",
								book: $('#book').val(),
								part: $('#part').val(),
								lesson: $('#lesson').val(),
								data: s
							}, function(res) {
								if (res == "success") {
									$('#dlpn').addClass("panel-success");
									dialog.showModal();	
									completed = 1;
								} else {
									$('#dlpn').addClass("panel-danger");
									$('#dlnoti').html("Import thất bai, lỗi: <br>" + res);
									dialog.showModal();	
								}
							});
						});
						
						$('#afq').click(function() {
							var url = $('#quizlet').val();
							$.post("loader.php",{data:url}, function(e) {
								$('.qWord', e).each(function() {
                                    keys.push($(this).html());
                                });
								$('.qDef', e).each(function() {
                                    value.push($(this).html());
                                });
								var i;
								for (i = 0;i < keys.length;i++) {
									key.push('{"n":"' + keys[i] + '", "m":"' + value[i] + '"}');
									$('#list').append($('<option>', {
										text: "<b>" + keys[i] + "</b>" + ": " + value[i]
									}));
								}
							});
						});

						$('#commit').click(function() {
							$.post("cf.php", {r:"commit"}, function(e) {
								$('#dlpn').addClass("panel-success");
								$('#dlnoti').html("Public data đã được cập nhât lên revision " + e);
								dialog.showModal();	
							});
						});
						
						$('#dlcl').click(function() {
							if (completed == 1) window.location.reload();
							else dialog.close();
						});
						
						$('#del').click(function() {
							var index = $('#list :selected').index();
							key.splice(index, 1);
							$('#list :selected').remove();
						});
					});
				</script>
				<input id="book" type="text" class="form-control" style="width:159px;margin: 30px;display:inline" placeholder="Quyển" /> 
				<input id="part" type="text" class="form-control" style="width:150px;margin: 30px;display:inline" placeholder="Phần" />
				<input id="lesson" type="text" class="form-control" style="width:150px;margin: 30px;display:inline" placeholder="Tên bài" />
                <input id="quizlet" type="text" class="form-control" style="width:500px;margin-top: 30px;margin-left:30px;display:inline" placeholder="Paste link quizlet vào đây" />
                <button id="afq" class="btn btn-primary" style="margin-left:10px;width:75px;">Thêm</button><br>
				<input id="n" type="text" class="form-control" style="width:259px;margin-top: 30px;margin-left:30px;display:inline" placeholder="Từ/Cáu trúc" />
				<select id="list" size="10" style="float:right;width:280px;margin-top:25px;margin-right:40px;overflow:scroll">
				</select><br>
				<textarea id="m" type="text" class="form-control" style="width:259px;margin-top: 30px;margin-left:30px;display:inline" placeholder="Nghĩa/Giải nghĩa" ></textarea><br>
				<button id="add" class="btn btn-primary" style="margin-top: 15px;margin-left:30px;width:100px;">Thêm</button>
				<button id="del" class="btn btn-primary" style="margin-top: 15px;margin-left:30px;width:100px;">Xóa</button><br> 
				<button id="done" class="btn btn-primary" style="margin-top: 20px;margin-left:30px;width:100px;">Xong</button>
				<button id="commit" class="btn btn-primary" style="margin-top: 20px;margin-left:30px;width:100px;">Update data</button><br>
			</div>
            <div id="input2" class="hidden"></div>
            <div id="input3" class="hidden"></div>
        </div>
    </div>
    <dialog id="dl">
    	<div class="panel-group" style="margin:0">
        	<div id="dlpn" class="panel" style="border-radius: 25px;">
            	<div class="panel-heading" style="border-top-left-radius: 25px;border-top-right-radius:25px">Thông báo</div>
                <div class="panel-body">
                	<span id="dlnoti">Import dữ liệu thành công</span><br>
                    <button id="dlcl" class="btn btn-primary" style="float:right;margin:5px">OK</button>
                </div>
            </div>
        </div>
    </dialog>
</body>
</html>