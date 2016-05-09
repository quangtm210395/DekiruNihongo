<?php
if (isset($_POST['data'])) {
	readfile($_POST['data']);
} else echo "Request is not valid";
?>