<?php

$connection = mysqli_connect("160.153.75.104","aats_admin","aats_admin123");

if (mysqli_connect_errno())
  {
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

mysqli_select_db($connection,"AATS_Database");

$professorID = (int)$_REQUEST['professor_id'];
 $sql = 'SELECT DISTINCT classID FROM professor_course WHERE professorID = $professorID ;';
     $result = mysqli_query($conn, $sql);
     $output = array();
     if (mysqli_num_rows($result) > 0) {
        while($row = mysqli_fetch_assoc($result)) {
          $output[] = $row;
        }
        print(json_encode($output));
     } else {
        echo "null";
     }
     mysqli_close($conn);
?>