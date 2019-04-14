<?php

$connection = mysqli_connect("160.153.75.104","aats_admin","aats_admin123");

if (mysqli_connect_errno())
  {
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

mysqli_select_db($connection,"AATS_Database");


 $sql = "SELECT DISTINCT classID from class_of_professor_in_time where professorID = 4 AND day = 'Tuesday' AND time ='1540-1630'";

 $result = mysqli_query($connection, $sql);
 $output = array();

 if (mysqli_num_rows($result) > 0) {
    while($row = mysqli_fetch_assoc($result)) {
      $output[] = $row;
    }
     print(json_encode($output));
 } else {
    echo "null";
 }

// print [0] element with key name classID
  // $courseName = $output[0]["classID"];
  // echo  $courseName






     mysqli_close($connection);
?>