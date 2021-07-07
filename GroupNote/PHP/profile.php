<?php

	// create or update the users profile

	include('connect.php');

	if ($_SERVER['REQUEST_METHOD'] == 'POST') {    
   
   		// set variables sent by device
   	   $newProfile=$_POST['newProfile']; 
	   $user_id=$_POST['user_id']; 
	   $firstname =$_POST['firstname'];
	   $lastname =$_POST['lastname'];
	   $telephone =$_POST['telephone'];

	   // feedback array
	   $profileData = array();

	   if ($newProfile == "true") {
	   	// create new profile
		   $query = "INSERT INTO profile (user_id_fk, firstname, lastname, telephone)
					 SELECT * FROM (SELECT '$user_id', '$firstname', '$lastname', '$telephone') AS tmp
	                 WHERE NOT EXISTS (
	    				SELECT user_id_fk FROM profile WHERE user_id_fk = '$user_id'
					 ) LIMIT 1";

	 		mysqli_query($conn,$query);

	 		// update feedback array
	         array_push($profileData, "created");

    	} else {
    		// update existing profile

    		$query = "UPDATE `groupNote`.`profile` 
    				  SET `firstname` = '$firstname', `lastname` = '$lastname', `telephone` = '$telephone' 
    				  WHERE (`user_id_fk` = '$user_id')";

			mysqli_query($conn,$query);
				// update feedback array
			array_push($profileData, "saved");

    	}

    	// return feedback
    	echo json_encode($profileData);
     
  }

?>  