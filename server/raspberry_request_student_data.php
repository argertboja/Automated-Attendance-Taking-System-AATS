
<?php 
/* 
 1. RETRIEVE STUDENT DATA FROM ID & PASSWORD 
 LINK_TEST: http://accentjanitorial.com/accentjanitorial.com/aats_admin/public_html/raspberry_request_student_data.php?ID=770253&password=123

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

$device_ID 		 = $_REQUEST['ID'];
$device_password = $_REQUEST['password'];



$currentTime = getCLassTime();
$currentTime = "1540-1630";
$currentDayOfWeek = getDayOfWeek(); // uncomment for real demo phase
$currentDayOfWeek = "Tuesday"; // hardcode according to db temporarily

$location = attempt_authentication_raspberries($device_ID, $device_password);
// print ($location);
if( !($location == "AUTH_FAILED_RASPBERRY") ){
	$connection = mysqli_connect("160.153.75.104","aats_admin","aats_admin123");
	if (mysqli_connect_errno()) { print(json_encode($AUTH_FAILED_DEVICE)); exit; }
    mysqli_select_db($connection,"AATS_Database");
	
	$sql = "select * from students_in_class where day = '$currentDayOfWeek' and time = '$currentTime' and location = '$location' ;";
	$result = mysqli_query($connection, $sql);
	$output = array();
	if (mysqli_num_rows($result) > 0) {
	    while($row = mysqli_fetch_assoc($result)) {
	    	$tmp = array();
	    	$tmp["studentID"] =	$row["studentID"];
	    	$tmp["facevector"] = $row["facevector"];

	    	// get base64 image of student 
	    	$filename = "/home/accentjanitorial/public_html/accentjanitorial.com/aats_admin/images/". $row["studentID"] . ".jpg";
		    $file = file_get_contents($filename); 
			$base_64_img = base64_encode($file);

	    	$tmp["image"] = $base_64_img;
			array_push($output, $tmp);	
	    }
	     print(json_encode($output)); // PRINTS LIST OF ALL ENTRIES IN JSON FORMAT
	} else {
	    print(json_encode($EMPTY));
	}
}else{
	print(json_encode($AUTH_FAILED_DEVICE));
}


?>