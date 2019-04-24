<?php

include "db_data.php";
date_default_timezone_set('Europe/Istanbul');

// create failure json message
$AUTH_FAILED = array();
$tmp["response"] 	= "AUTH_FAILED";		
array_push($AUTH_FAILED,$tmp);

$AUTH_SUCCESS = array();
$tmp["response"] 	= "AUTH_SUCCESS";		
array_push($AUTH_SUCCESS,$tmp);

$EMPTY = array();
$tmp["response"] = "NO_DATA";		
array_push($EMPTY,$tmp);

$UPLOAD_AUTH = array();
$tmp["response"] 	= "NO_PERMISSION_TO_UPLOAD";		
array_push($UPLOAD_AUTH,$tmp);

$UPLOAD_FAILED = array();
$tmp["response"] 	= "FAILED TO UPLOAD IMAGE, TRY AGAIN";		
array_push($UPLOAD_FAILED,$tmp);

$MARK_FAILED = array();
$tmp["response"] 	= "FAILED TO MARK PRESENT, OLD IMAGE REVERTED, TRY AGAIN!";		
array_push($MARK_FAILED,$tmp);

$MARK_FAILED_NO_RET = array();
$tmp["response"] 	= "FAILED TO MARK PRESENT, OLD IMAGE LOST, TRY AGAIN!";		
array_push($MARK_FAILED_NO_RET,$tmp);

$UPLOAD_SUCCESS = array();
$tmp["response"] 	= "IMAGE UPLOADED & STUDENT MARKED PRESENT!";		
array_push($UPLOAD_SUCCESS,$tmp);

$DB_CONN_ERROR = array();
$tmp["response"] 	= "FAILED TO CONNECT TO MYSQL!";		
array_push($DB_CONN_ERROR,$tmp);

$MARK_ABSENT_FAILED = array();
$tmp["response"] 	= "FAILED TO MARK ABSENT, TRY AGAIN!";		
array_push($MARK_ABSENT_FAILED,$tmp);

$MARK_ABSENT_SUCCESS = array();
$tmp["response"] 	= "STUDENT WAS MARKED ABSENT!";		
array_push($MARK_ABSENT_SUCCESS,$tmp);

// create failure json message
$AUTH_FAILED_DEVICE = array();
$tmp["response"] 	= "AUTH_FAILED_RASPBERRY";
array_push($AUTH_FAILED_DEVICE,$tmp);

// create failure json message
$AUTH_DEVICE_SUCCESS = array();
$tmp["response"] 	= "AUTH_SUCCESS_RASPBERRY";
array_push($AUTH_DEVICE_SUCCESS,$tmp);

// create failure json message
$EMPTY_PARAM = array();
$tmp["response"] 	= "EMPTY_PARAMS_GIVEN";
array_push($EMPTY_PARAM,$tmp);

$FAILED_TO_MARK = array();
$tmp["response"] 	= "FAIL_TO_MARK_SOME_STUDENTS";
array_push($FAILED_TO_MARK,$tmp);

$ALL_STUDENTS_MARKED = array();
$tmp["response"] 	= "ALL_STUDENTS_MARKED_CORRECTLY";
array_push($ALL_STUDENTS_MARKED,$tmp);

$ALL_FACEVECTORS_UPDATED = array();
$tmp["response"] 	= "ALL_FACEVECTORS_UPDATED_CORRECTLY";
array_push($ALL_FACEVECTORS_UPDATED, $tmp);

$FAILED_TO_UPDATE_FACEVECTOR = array();
$tmp["response"] 	= "FAIL_TO_UPDATE_ALL_FACEVECTORS";
array_push($FAILED_TO_UPDATE_FACEVECTOR,$tmp);


$IMAGE_DIR = "/homepages/46/d776529111/htdocs/bilmenu/aats/images/";

