<?php

include "convert_functions.php";

$connection = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD);
if (mysqli_connect_errno())  {  print(json_encode($DB_CONN_ERROR));   exit;  }
mysqli_select_db($connection, $DB_NAME);


$ID = (string)$_REQUEST['ID'];
$password = $_REQUEST['password'];
$professorID = (int) $ID;


if(attempt_authentication_professor_only($ID, $password)){
		// set timezone to istanbul to receive current time ( to be exctended to other countries in the future)
		date_default_timezone_set('Europe/Istanbul');
		
		$currentTime = getCLassTime();
		$currentTime = "1540-1630";
		$currentDayOfWeek = getDayOfWeek(); // uncomment for real demo phase
		$currentDayOfWeek = "Tuesday"; // hardcode according to db temporarily

		// echo $currentDayOfWeek;

		// $professorID = (int)$_REQUEST['professor_id'];
		 $sql = "SELECT DISTINCT classID from class_of_professor_in_time where professorID = $professorID AND day = '$currentDayOfWeek' AND time ='$currentTime'";
		 $result = mysqli_query($connection, $sql);
		 $output = array();
		 if (mysqli_num_rows($result) > 0) {
		    while($row = mysqli_fetch_assoc($result)) {
		      $output[] = $row;
		    }
		     // print(json_encode($output));
		 } else {
		    print(json_encode($EMPTY));
		    mysqli_close($connection);
		    exit;
		 }

		if($output[0]["classID"]){
		  	$courseName = (int) $output[0]["classID"];

		 	$sql = "SELECT DISTINCT studentID, present from students_in_class WHERE classID =  $courseName ";
	 		$result = mysqli_query($connection, $sql);
		 	$output = array();
		 	
		 	if (mysqli_num_rows($result) > 0) {
			    while($row = mysqli_fetch_assoc($result)) {
			        	$tmp = array();
					    $tmp["studentID"] = $row["studentID"];
					    $tmp["present"] = $row["present"];
					    $path = $IMAGE_DIR . $row["studentID"] . '.jpg';
						$type = pathinfo($path, PATHINFO_EXTENSION);
						$data = file_get_contents($path);
						$base64 = 'data:image/' . $type . ';base64,' . base64_encode($data);
					 	$tmp["base64"]	= $base64;
						array_push($output, $tmp);				
			    }	
			    print(json_encode($output));
		 	} else {
			   print(json_encode($EMPTY));
		 	}
	 	}else{
	 		 print(json_encode($EMPTY));
	 	}
}else if ($auth_response  == "AUTH_FAILED"){
	print(json_encode($AUTH_FAILED));
}
 mysqli_close($connection);


?>