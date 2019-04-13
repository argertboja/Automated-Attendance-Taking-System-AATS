<?php

// create failure json message
$AUTH_FAILED = array();
$tmp["response"] 	= "AUTH_FAILED";		
array_push($AUTH_FAILED,$tmp);

$EMPTY = array();
$tmp["response"] 	= "NO_DATA";		
array_push($$EMPTY,$tmp);

$UPLOAD_AUTH = array();
$tmp["response"] 	= "NO_PERMISSION_TO_UPLOAD";		
array_push($$UPLOAD_AUTH,$tmp);

$UPLOAD_SUCCESS = array();
$tmp["response"] 	= "IMAGE UPLOADED SUCCESSFULLY!";		
array_push($$UPLOAD_SUCCESS,$tmp);


// authenticate
function attempt_authentication($ID, $password){
	$path =  "php login.php $ID $password";//?ID=' . $_REQUEST['ID']. '&password=' . $_REQUEST['password'];

	$authentication_result =  exec($path);

	$authentication_result = json_decode($authentication_result, true);

	$authentication_result =  $authentication_result[0]["response"];
	
	return $authentication_result;
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