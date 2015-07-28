<?php

namespace dbMethods;


//header('Content-Type:text/html; charset=utf-8');

class Login
{
    public static function Login()
    {
        try {
            $connect = new \PDO("mysql:host=45.35.4.29;dbname=db_webservice", "root", "connection");
            $connect->setAttribute(\PDO::ATTR_EMULATE_PREPARES, false);
            $connect->setAttribute(\PDO::ATTR_ERRMODE, \PDO::ERRMODE_EXCEPTION);

            try {
                $_id = 0;
                $_status = 0;
                $collectedResult = array();

                $_username = isset($_REQUEST['_username']) ? $_REQUEST['_username'] : '1';
                $_password = isset($_REQUEST['_password']) ? $_REQUEST['_password'] : '1';

                $select = "SELECT _id, _status FROM users WHERE _username = '{$_username}' AND _password = '{$_password}'";
                foreach ($connect->query($select) as $result) {
                    $_id = $result[0];
                    $_status = $result[1];
                }


                if ($_id > 0 && $_status == 1) {
                    $user_data = "SELECT `_name`, `_surname`, `_graduated_from`, `_graduated_in`, `_born_place`, `_birthday` FROM users WHERE _id = {$_id}";

                    session_start();
                    $_SESSION["_id"] = $_id;

                    $data = $connect->query($user_data);
                    while ($row = $data->fetch(\PDO::FETCH_ASSOC)) {
                        $collectedResult[] = $row;
                    }

                    echo json_encode($collectedResult, JSON_UNESCAPED_UNICODE);
                } else
                    echo "Username or password is wrong";

            } catch (\Exception $e) {
                print $e->getMessage();
            }
        } catch (\PDOException $e) {
            print "Connection error: Couldn't connect to server.\nPlease try again later.";
        }
    }
}
