<?php

include "convert_functions.php";

//ID AND PASWORD ARE ALWAYS NEEDED
$ID = (string)$_REQUEST['ID'];
$password = $_REQUEST['password'];
$studentID = $_REQUEST['studentID'];

if( attempt_authentication_professor_only($ID, $password)  ){ // IF auth does not fail, it succeeds, then proceed as usual 
		$connection = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD);
		if (mysqli_connect_errno())  {  print(json_encode($DB_CONN_ERROR));   exit;  }
		mysqli_select_db($connection, $DB_NAME);

		/* Prepare an update statement */
		$query = "UPDATE students SET present = 0 WHERE ID = ? ;";
		$stmt = $connection->prepare($query);
		if(!$stmt){  print(json_encode($MARK_ABSENT_FAILED));  mysqli_close($connection);  exit; }	
		$stmt->bind_param("i", $studentID);

		/* Execute the statement */
		$result = $stmt->execute();

		if($result){	
			print(json_encode($MARK_ABSENT_SUCCESS));	
			
		}else {		
			print(json_encode($MARK_ABSENT_FAILED));			
		}

		mysqli_stmt_close($stmt);

		
}else{
	print(json_encode($AUTH_FAILED));
}

 mysqli_close($connection);


?>