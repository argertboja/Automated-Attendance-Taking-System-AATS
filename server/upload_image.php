<?php
    include "convert_functions.php";
	//ID AND PASWORD ARE ALWAYS NEEDED
	$ID = (string)$_REQUEST['ID'];
	$password = $_REQUEST['password'];
	$uploadDone = false;

	if( !(attempt_authentication($ID, $password)  == "AUTH_FAILED") ){ // IF auth does not fail, it succeeds, then proceed as usual 
		    $base = $_REQUEST['image'];
		    $filename =  $_REQUEST['filename'] ;
		    $studentID = $filename;
		    $filename = "/home/accentjanitorial/public_html/accentjanitorial.com/aats_admin/images/".$filename. ".jpg";
		    $saved_old_image = file_get_contents($filename); 
			$saved_old_image_base64 = base64_encode($saved_old_image);

		    $binary=base64_decode($base);
		    header('Content-Type: bitmap; charset=utf-8');
		    $file = fopen($filename, 'wb');
		    if(fwrite($file, $binary)){
		    	if(fclose($file)){
		    		$connection = mysqli_connect("160.153.75.104","aats_admin","aats_admin123");
					if (mysqli_connect_errno())  { print(json_encode($DB_CONN_ERROR));  }
					mysqli_select_db($connection,"AATS_Database");

					/* Prepare an update statement */
					$query = "UPDATE students SET present = 1 WHERE ID = ? ;";
					$stmt = $connection->prepare($query);
					if(!$stmt){ print(json_encode($MARK_FAILED_NO_RET)); exit; }	
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
?>

