<?php
if (isset($argc)) {
  for ($i = 0; $i < $argc; $i++) {
    if($i == 1){
      $user_ID = (int)$argv[$i];
    }else if($i == 2 ){
      $user_password =  $argv[$i];
    }
  }
}

include "convert_functions.php";
$connection = mysqli_connect("160.153.75.104","aats_admin","aats_admin123");

if (mysqli_connect_errno())
  {
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

mysqli_select_db($connection,"AATS_Database");
  
    // $ID = (string)$_REQUEST['ID'];
    // $password = $_REQUEST['password'];
    $professorID = $user_ID;


    $currentTime = getCLassTime();
    $currentTime = "1540-1630";
    $currentDayOfWeek = getDayOfWeek(); // uncomment for real demo phase
    $currentDayOfWeek = "Tuesday"; // hardcode according to db temporarily

    // echo $currentDayOfWeek;
    // echo $professorID;

  // if($auth_response  == "AUTH_SUCCESS"){
       $sql = "SELECT DISTINCT classID from class_of_professor_in_time where professorID = $professorID AND day = '$currentDayOfWeek' AND time ='$currentTime'";
       $result = mysqli_query($connection, $sql);
       $output = array();
       if (mysqli_num_rows($result) > 0) {
          while($row = mysqli_fetch_assoc($result)) {
            $output[] = $row;
          }
           print(json_encode($output));
       } else {
          print(json_encode($EMPTY));
       }

  // } 
  // else if ($auth_response  == "AUTH_FAILED"){
  //   print(json_encode($AUTH_FAILED));
  // }
  mysqli_close($connection);
?>