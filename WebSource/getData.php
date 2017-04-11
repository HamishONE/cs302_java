<?php
include 'dbcon.php';

$result = mysqli_query($con, "SELECT TOP 10 FROM CS302_Highscores ORDER BY Score");

while ($row = mysqli_fetch_array($result)) {
                echo $row['Name'];
                echo '|';
                echo $row['Score'];
                echo '|';
              }

mysqli_close($con);
?>