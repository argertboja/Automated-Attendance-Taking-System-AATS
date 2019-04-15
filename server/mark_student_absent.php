<?php

include "convert_functions.php";
$connection = mysqli_connect("160.153.75.104","aats_admin","aats_admin123");

if (mysqli_connect_errno())
  {
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

mysqli_select_db($connection,"AATS_Database");


//ID AND PASWORD ARE ALWAYS NEEDED
$ID = (string)$_REQUEST['ID'];
$password = $_REQUEST['password'];
$studentID = $_REQUEST['studentID'];

if( !(attempt_authentication($ID, $password)  == "AUTH_FAILED") ){ // IF auth does not fail, it succeeds, then proceed as usual 

		/* Prepare an update statement */
		$query = "UPDATE students SET present = 0 WHERE ID = ? ;";
		$stmt = $connection->prepare($query);
		if(!$stmt){ print(json_encode($MARK_ABSENT_FAILED)); exit; }	
		$stmt->bind_param("i", $studentID);

		/* Execute the statement */
		$result = $stmt->execute();

		if($result){	
			print(json_encode($MARK_ABSENT_SUCCESS));			
		}else {		
			print(json_encode($MARK_ABSENT_FAILED));			
		}

		
}else{
	print(json_encode($AUTH_FAILED));
}

 mysqli_close($connection);


?>