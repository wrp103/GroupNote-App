<?php
  
  // create, edit or delete a note
    include('connect.php');

   if ($_SERVER['REQUEST_METHOD'] == 'POST') {    
   

      // set variables sent from device
      $setting = $_POST['setting'];
      $group_id = $_POST['group_id'];
      $note_text = $_POST['note_text'];
      $note_id = $_POST['note_id'];
      $note_title = $_POST['note_title'];

      // feedback array
      $noteQueryFeedback = array();

      // create a note entry
      if ($setting == "create") {

        // insert query to add to note table
        $createNoteQuery = "INSERT INTO `groupNote`.`note` (`noteTitle`, `noteText`) VALUES ('$note_title','$note_text')";
        mysqli_query($conn,$createNoteQuery);

        // get the just added note_id
        $last_id = $conn->insert_id;

        // then insert to group_note
        $createUserNoteQuery = "INSERT INTO `groupNote`.`group_note` (`group_id_fk`, `note_id_fk`) VALUES ('$group_id', '$last_id')";
        mysqli_query($conn,$createUserNoteQuery);

        // add to feedback array
        array_push($noteQueryFeedback, $last_id);

      } elseif ($setting == "edit") {

        // edit existing note
        $editNoteQuery = "UPDATE `groupNote`.`note` SET `noteTitle` = '$note_title', `noteText` = '$note_text' WHERE (`note_id` = '$note_id')";
        mysqli_query($conn,$editNoteQuery);
   

      } elseif ($setting == "delete") {

          // delete note
          $deleteNoteQuery = "DELETE FROM `groupNote`.`group_note` WHERE (`note_id_fk` = $note_id)";
          mysqli_query($conn,$deleteNoteQuery);
          $deleteNoteQuery = "DELETE FROM `groupNote`.`note` WHERE (`note_id` = $note_id)";
          mysqli_query($conn,$deleteNoteQuery);
          array_push($noteQueryFeedback, "deleted");
          
      } elseif ($setting == "listNotes") {


          // list all notes
          $noteListQuery = "SELECT `note_id`, `noteTitle`, `noteText`
                             FROM `note`
                             JOIN `group_note` ON `note_id_fk` = `note_id`
                             WHERE `group_id_fk` = '$group_id';";

            //creating an statment with the query
            $stmt = $conn->prepare($noteListQuery);
            //executing that statment
            $stmt->execute();
            //binding results for that statment
            $stmt->bind_result($retrieved_note_id, $retrieved_note_title, $retrieved_note_text);
            //looping through all the records
            while ($stmt->fetch())
            {

                //pushing fetched data in an array
                $noteListData = ['note_id' => $retrieved_note_id, 'note_title' => $retrieved_note_title, 'note_text' => $retrieved_note_text];

                //pushing the note list inside the feedback array
                array_push($noteQueryFeedback, $noteListData);

            }

      }

      // return feedback
      echo json_encode($noteQueryFeedback);


   }   
?>  