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

$connection = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD);
if (mysqli_connect_errno())  {  print(json_encode($DB_CONN_ERROR));   exit;  }
mysqli_select_db($connection, $DB_NAME);
  
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
       $output = "NO_DATA";
       if (mysqli_num_rows($result) > 0) {
          if($row = mysqli_fetch_assoc($result)) {
            $output = $row["classID"];
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