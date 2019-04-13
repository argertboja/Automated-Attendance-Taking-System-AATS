<?php

include "convert_functions.php";
$connection = mysqli_connect("160.153.75.104","aats_admin","aats_admin123");

if (mysqli_connect_errno())
  {
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

mysqli_select_db($connection,"AATS_Database");


$ID = (string)$_REQUEST['ID'];
$password = $_REQUEST['password'];
$professorID = (int) $ID;

if( !(attempt_authentication($ID, $password)  == "AUTH_FAILED") ){ // IF auth does not fail, it succeeds, then proceed as usual 

		// set timezone to istanbul to receive current time ( to be exctended to other countries in the future)
		date_default_timezone_set('Europe/Istanbul');
		$parentPath = "/home/accentjanitorial/public_html/accentjanitorial.com/aats_admin/images/";
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
		    print(json_encode($NO_DATA));
		 }

		if($output[0]["classID"]){
		  	$courseName = (string) $output[0]["classID"];

		 	$sql = "SELECT DISTINCT studentID from students_in_class WHERE classID =  '$courseName' ";
	 		$result = mysqli_query($connection, $sql);
		 	$output = array();
		 	if (mysqli_num_rows($result) > 0) {
			    while($row = mysqli_fetch_assoc($result)) {
			        $tmp = array();
				    $tmp["studentID"] = $row["studentID"];
				    $path = $parentPath . $row["studentID"] . '.jpg';
					$type = pathinfo($path, PATHINFO_EXTENSION);
					$data = file_get_contents($path);
					$base64 = 'data:image/' . $type . ';base64,' . base64_encode($data);
				 	$tmp["base64"]	= $base64;
					array_push($output, $tmp);
			    }
			    print(json_encode($output));
		 	} else {
			   print(json_encode($NO_DATA));
		 	}
	 	}else{
	 		 print(json_encode($NO_DATA));
	 	}
}else{
	print(json_encode($AUTH_FAILED));
}


 mysqli_close($connection);


?>