<?php
	if (isset($argc)) {
	for ($i = 0; $i < $argc; $i++) {
		if($i == 1){
			$device_ID = $argv[$i];
		}else if($i == 2 ){
			$device_password =  $argv[$i];
		}
	}
}else{
	$device_ID 		 = (int)$_REQUEST['ID'];
	$device_password = $_REQUEST['password'];
}
// test this file at https://bilmenu.com/aats/php/login_device.php?ID=770253&password=123 
include "convert_functions.php";

$connection = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD);
if (mysqli_connect_errno())  {  print(json_encode($DB_CONN_ERROR));   exit;  }
mysqli_select_db($connection, $DB_NAME);


$stmt = $connection->prepare("SELECT * FROM devices_table WHERE ID = ? AND password = ? ; ");
if( !$stmt){	print(json_encode($AUTH_FAILED_DEVICE)); exit; } else {	$stmt->bind_param("ss",  $device_ID, $device_password); }

$stmt->execute();
$output = array();
$result = $stmt->bind_result($id, $password, $location);
if ( $result){
	if ($stmt->fetch()) {
		$tmp = array();
	    $tmp["location"]	= $location;
		array_push($output, $tmp);		
		
		print(json_encode($output));
	}else{
		print(json_encode($AUTH_FAILED_DEVICE)); 
	}			
}else{ print(json_encode($AUTH_FAILED_DEVICE));  }


mysqli_close($connection);

?>
