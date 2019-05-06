<?php
// Encrypt string using key, iv and cipher method
// encryption process generates unique authentication tag for each encryption made
// auth_tag generated is encrypted again using encryptTag() method and concatenated on result of 
// encrypt() method
// decrypt() method separates the tag from the ciphered text, decrypts the tag using decryptTag
// then uses the decrypted tag to decrypt the ciphered text

function encrypt($text){
	$cipher = file_get_contents('cipher.xyz', FILE_USE_INCLUDE_PATH);
	$iv 	= file_get_contents('iv.xyz', FILE_USE_INCLUDE_PATH);
	$key 	= file_get_contents('key.xyz', FILE_USE_INCLUDE_PATH);

	if (in_array($cipher, openssl_get_cipher_methods())){
   	 	$ciphertext = openssl_encrypt($text, $cipher, $key, $options=0, $iv, $tag);
   	 	if($ciphertext){
            // $encryptedTag  = hex2bin(encryptTAG($tag));            
   	 		// return  bin2hex($encryptedTag . $ciphertext);
             // echo $tag;
            return $tag . $ciphertext;
   	 	}else{
   	 		return null;
   	 	}
 	}
 	return null;
}

function decrypt($textToDecrypt){
	$cipher = file_get_contents('cipher.xyz', FILE_USE_INCLUDE_PATH);
	$iv 	= file_get_contents('iv.xyz', FILE_USE_INCLUDE_PATH);
	$key 	= file_get_contents('key.xyz', FILE_USE_INCLUDE_PATH);
   // echo $textToDecrypt;
   $num = 16 - strlen($textToDecrypt);

   // $decoded = hex2bin($textToDecrypt);
   // $tag = mb_substr($decoded, 0, 16, '8bit');
   // // $tag = decryptTAG(bin2hex($tag));
   // $textToDecrypt = mb_substr($decoded, 16, null, '8bit');
   
   $tag = substr($textToDecrypt, 0, $num);
   $textToDecrypt = substr($textToDecrypt,  16);
   
	if (in_array($cipher, openssl_get_cipher_methods())){
 		$decryptedtext = openssl_decrypt($textToDecrypt, $cipher, $key, $options=0, $iv, $tag);
   	 	if($decryptedtext){
   	 		return $decryptedtext;
   	 	}else{
            echo "false;";
   	 		return null;
   	 	}  
	}
	return null;
}

// We need to concatenate the tag along with the cihpered text so we use another encryption method for that as well
function encryptTAG(string $ssn): string
{
   $key    = file_get_contents("tag_key.xyz", FILE_USE_INCLUDE_PATH);
   $nonce = random_bytes(24);
   $ciphertext = sodium_crypto_secretbox($ssn, $nonce, $key);
   return bin2hex($nonce . $ciphertext);
}

function decryptTAG(string $ciphertext): string
{
   $key    = file_get_contents("tag_key.xyz", FILE_USE_INCLUDE_PATH);
   $decoded = hex2bin($ciphertext);
   $nonce = mb_substr($decoded, 0, 24, '8bit');
   $cipher = mb_substr($decoded, 24, null, '8bit');
   return sodium_crypto_secretbox_open($cipher, $nonce, $key);
}

?>