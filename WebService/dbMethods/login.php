<?php

namespace dbMethods;

require_once('conf.php');
header('Content-Type: application/json; charset=utf-8');
try {
    $connect = new \PDO("mysql:host=localhost;dbname=db_webservice", "root", "connection");
    $connect->setAttribute(\PDO::ATTR_ERRMODE, \PDO::ERRMODE_EXCEPTION);

    try {
        $_id = 0;
        $collectedResult = array();

        $_username = isset($_REQUEST['_username']) ? $_REQUEST['_username'] : 'miri';
        $_password = isset($_REQUEST['_password']) ? $_REQUEST['_password'] : '1';

        $select = "SELECT _id FROM users WHERE _username = '{$_username}' AND _password = '{$_password}'";
        foreach ($connect->query($select) as $result)
            $_id = $result[0];

        $user_data = "SELECT _id,_name,_surname,_graduated_from,_graduated_in,_born_place,_birthday,_profile_pic FROM users WHERE _id = {$_id}";

        $data = $connect->query($user_data);
        while ($row = $data->fetch(\PDO::FETCH_ASSOC)) {
            $collectedResult[] = $row;
        }

       // echo end(end($collectedResult));
//        $file_name = 'uploadedimages/' . end(end($collectedResult));
        //fopen($file_name, "rb");
//        header('Content-Type: bitmap; charset=utf-8');
//        $bin = base64_encode(fopen($file_name, "rb"));
        print base64_encode('uploadedimages/' . end(end($collectedResult)));
//        $_image = $_REQUEST['image'];
//        $binary = base64_decode($_image);
//        header('Content-Type: bitmap; charset=utf-8');
//        $file = fopen('uploadedimages/' . $_profile_pic, 'wb');
//        fwrite($file, $binary);
//        fclose($file);

        print json_encode($collectedResult, JSON_UNESCAPED_UNICODE);

        $connect = null;
    } catch (\Exception $e) {
        print $e->getMessage();
    }
} catch (\PDOException $e) {
    print "Connection error: Couldn't connect to server.\nPlease try again later.";
}
