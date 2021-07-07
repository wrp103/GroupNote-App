
<?php
// connect to the database
$db_name="groupNote";
$mysql_username="root";
$mysql_password="root";
$server_name="192.168.0.12";
$conn = mysqli_connect($server_name,$mysql_username,$mysql_password,$db_name);

// debugging check
/*
if($conn)
{
  echo "Connection Success";
}
else
{
  echo "Connection Failed";
}
*/
?>