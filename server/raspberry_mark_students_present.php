<?php

/* 
 2. MARK STUDENTS PRESENT/ABSENT FROM RASPBERRY (PRESENT ONLY, ABSENTS STAY ABSENT)
 LINK_TEST: http://accentjanitorial.com/accentjanitorial.com/aats_admin/public_html/raspberry_mark_students_present.php?ID=770253&password=123&studentList=21500342:1,21500009:1,21503525:1

 INPUT : ID = "770253"
 		password = "123"
 		studentList = "21500342:1,21500009:1,21503525:1" 

 OUTPUT : [{"response":"ALL_STUDENTS_MARKED_CORRECTLY"}]

 OTHER POSSIBLE OUTPUTS : [{"response":"AUTH_FAILED_RASPBERRY"}]
 						  [{"response":"FAIL_TO_MARK_SOME_STUDENTS"}]
*/					

include "convert_functions.php";

$device_ID 		 = $_REQUEST['ID'];
$device_password = $_REQUEST['password'];

$list_students = $_REQUEST['studentList'];

// $list_students = "21500342:0,21500009:1,21503525:0"; // sample input string

$location = attempt_authentication_raspberries($device_ID, $device_password);

if( !($location == "AUTH_FAILED_RASPBERRY") ){
	$connection = mysqli_connect("160.153.75.104","aats_admin","aats_admin123");
	if (mysqli_connect_errno()) { print(json_encode($AUTH_FAILED_DEVICE)); exit; }
    mysqli_select_db($connection,"AATS_Database");
	
	$list_exploded = explode(",", $list_students);
	$size_of_array = sizeof($list_exploded);
	$result = array();
	if($list_exploded && $list_exploded[0] != "" ){
		$result = array();
		for ( $i = 0; $i < $size_of_array; $i++){
			$temp_list =  explode(":", $list_exploded[$i]);
			if($temp_list && $list_exploded[0] != "" ){
		    	$result[$temp_list[0]] = (int)$temp_list[1];
	    	}
		}

		$total_marked = 0;
		while(key($result) ){
			$studentID = (int) key($result);
			$presence = (int) current($result);
			// echo $studentID . " = ";
			// echo $presence . '<br>' ;
			$sql = "UPDATE students SET present = $presence WHERE ID = $studentID " ;
			$retval =  mysqli_query($connection, $sql);
			if($retval) {
				$total_marked++;
			}
			next($result);
		}

		if($total_marked = $size_of_array){
			print(json_encode($ALL_STUDENTS_MARKED));
		}else{
			print(json_encode($FAILED_TO_MARK));
		}
		mysqli_close($connection);

		// print(json_encode($result)); // [{"21500342":"0"},{"21500009":"1"},{"21503525":"0"}]
	}else{
		print(json_encode($EMPTY_PARAM));
		mysqli_close($connection);
		exit;
	}
}else{
	print(json_encode($AUTH_FAILED_DEVICE));
}



?>