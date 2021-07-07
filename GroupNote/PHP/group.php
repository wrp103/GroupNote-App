<?php

  // create or update a group
    include('connect.php');

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {    
   
    // set the variables sent by the user
     $user_id = $_POST['user_id'];
     $group_name = $_POST['group_name']; 
     $setting = $_POST['setting'];
     $add_username = $_POST['add_username'];
     $group_id = $_POST['group_id'];

     // declare the feedback to return to the device
     $groupQueryFeedback = array();

           
      //create a group entry
      if ($setting == "create") {

        //insert query to add to group table
        $createGroupQuery = "INSERT INTO `groupNote`.`group` (`groupName`) VALUES ('$group_name')";
        mysqli_query($conn,$createGroupQuery);

        //get the just added group_id
        $last_id = $conn->insert_id;

        //then insert to group_user with an insert query using user_id and group_id
        $createUserGroupQuery = "INSERT INTO `groupNote`.`group_user` (`user_id_fk`, `group_id_fk`) VALUES ('$user_id', '$last_id')";
        mysqli_query($conn,$createUserGroupQuery);

        array_push($groupQueryFeedback, $last_id);

      } elseif ($setting == "addUser") {

        // query if username exists
        $checkUser = "SELECT user_id FROM `groupNote`.`user` WHERE username = '$add_username'";
        $result=mysqli_query($conn,$checkUser);
        $row = mysqli_fetch_array ($result);
        $user_id =  ($row[user_id]);


        if ($user_id != null) {

          // query if user is already in the group
          $checkGroup = "SELECT group_user_id FROM group_user WHERE user_id_fk = '$user_id' AND group_id_fk = '$group_id'";
          $result=mysqli_query($conn,$checkGroup);
          $row = mysqli_fetch_array ($result);
          $group_check = ($row[group_user_id]);

          if ($group_check != null) {

            
            array_push($groupQueryFeedback, "user already in group");

          } else { 

            //insert query to add to group table
            $addUserQuery = "INSERT INTO `groupNote`.`group_user` (`user_id_fk`, `group_id_fk`) VALUES ('$user_id', '$group_id')";

            mysqli_query($conn,$addUserQuery);

            //add user to group
            array_push($groupQueryFeedback, "user added");

          }
          
        } else {
          array_push($groupQueryFeedback, "unknown username");
        }

      } elseif ($setting == "listGroups") {

          // select groups the user belongs to
          $groupListQuery = "SELECT `group_id`, `groupName`
                             FROM `group`
                             JOIN `group_user` ON `group_id_fk` = `group_id`
                             WHERE `user_id_fk` = '$user_id';";

            //creating an statment with the query
            $stmt = $conn->prepare($groupListQuery);
            //executing that statment
            $stmt->execute();
            //binding results for that statment
            $stmt->bind_result($retrieved_group_id, $retrieved_group_name);
            //looping through all the records
            while ($stmt->fetch())
            {

                //pushing fetched data in an array
                $groupListData = ['group_id' => $retrieved_group_id, 'group_name' => $retrieved_group_name];

                //pushing group list inside the feedback array
                array_push($groupQueryFeedback, $groupListData);
            }
      }

      // send feedback
      echo json_encode($groupQueryFeedback);


   }   
?>  