// authenticate
function attempt_authentication_student_only($ID, $password){
	$connection = mysqli_connect($GLOBALS[DB_HOST], $GLOBALS[DB_USER], $GLOBALS[DB_PASSWORD]);
	if (mysqli_connect_errno())  {   print(json_encode($GLOBALS[DB_CONN_ERROR]));   exit;  }
	mysqli_select_db($connection, $GLOBALS[DB_NAME]);

	$stmt_p = $connection->prepare("SELECT * FROM students WHERE ID = ? AND password = ? ; ");
	if( !$stmt_p){	print(json_encode($GLOBALS[AUTH_FAILED])); }else{	$stmt_p->bind_param("is", (int) $ID, $password); }

	$stmt_p->execute();
	$output = array();
	$result = $stmt_p->bind_result($id, $name, $surname, $email, $password, $facevector, $present, $first_time);
	if ( $result){
		if ($stmt_p->fetch()) {
			mysqli_close($connection);
			return true;
		}
		mysqli_close($connection);
		return false;
	}else{ 
		mysqli_close($connection);
		return false;
	}
}

function attempt_authentication_professor_only($ID, $password){
	$connection = mysqli_connect($GLOBALS[DB_HOST], $GLOBALS[DB_USER], $GLOBALS[DB_PASSWORD]);
	if (mysqli_connect_errno())  {   print(json_encode($GLOBALS[DB_CONN_ERROR]));   exit;  }
	mysqli_select_db($connection, $GLOBALS[DB_NAME]);

	$stmt_p = $connection->prepare("SELECT * FROM professors WHERE ID = ? AND password = ? ; ");
	if( !$stmt_p){	print(json_encode($AUTH_FAILED)); }else{	$stmt_p->bind_param("is",  $ID, $password); }

	$stmt_p->execute();
	$output = array();
	$result = $stmt_p->bind_result($id, $name, $surname, $email, $password);
	if ( $result){
		if ($stmt_p->fetch()) {
			mysqli_close($connection);
			return true;
		}
		mysqli_close($connection);
		return false;
	}else{ 
		mysqli_close($connection);
		return false;
	}
}


// authenticate raspberries
function attempt_authentication_raspberries($ID, $password){
	$connection = mysqli_connect($GLOBALS[DB_HOST], $GLOBALS[DB_USER], $GLOBALS[DB_PASSWORD]);
	if (mysqli_connect_errno())  {   print(json_encode($GLOBALS[DB_CONN_ERROR]));   exit;  }
	mysqli_select_db($connection, $GLOBALS[DB_NAME]);

	$stmt = $connection->prepare("SELECT * FROM devices_table WHERE ID = ? AND password = ? ; ");
	if( !$stmt){	print(json_encode($AUTH_FAILED_DEVICE)); exit; } else {	$stmt->bind_param("ss",  $ID, $password); }

	$stmt->execute();
	$output = array();
	$result = $stmt->bind_result($id, $password, $location);
	if ( $result){
		if ($stmt->fetch()) {
			mysqli_close($connection);
			return $location;		
		}else{
			mysqli_close($connection);
			return "AUTH_FAILED_RASPBERRY";
		}			
	}else{ mysqli_close($connection); return "AUTH_FAILED_RASPBERRY"; }
}

// authenticate
function get_professor_current_course($ID){
	$currentTime = getCLassTime();
    $currentTime = "1540-1630";
    $currentDayOfWeek = getDayOfWeek(); // uncomment for real demo phase
    $currentDayOfWeek = "Tuesday"; // hardcode according to db temporarily

	$connection = mysqli_connect($GLOBALS[DB_HOST], $GLOBALS[DB_USER], $GLOBALS[DB_PASSWORD]);
	if (mysqli_connect_errno())  {   print(json_encode($GLOBALS[DB_CONN_ERROR]));   exit;  }
	mysqli_select_db($connection, $GLOBALS[DB_NAME]);

	$stmt = $connection->prepare("SELECT DISTINCT classID from class_of_professor_in_time where professorID = ? AND day = '$currentDayOfWeek' AND time ='$currentTime'");
	if( !$stmt){	print(json_encode($AUTH_FAILED_DEVICE)); exit; } else {	$stmt->bind_param("i",   $ID) ;}

	$stmt->execute();
	$result = $stmt->bind_result($current_course);
	if ( $result){
		if ($stmt->fetch()) {
			mysqli_close($connection);
			return $current_course;		
		}else{
			mysqli_close($connection);
			return "NO_DATA";
		}			
	}else{ mysqli_close($connection);  return "NO_DATA"; }

}

