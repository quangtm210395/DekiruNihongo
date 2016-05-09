<?php
error_reporting(E_ALL ^ E_DEPRECATED);
include("config_db.php");
$con = mysql_connect($host, $user, $pass) or die ("Không kết nối được đến database");
mysql_select_db($db,$con);

if(isset($_GET['r'])) {
	if ($_GET['r'] == "getListContent") {
		$book = $_GET['book'];
		$part = $_GET['part'];
		$lesson = $_GET['lesson'];
		$s = '{"res":[';
		$res = mysql_query("Select l".$lesson." from ".$part.$book);
		while ($row = mysql_fetch_array($res)) {
			if ($row[0] != null) $s .= $row[0].",";
			else break;
		}
		$s[strlen($s)-1] = "]";
		$s .= "}";
		echo $s;
	} else if ($_GET['r'] == "getListLesson") {
		$book = $_GET['book'];
		$res = mysql_query("select b".$book." from list1");
		$s = '{"res":[';
		while ($row = mysql_fetch_array($res)) $s .= '{"n":"'. $row[0] . '"},';
		$s[strlen($s)-1] = "]";
		$s .= "}";
		echo $s;
	} else if ($_GET['r'] == "getrev") {
		$rev = mysql_fetch_array(mysql_query("select rev from info"));
		echo ($rev[0]);
	} else if ($_GET['r']=="getAll") {
		//$s = mysql_fetch_array(mysql_query("select dat from info"));
		//echo $s[0];
		$file = fopen("pubdata.txt", "r+") or die ("Khppng thể mở file để lấy dữ liệu");
		echo file_get_contents("pubdata.txt");
		fclose($file);
	} 
} else if (isset($_POST['r'])) {
	if ($_POST['r'] == "add") {
		$book = $_POST['book'];
		$part = $_POST['part'];
		$lname = $_POST['lesson'];
		$data = $_POST['data'];
		if (($book == "")||($part == "")||($lname == "")||($data== "")) die ("Not enough data");
		$data = json_decode($data, true);
		$data = $data['res'];
		$query = mysql_query("select * from ".$part.$book);
		$max_row = mysql_num_rows($query)-1;
		$ls = mysql_num_fields($query);
		if ($ls > mysql_num_fields($query)-1) {
			mysql_query("alter table ".$part.$book." add column (l".$ls." text)");
			for ($i = 0; $i <= count($data)-1;$i++) {
				$s = '{"n":"'.$data[$i]['n'].'","m":"'.$data[$i]['m'].'"}';
				if ($i <= $max_row) $q = "update ".$part.$book." set l".$ls."='".$s."' where num=".($i+1);
				else $q = "insert into ".$part.$book." (l".$ls.") values ('".$s."')";
				mysql_query($q);
			}
			if ($part == "vocab") $partnum = 1;
			else if ($part == "gra") $partnum = 2;
			else if ($part == "quiz") $partnum = 3;
			else $partnum = 4; 
			$query = mysql_query("select * from list".$partnum);
			$max_row = mysql_num_rows($query);
			if ($ls <= $max_row) $q = "update list".$partnum." set b".$book."='".$lname."' where num=".($ls);
			else $q = "insert into list".$partnum." (b".$book.") values ('".$lname."')";
			mysql_query($q);
		}
		echo "success";
	} else if ($_POST['r'] == "commit") {
		commitChange();
	}
} else echo "No request found";

function getTable($table) {
	$query = mysql_query("select * from ".$table);
	$max_col = mysql_num_fields($query) - 1;
	$s = '"'.$table.'":{ ';
	$num = 0;
	for ($i = 1; $i <= $max_col;$i++) {
		$s .= '"l'.$i.'":[ ';
		$query = mysql_query("select l".$i." from ".$table);
		while ($row = mysql_fetch_array($query)) if ($row[0] != null) $s .= $row[0] .',';
		$s[strlen($s)-1] = "]";
		$s .= ",";
	} 
	$s[strlen($s)-1] = "}";
	return $s;
}

function getListLesson($table) {
	$query = mysql_query("select * from ".$table);
	$max_col = mysql_num_fields($query) - 1;
	$s = '"'.$table.'":{ ';
	$num = 0;
	for ($i = 1; $i <= $max_col;$i++) {
		$s .= '"b'.$i.'" :[ ';
		$query = mysql_query("select b".$i." from ".$table);
		while ($row = mysql_fetch_array($query)) if ($row[0] != null) $s .= '"'.$row[0] .'",';
		$s[strlen($s)-1] = "]";
		$s .= ",";
	}
	$s[strlen($s)-1] = "}";
	return $s;
}

function commitChange() {
	$data = '{"data": {';
	$data .= getTable("vocab1") .',';
	$data .= getTable("vocab2") .',';
	$data .= getTable("vocab3") .',';
	$data .= getTable("gra1") .',';
	$data .= getTable("gra2") .',';
	$data .= getTable("gra3") .',';
	$data .= getTable("quiz1") .',';
	$data .= getTable("quiz2") .',';
	$data .= getTable("quiz3") .',';
	$data .= getTable("kan1") .',';
	$data .= getTable("kan2") .',';
	$data .= getTable("kan3") .',';
	$data .= getListLesson("list1") .',';
	$data .= getListLesson("list2") .',';
	$data .= getListLesson("list3") .',';
	$data .= getListLesson("list4");
	$data .= "}}";
	$rev = mysql_fetch_array(mysql_query("select rev from info"));
	$rev = $rev[0];
	$file = fopen("pubdata.txt", "w+") or die ("Không thể mở file để ghi dữ liệu");
	$data = "\xEF\xBB\xBF" . $data;
	file_put_contents("pubdata.txt", $data);
	fclose($file);
	mysql_query("update info set rev=".++$rev." where num=1");
	echo $rev;
}
?>