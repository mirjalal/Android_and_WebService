<?php

namespace dbMethods;

header('Content-Type:text/html; charset=utf-8');

try {
    $connect = new \PDO("mysql:host=localhost;dbname=db_webservice", "root", "connection");
    $connect->setAttribute(\PDO::ATTR_ERRMODE, \PDO::ERRMODE_EXCEPTION);

    try {
        $_id = 0;
        $collectedResult = array();

        $_username = isset($_REQUEST['_username']) ? $_REQUEST['_username'] : '1';
        $_password = isset($_REQUEST['_password']) ? $_REQUEST['_password'] : '2';

        $select = "SELECT _id FROM users WHERE _username = '{$_username}' AND _password = '{$_password}'";
        foreach ($connect->query($select) as $result)
            $_id = $result[0];

        if ($_id > 0) {
            $user_data = "SELECT _name,_surname,_graduated_from,_graduated_in,_born_place,_birthday FROM users WHERE _id = {$_id}";

            $data = $connect->query($user_data);
            while ($row = $data->fetch(\PDO::FETCH_ASSOC)) {
                $collectedResult[] = $row;
            }

//            $file = 'uploadedimages/' . end(end($collectedResult));
//            if (file_exists($file)) {
//                $data = base64_encode(file_get_contents($file));
//            } else
//                echo 'No such file';

            //        echo file_get_contents($file);
        //    echo base64_encode(end(end($collectedResult)));
//            echo '<img src="data:image/jpeg;base64,'.end(end($collectedResult)).'">';
            echo json_encode($collectedResult, JSON_UNESCAPED_UNICODE);
        } else
            echo "Username or password is incorrect";

        $connect = null;
    } catch (\Exception $e) {
        print $e->getMessage();
    }
} catch (\PDOException $e) {
    print "Connection error: Couldn't connect to server.\nPlease try again later.";
}
