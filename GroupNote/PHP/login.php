<?php
include ('connect.php');

// log the user into the app

if ($_SERVER['REQUEST_METHOD'] == 'POST')
 {

    // set variables sent by device
     $enteredUsername = $_POST['username'];
     $enteredPassword = $_POST['password'];

     // check username exists and password matches
    $query = "SELECT COUNT(username) FROM user WHERE username = '$enteredUsername' AND password = '$enteredPassword';";
    $result = mysqli_query($conn, $query);
    $row = mysqli_fetch_array($result);
    $numOfUsers = $row['COUNT(username)'];

    // feedback array
    $users = array();

    // if user is found
    if ($numOfUsers > 0)
    {
        // get all user information
        $query = "SELECT * FROM user WHERE username = '$enteredUsername';";

        array_push($users, "true");
        //creating an statment with the query
        $stmt = $conn->prepare($query);
        //executing that statment
        $stmt->execute();
        //binding results for that statment
        $stmt->bind_result($user_id, $username, $password, $email);
        //looping through all the records
        while ($stmt->fetch())
        {

            //pushing fetched data in an array
            $userData = ['user_id' => $user_id, 'username' => $username, 'password' => $password, 'email' => $email];

            //pushing user data into feedback array
            array_push($users, $userData);
        }

        // check if user has a profile
        $user_id = ($users[1]["user_id"]);
        $query = "SELECT COUNT(profile_id) FROM profile WHERE user_id_fk = $user_id";
        $result = mysqli_query($conn, $query);
        $row = mysqli_fetch_array($result);
        $numOfProfiles = $row['COUNT(profile_id)'];
		array_push($users, $numOfProfiles);

        // if user has a profile
        if ($numOfProfiles > 0)
        {
            // retrieve profile information
            $query = "SELECT firstname, lastname, telephone FROM profile WHERE user_id_fk = '$user_id';";
            //creating an statment with the query
            $bap = $conn->prepare($query);
            //executing that statment
            $bap->execute();
            //binding results for that statment
            $bap->bind_result($firstname, $lastname, $telephone);
            //looping through all the records
            while ($bap->fetch())
            {

                //pushing fetched data in an array
                $profileData = ['firstname' => $firstname, 'lastname' => $lastname, 'telephone' => $telephone];

                //pushing the profile data into feedback array
                array_push($users, $profileData);

            }
        }
    }
    else
    {

        array_push($users, "false");
    }

    // sending feedback to device
    echo json_encode($users);

 }

?>
