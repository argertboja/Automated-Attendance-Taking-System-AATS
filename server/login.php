<?php
if (isset($argc)) {
	for ($i = 0; $i < $argc; $i++) {
		if($i == 1){
			$user_ID = (int)$argv[$i];
		}else if($i == 2 ){
			$user_password =  $argv[$i];
		}
	}
}else{
	$user_ID 		= (int)$_REQUEST['ID'];
	$user_password 	= $_REQUEST['password'];
}

include "convert_functions.php";

$currentTime = getCLassTime();
$currentTime = "1540-1630";

// test this file at https://bilmenu.com/aats/php/login.php?ID=4&password=haluk123
// or https://bilmenu.com/aats/php/login.php?ID=21500342&password=ndricim123

$connection = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD);
if (mysqli_connect_errno())  {  print(json_encode($DB_CONN_ERROR));   exit;  }
mysqli_select_db($connection, $DB_NAME);


// create failure json message
$AUTH_FAILED = array();
$tmp["response"] 	= "AUTH_FAILED";
array_push($AUTH_FAILED,$tmp);


$stmt_s = $connection->prepare("SELECT * FROM students WHERE ID = ? AND password = ? ; ");
if( !$stmt_s){  print(json_encode($AUTH_FAILED)); } else{	$stmt_s->bind_param("is",  $user_ID, $user_password); }

$stmt_p = $connection->prepare("SELECT * FROM professors WHERE ID = ? AND password = ? ; ");
if( !$stmt_p){	print(json_encode($AUTH_FAILED)); }else{	$stmt_p->bind_param("is",  $user_ID, $user_password); }


$isStudent = check_if_student($user_ID , $connection);
$isProfessor = check_if_professor($user_ID , $connection);

//Authentication process
if($isProfessor){
	$output_result = returnProfessorData($connection, $stmt_p);
	if ($output_result != -1){
		print(json_encode($output_result)); 
	}else { print(json_encode($AUTH_FAILED)); /*print(":: Authentication Failed"); */ }
}else if($isStudent){

	$output_result = returnStudentData($connection, $stmt_s);
	if ($output_result != -1){

		print(json_encode($output_result));
	}else {  print(json_encode($AUTH_FAILED)); /* print(":: Authentication Failed");*/}
}else{

	print(json_encode($AUTH_FAILED));
	// print ("AUTH_FAILED:: USER WITH THAT ID DOES NOT EXIST");
}
        


//***********************************************************************
// FUNCTIONS BELOW:
//function returns true if user with this id is a professor
function check_if_professor($id , $connection ) {
$stmt = $connection->prepare("select check_if_professor( ? ) AS result");
	if( !$stmt){ 	print(json_encode($AUTH_FAILED));/*print("FAILED TO PREPARE CHECK professor STMT :: ");*/ } else{	$stmt->bind_param("i",  $id); }

	$stmt->execute();
	$output = array();
	$result = $stmt->bind_result($id);
	if ( $result){
		while ($stmt->fetch()) {
		    $tmp = array();
		    $tmp["result"] 	= $id;		  
			array_push($output, $tmp);
		}	

		if ( (int)$output[0]["result"] == 1 ){  	return true;   }
	}else{ return false;  /*print("ERROR FETCHING - CHECK STUDENT");*/}
}

//function returns true if user with this id is a student
function check_if_student($id , $connection ) {

	$stmt = $connection->prepare("select check_if_student( ? ) AS result");
	if( !$stmt){ print(json_encode($AUTH_FAILED)); /*print("FAILED TO PREPARE CHECK STUDENT STMT :: ");*/ } else{	$stmt->bind_param("i",  $id); }

	$stmt->execute();
	$output = array();
	$result = $stmt->bind_result($id);
	if ( $result){
		while ($stmt->fetch()) {
		    $tmp = array();
		    $tmp["result"] 	= $id;		  
			array_push($output, $tmp);
		}	
		if ( (int)$output[0]["result"] == 1 ){  	return true;   }
	}else{  return false; /*print("ERROR FETCHING - CHECK STUDENT");*/}

}


// function takes connection object, query and returns result or -1 if something goes wrong
function returnProfessorData($connection, $stmt){
	global $user_ID; global $user_password; global $currentTime;
	$current_course_professor = get_professor_current_course($user_ID);

	global $AUTH_FAILED;
	$stmt->execute();
	$output = array();
	$result = $stmt->bind_result($id, $name, $surname, $email, $password);
	if ( $result){
		while ($stmt->fetch()) {
		    $tmp = array();
		    $tmp["classID"]	    = $current_course_professor;
		    $tmp["ID"] 			= $id;
		    $tmp["name"]		= $name;
		    $tmp["surname"] 	= $surname;
			$tmp["email"] 		= $email;
			$tmp["password"]	= $password;
			$tmp["present"]	    = "-1";
			$tmp["current_hour"] = $currentTime;
			array_push($output, $tmp);
		}		
		
		if( empty($output) ) {
			// print(json_encode($AUTH_FAILED));
			return -1;
		}
	 	// print (($output));
	 	
		return $output;
	}else{ print(json_encode($AUTH_FAILED));  return -1; }
}

// function takes connection object, query and returns result or -1 if something goes wrong
function returnStudentData($connection, $stmt){
	global $user_ID; global $user_password; global $currentTime;
	$current_course_student = get_student_current_course($user_ID);

	global $AUTH_FAILED;
	 $stmt->execute();

	$output = array();
	$result = $stmt->bind_result($id, $name, $surname, $email, $password, $facevector, $present, $first_time);
	if ( $result){
		while ($stmt->fetch()) {
		    $tmp = array();
		    $tmp["classID"]		= $current_course_student;
		    $tmp["ID"] 			= $id;
		    $tmp["name"]		= $name;
		    $tmp["surname"] 	= $surname;
			$tmp["email"] 		= $email;
			$tmp["password"]	= $password;
			$tmp["present"]	    = $present;
			$tmp["current_hour"] = $currentTime;
			array_push($output, $tmp);
		}		

		if( empty($output) ) {
			// print(json_encode($AUTH_FAILED));
			return -1;
		}
	 	
	 	
		return $output;
	}else{ print(json_encode($AUTH_FAILED));  return -1; }
}

mysqli_close($connection);

?>