function get_student_current_course($ID) {
   	$currentTime = getCLassTime();
    $currentTime = "1540-1630";
    $currentDayOfWeek = getDayOfWeek(); // uncomment for real demo phase
    $currentDayOfWeek = "Tuesday"; // hardcode according to db temporarily


	$connection = mysqli_connect($GLOBALS[DB_HOST], $GLOBALS[DB_USER], $GLOBALS[DB_PASSWORD]);
	if (mysqli_connect_errno())  {   print(json_encode($GLOBALS[DB_CONN_ERROR]));   exit;  }
	mysqli_select_db($connection, $GLOBALS[DB_NAME]);

	$stmt = $connection->prepare("SELECT DISTINCT classID FROM students_in_class WHERE studentID = ? AND day='$currentDayOfWeek' AND time='$currentTime'");
	if( !$stmt){	print(json_encode($GLOBALS[AUTH_FAILED_DEVICE])); exit; } else {	$stmt->bind_param("i", $ID) ;}

	$stmt->execute();
	$result = $stmt->bind_result($current_course);
	if ( $result){
		if ($stmt->fetch()) {
			mysqli_close($connection);
			return $current_course;		
		}else{
			mysqli_close($connection);
			return "NO_DATA";
		}			
	}else{ mysqli_close($connection);  return "NO_DATA"; }
}


function getCLassTime(){
	$temp_classes = array("0840-0930", "0940-1030", "1040-1130","1140-1230","1340-1430","1440-1530","1540-1630","1640-1730");
	$Hour = (int) date("H");
	$Minute =  (int) date("i");
	if($Hour >= 8 && $Hour <= 17){ // skip no-lecture hours
		if( !(($Hour == 12  && $Minute > 30 && $Minute < 59) || ($Hour == 13  && $Minute >= 0 && $Minute <= 39)) ){ // SKIP LUNCH TIME
			if($Minute <= 30 || $Minute >= 40 ){ // skip 10 MIN break time
					if($Hour == 8 || ($Hour == 9 && $Minute <= 30) ){
						return $temp_classes[0]; // FIRST HOUR
					}else if( ($Hour == 9 && $Minute >= 40) || ($Hour == 10 && $Minute <= 30) ) {
						return $temp_classes[1]; // SECOND HOUR
					}else if( ($Hour == 10 && $Minute >= 40) || ($Hour == 11 && $Minute <= 30) ) {
						return $temp_classes[2]; // THIRD HOUR
					}else if( ($Hour == 11 && $Minute >= 40) || ($Hour == 12 && $Minute <= 30) ) {
						return $temp_classes[3]; // FOURTH HOUR
					}else if( ($Hour == 13 && $Minute >= 40) || ($Hour == 14 && $Minute <= 30) ) {
						return $temp_classes[4]; // FIFTH HOUR
					}else if( ($Hour == 14 && $Minute >= 40) || ($Hour == 15 && $Minute <= 30) ) {
						return $temp_classes[5]; // SIXTH HOUR
					}else if( ($Hour == 15 && $Minute >= 40) || ($Hour == 16 && $Minute <= 30) ) {
						return $temp_classes[6]; // SEVENTH HOUR
					}else if( ($Hour == 16 && $Minute >= 40) || ($Hour == 17 && $Minute <= 30) ) {
						return $temp_classes[7]; // EIGHTTH HOUR
					}
			}
		}		
	}
	return "null";
}

 // function returns string format of the current day of week, omits Sat and Sun
function getDayOfWeek(){
	$dow = date("l");
	if($dow != "Saturday" && $dow != "Sunday"){
		return $dow;
	}else{
		return "null";
	}
}



?>