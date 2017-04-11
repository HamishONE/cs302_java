<?php
$con=mysqli_connect("localhost", "DB_User", "CS302", "UNI");
	if(!$con) {
		die ('Could Not Connect:' .mysqli_error());
	}
?>