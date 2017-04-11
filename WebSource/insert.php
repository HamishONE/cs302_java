<?php
include 'dbcon.php';

$name = $_GET["name"];
$score = $_GET["score"];

$sql = "INSERT INTO `CS302_Highscores` (`Name`, `Score`) VALUES ('$name', '$score')";

if ($con->query($sql) === TRUE) {
    echo "Success";
} else {
    echo "Error: " . $sql . $con->error;
}
mysqli_close($con);
?>