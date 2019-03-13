<?php
$connection = mysqli_connect("pdb23.awardspace.net","2495132_aats","%&uM7sU28jt");

if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

mysqli_select_db($connection,"2495132_aats");
 
$q=mysqli_query($connection,"SELECT * FROM user WHERE email = '".$_REQUEST['email']."' AND password =  '".$_REQUEST['password']."' ");
while($e=mysqli_fetch_assoc($q))
    $output[] = $e;
 
print(json_encode($output));
 
mysqli_close($connection);
?>