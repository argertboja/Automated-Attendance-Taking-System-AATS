<?php

// create failure json message
$AUTH_FAILED = array();
$tmp["response"] 	= "AUTH_FAILED";		
array_push($AUTH_FAILED,$tmp);

$AUTH_SUCCESS = array();
$tmp["response"] 	= "AUTH_FAILED";		
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


// authenticate
function attempt_authentication($ID, $password){
	$path =  "php login.php $ID $password";//?ID=' . $_REQUEST['ID']. '&password=' . $_REQUEST['password'];

	$authentication_result =  exec($path);

	$authentication_result = json_decode($authentication_result, true);
	
	$value = isset($authentication_result[0]["response"]) ? "AUTH_FAILED" : "AUTH_SUCCESS";
	return $value;

}

// authenticate
function attempt_authentication_raspberries($ID, $password){
	$path =  "php login_device.php $ID $password";
	$authentication_result =  exec($path);

	$authentication_result = json_decode($authentication_result, true);
	
	$value = isset($authentication_result[0]["response"]) ? $authentication_result[0]["response"] : $authentication_result[0]["location"];
	// print ($value);
	return $value;

	// if ($value == "AUTH_SUCCESS_RASPBERRY") { return true;}
	// else if ($value == "AUTH_FAILED_RASPBERRY") { return false;}
	// return false;
}

// authenticate
function get_professor_current_course($ID, $password){
	$path =  "php get_professor_current_course.php $ID $password"; 

	$authentication_result =  exec($path);

	$authentication_result = json_decode($authentication_result, true);

	$value = isset($authentication_result[0]["response"]) ? "NO_DATA" : $authentication_result[0]["classID"];
	return $value;

	
}

function get_student_current_course($ID, $password) {
    $path = "php retreive_student_info.php $ID $password";
    
    $authentication_result = exec($path);
    
    $authentication_result = json_decode($authentication_result, true);
    
    $value = isset ($authentication_result[0]["response"]) ? "NO_DATA" : $authentication_result[0]["classID"];
    
    return $value;
}

function get_student_presence($ID, $password) {
    $path = "php retreive_student_info.php $ID $password";
    
    $authentication_result = exec($path);
    
    $authentication_result = json_decode($authentication_result, true);
    
    $value = isset ($authentication_result[0]["response"]) ? "NO_DATA" :
        $authentication_result[0]["present"];
    
    return $value;
}

function getCLassTime(){
	$temp_classes = array("0840-0930", "0940-1030", "1040-1130","1140-1230","1340-1430","1440-1530","1540-1630","1640-1730");
	$Hour = (int) date("H");
	$Minute =  (int) date("i");
	if($Hour >= 8 && $Hour <= 17){ // skip no-lecture hours
		if( !(($Hour == 12  && $Minute > 30 && $Minute < 59) || ($Hour == 13  && $Minute >= 0 && $Minute <= 39)) ){ // SKIP LUNCH TIME
			if($Minute <= 30 || $Minute >= 40 ){ // skip 10 MIN break time
					if($Hour == 08 || ($Hour == 09 && $Minute <= 30) ){
						return $temp_classes[0]; // FIRST HOUR
					}else if( ($Hour == 09 && $Minute >= 40) || ($Hour == 10 && $Minute <= 30) ) {
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