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
$professorID = (int) $ID;

if( !(attempt_authentication($ID, $password)  == "AUTH_FAILED") ){ // IF auth does not fail, it succeeds, then proceed as usual 

		// $professorID = (int)$_REQUEST['professor_id'];
		 $sql = "SELECT * from class_of_professor_in_time";
		 $result = mysqli_query($connection, $sql);
		 $output = array();
		 if (mysqli_num_rows($result) > 0) {
		    while($row = mysqli_fetch_assoc($result)) {
		      $output[] = $row;
		    }
		     print(json_encode($output)); // PRINTS LIST OF ALL ENTRIES IN JSON FORMAT
		 } else {
		    print(json_encode($NO_DATA));
		 }

		
}else{
	print(json_encode($AUTH_FAILED));
}

 mysqli_close($connection);


?>