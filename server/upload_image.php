<?php
    include "convert_functions.php";
	//ID AND PASWORD ARE ALWAYS NEEDED
	$ID = (string)$_REQUEST['ID'];
	$password = $_REQUEST['password'];

	if( !(attempt_authentication($ID, $password)  == "AUTH_FAILED") ){ // IF auth does not fail, it succeeds, then proceed as usual 
		    $base = $_REQUEST['image'];
		    $filename =  $_REQUEST['filename'] ;
		    $filename = "/home/accentjanitorial/public_html/accentjanitorial.com/aats_admin/images/".$filename;
		    $binary=base64_decode($base);
		    header('Content-Type: bitmap; charset=utf-8');
		    $file = fopen($filename, 'wb');
		    fwrite($file, $binary);
		    fclose($file);
		    print(json_encode($UPLOAD_SUCCESS));
	}else{
		print(json_encode($UPLOAD_AUTH));
	}
?>

