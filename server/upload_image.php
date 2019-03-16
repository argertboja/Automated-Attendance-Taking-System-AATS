<?php
    $base = $_REQUEST['image'];
    $filename =  $_REQUEST['filename'] ;
    $binary=base64_decode($base);
    header('Content-Type: bitmap; charset=utf-8');
    $file = fopen($filename, 'wb');
    fwrite($file, $binary);
    fclose($file);
    echo 'Image uploaded to server!';
?>