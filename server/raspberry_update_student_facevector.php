<?php

/* 
 2. UPDATE STUDENTS FACEVECTOR FROM RASPBERRY
 LINK_TEST: https://bilmenu.com/aats/php/raspberry_update_student_facevector.php?ID=770253&password=123&studentList=21500342:1,21500009:1,21503525:1

 INPUT : ID = "770253"
 		password = "123"
 		studentList = "21500342:1,21500009:1,21503525:1" 

 OUTPUT : [{"response":"ALL_VECTORS_UPDATED_CORRECTLY"}]

 OTHER POSSIBLE OUTPUTS : [{"response":"AUTH_FAILED_RASPBERRY"}]
 						  [{"response":"FAIL_TO_UPDATE_SOME_VECTORS"}]
*/					

include "convert_functions.php";
include __DIR__ . "/secure/encryption.php";

$device_ID 		 = $_REQUEST['ID'];
$device_password = $_REQUEST['password'];

$list_students = $_REQUEST['studentList'];

// $list_students = "21500342:0,21500009:1,21503525:0"; // sample input string

$location = attempt_authentication_raspberries($device_ID, $device_password);

if( !($location == "AUTH_FAILED_RASPBERRY") ){
	$connection = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD);
	if (mysqli_connect_errno())  {   print(json_encode($DB_CONN_ERROR));   exit;  }
	mysqli_select_db($connection, $DB_NAME);
	
	$list_exploded = explode(",", $list_students);
	$size_of_array = sizeof($list_exploded);
	$result = array();
	if($list_exploded && $list_exploded[0] != "" ){
		$result = array();
		for ( $i = 0; $i < $size_of_array; $i++){
			$temp_list =  explode(":", $list_exploded[$i]);
			if($temp_list && $list_exploded[0] != "" ){
		    	$result[$temp_list[0]] = $temp_list[1];
	    	}
		}

		$total_updated = 0;
		while(key($result) ){
			$studentID = (int) key($result);
			$facevector =  current($result);
			if($facevector == null || $facevector == ""){
				$facevector = "empty";
			}
			$facevector = encrypt($facevector);
			// echo $facevector;

			$sql = "UPDATE students SET facevector = '$facevector' , first_time = 1 WHERE ID = $studentID " ;
			
			$retval =  mysqli_query($connection, $sql);
			if($retval) {
				$total_updated++;
			}
			next($result);
		}

		if($total_updated = $size_of_array){
			print(json_encode($ALL_FACEVECTORS_UPDATED));
		}else{
			print(json_encode($FAILED_TO_UPDATE_FACEVECTOR));
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