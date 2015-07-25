<?php

namespace dbMethods;


class Connect
{
    public static $connect;

    public static function getConnected()
    {
        try {
            $connect = new \PDO("mysql:host=localhost;dbname=db_webservice", "root", "connection");
            $connect->setAttribute(\PDO::ATTR_ERRMODE, \PDO::ERRMODE_EXCEPTION);
        } catch (\PDOException $e) {
            print "Error: Couldn't connect to server.";
            die();
        }
    }
}