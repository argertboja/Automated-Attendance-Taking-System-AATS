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


//ID AND PASWORD ARE ALWAYS NEEDED
$ID = (int) $user_ID;;
$password = $user_password;;
$studentID = (int) $user_ID;
$currentTime = getCLassTime();
$currentTime = "1540-1630";
$currentDayOfWeek = getDayOfWeek(); // uncomment for real demo phase
$currentDayOfWeek = "Tuesday"; // hardcode according to db temporarily

if( !(attempt_authentication($ID, $password)  == "AUTH_FAILED") ){ // IF auth does not fail, it succeeds, then proceed as usual 
		 
		 $sql = "SELECT DISTINCT classID FROM students_in_class WHERE studentID = $studentID AND day='$currentDayOfWeek' AND time='$currentTime'";
		 $result = mysqli_query($connection, $sql);
		 $output = array();
		 if (mysqli_num_rows($result) > 0) {
		    while($row = mysqli_fetch_assoc($result)) {
		      $output[] = $row;
		    }
		     print(json_encode($output)); // PRINTS LIST OF ALL ENTRIES IN JSON FORMAT
		 } else {
		    print(json_encode($EMPTY));
		 }
}else{
	print(json_encode($AUTH_FAILED));
}

 mysqli_close($connection);


?>