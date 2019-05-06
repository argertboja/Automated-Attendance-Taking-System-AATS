
<?php 
/* 
 1. RETRIEVE STUDENT DATA FROM ID & PASSWORD 
 LINK_TEST: https://bilmenu.com/aats/php/raspberry_request_student_data_w_facevector.php?ID=770253&password=123

 INPUT : ID = "770253"
 		 password = "123"

 OUTPUT : [
 			{"studentID":"21500009","facevector":"030232","image":"stringu i gjat base64"},
 		    {"studentID":"21500342","facevector":"151511","image":"stringu i gjat base64"},
 		    {"studentID":"21500342","facevector":"151511","image":"stringu i gjat base64"}
 		  ]

 OTHER POSSIBLE OUTPUTS : [{"response":"AUTH_FAILED_RASPBERRY"}]
 						  [{"response":"NO_DATA}]   //no students in that class in that hour that day
*/		

include "convert_functions.php";
include __DIR__ . "/secure/encryption.php";

$device_ID 		 = $_REQUEST['ID'];
$device_password = $_REQUEST['password'];

// echo $device_ID .  	$device_password;

$currentTime = getCLassTime();
$currentTime = "1540-1630";
$currentDayOfWeek = getDayOfWeek(); // uncomment for real demo phase
$currentDayOfWeek = "Tuesday"; // hardcode according to db temporarily

$location = attempt_authentication_raspberries($device_ID, $device_password);

if( !($location == "AUTH_FAILED_RASPBERRY") ){
	$connection = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD);
	if (mysqli_connect_errno())  {   print(json_encode($DB_CONN_ERROR));   exit;  }
	mysqli_select_db($connection, $DB_NAME);
	
	$sql = "select * from students_in_class where day = '$currentDayOfWeek' and time = '$currentTime' and location = '$location' ;";
	$result = mysqli_query($connection, $sql);
	$output = array();
	if($result){
		if (mysqli_num_rows($result) > 0) {
		    while($row = mysqli_fetch_assoc($result)) {
		    	$tmp = array();
		    	$tmp["studentID"] =	$row["studentID"];
		    	$fv = decrypt($row["facevector"]);
		    	if($fv){
		    		$tmp["facevector"] = $fv;
		    	}else{
		    		$tmp["facevector"] = "fv_data_attacked";
		    	}		    	
				array_push($output, $tmp);	
		    }
		     print(json_encode($output)); // PRINTS LIST OF ALL ENTRIES IN JSON FORMAT
		} else {
		    print(json_encode($EMPTY));
		}

	  }
	
}else{
	print(json_encode($AUTH_FAILED_DEVICE));
}


?>