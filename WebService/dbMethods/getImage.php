<?php
include 'login_class.php';


session_start();
try {
    $collectedResult = array();
    $connect = new \PDO("mysql:host=45.35.4.29;dbname=db_webservice", "root", "connection");
    $connect->setAttribute(\PDO::ATTR_EMULATE_PREPARES, false);
    $connect->setAttribute(\PDO::ATTR_ERRMODE, \PDO::ERRMODE_EXCEPTION);
    $user_data = "SELECT `_profile_pic` FROM users WHERE _id = {$_SESSION["_id"]}";
    $data = $connect->query($user_data);
    while ($row = $data->fetch(\PDO::FETCH_ASSOC)) {
        $collectedResult[] = $row;
    }

    $filename = basename(end(end($collectedResult)));
    $file_extension = strtolower(substr(strrchr($filename, "."), 1));

    switch ($file_extension) {
        case "gif":
            $ctype = "image/gif";
            break;
        case "png":
            $ctype = "image/png";
            break;
        case "jpeg":
            $ctype = "image/jpeg";
            break;
        case "jpg":
            $ctype = "image/jpg";
            break;
        default:
            break;
    }

    header('Content-type: ' . $ctype);
    echo $ctype;

    echo '<img src="uploads/' . end(end($collectedResult)) . '">';
} catch (\PDOException $e) {
    //  print "Connection error: Couldn't connect to server.\nPlease try again later.";
}

unset($_SESSION["_id"]);