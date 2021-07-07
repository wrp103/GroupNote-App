<?php

    // create a new account
    include('connect.php');

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {    
   
    // set variables sent from device
      $username=$_POST['username']; 
      $password=$_POST['password'];
      $email=$_POST['email'];

      // feedback array
      $createFeedback = array();

      // check if username exists
      $query="SELECT COUNT(username) FROM user WHERE username = '$username';";
      $result=mysqli_query($conn,$query);
      $row = mysqli_fetch_array ($result);
      $numOfUsers = $row['COUNT(username)'];

      if ($numOfUsers > 0) {

        // already account with that username
        // update feedback array
        array_push($createFeedback, "false");


      } else {

          // create user
            $query = "INSERT INTO `groupNote`.`user` (`username`, `password`, `email`) VALUES ('$username', '$password', '$email')";
            mysqli_query($conn,$query);
            $last_id = $conn->insert_id;
            // update feedback array
            array_push($createFeedback, $last_id);

      }

      // return feedback array
      echo json_encode($createFeedback);
    }   
?>  