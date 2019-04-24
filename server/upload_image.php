<?php
    include "convert_functions.php";
	//ID AND PASWORD ARE ALWAYS NEEDED
	$ID = (string)$_REQUEST['ID'];
	$password = $_REQUEST['password'];
	$uploadDone = false;

	if( attempt_authentication_professor_only($ID, $password) ){ // IF auth does not fail, it succeeds, then proceed as usual 
		    $base = $_REQUEST['image'];
		    $filename =  $_REQUEST['filename'] ;
		    $studentID = $filename;
		    $filename = $IMAGE_DIR . $filename. ".jpg";
		    $saved_old_image = file_get_contents($filename); 
			$saved_old_image_base64 = base64_encode($saved_old_image);

		    $binary=base64_decode($base);
		    header('Content-Type: bitmap; charset=utf-8');
		    $file = fopen($filename, 'wb');
		    if(fwrite($file, $binary)){
		    	if(fclose($file)){
					$connection = mysqli_connect($DB_HOST, $DB_USER, $DB_PASSWORD);
					if (mysqli_connect_errno())  {  print(json_encode($DB_CONN_ERROR));   exit;  }
					mysqli_select_db($connection, $DB_NAME);

					/* Prepare an update statement */
					$query = "UPDATE students SET present = 1 , first_time = 0 WHERE ID = ? ;";
					$stmt = $connection->prepare($query);
					if(!$stmt){ print(json_encode($MARK_FAILED_NO_RET));  mysqli_close($connection); exit; }	
					$stmt->bind_param("i", $studentID);

					/* Execute the statement */
					$result = $stmt->execute();

					if($result){	
						print(json_encode($UPLOAD_SUCCESS));			
					}else {					   
					    $binary=base64_decode($saved_old_image_base64);
					    header('Content-Type: bitmap; charset=utf-8');
					    $file = fopen($filename, 'wb');
					    if(fwrite($file, $binary)){
					    	if(fclose($file)){
					    		 	print(json_encode($MARK_FAILED));// MARK FAILED IN DB AND OLD WRITTEN IMAGE IS REVERTED
					    		}else{
					    			print(json_encode($MARK_FAILED_NO_RET));// MARK FAILED IN DB AND OLD WRITTEN IMAGE IS LOST
					    		}
					    }
					}
					$stmt->close();
		    	}
		    }
		   
	}else{
		print(json_encode($UPLOAD_AUTH));
	}
 mysqli_close($connection);
?>

