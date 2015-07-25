<?php

namespace dbMethods;

require_once('conf.php');


try {
    $connect = new \PDO("mysql:host=localhost;dbname=db_webservice", "root", "connection");
    $connect->setAttribute(\PDO::ATTR_ERRMODE, \PDO::ERRMODE_EXCEPTION);

    try {
        $_username = isset($_REQUEST['_username']) ? $_REQUEST['_username'] : '';
        $_password = isset($_REQUEST['_password']) ? $_REQUEST['_password'] : '';
        $_name = isset($_REQUEST['_name']) ? $_REQUEST['_name'] : '';
        $_surname = isset($_REQUEST['_surname']) ? $_REQUEST['_surname'] : '';
        $_graduated_from = isset($_REQUEST['_graduated_from']) ? $_REQUEST['_graduated_from'] : '';
        $_graduated_in = isset($_REQUEST['_graduated_in']) ? $_REQUEST['_graduated_in'] : '';
        $_born_place = isset($_REQUEST['_born_place']) ? $_REQUEST['_born_place'] : '';
        $_birthday = isset($_REQUEST['_birthday']) ? $_REQUEST['_birthday'] : '';
        $_profile_pic = isset($_REQUEST['filename']) ? $_REQUEST['filename'] : '';

        $_image = $_REQUEST['image'];
        $binary = base64_decode($_image);
        header('Content-Type: bitmap; charset=utf-8');
        $file = fopen('uploadedimages/' . $_profile_pic, 'wb');
        fwrite($file, $binary);
        fclose($file);

        $sql = "INSERT INTO users (`_username`,`_password`,`_name`,`_surname`,`_graduated_from`,`_graduated_in`,`_born_place`,`_birthday`,`_profile_pic`) VALUES ('$_username', '$_password', '$_name', '$_surname', '$_graduated_from', '$_graduated_in', '$_born_place', '$_birthday', '$_profile_pic')";
        // use exec() because no results are returned
        $connect->query($sql);

        echo "Success!";

        $connect = null;
    } catch (\Exception $e) {
        print "";
    }
} catch (\PDOException $e) {
    print "Connection error: Couldn't connect to server.\nPlease try again later.";
    die();
}
