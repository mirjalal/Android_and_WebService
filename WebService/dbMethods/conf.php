<?php

namespace dbMethods;


class dbActions
{
    public static $connect;

    public static function _disconnect()
    {
        self::$connect = null;
    }

    public static function _connect()
    {
        try {
            $connect = new \PDO("mysql:host=localhost;dbname=db_webservice", "root", "connection");
            $connect->setAttribute(\PDO::ATTR_EMULATE_PREPARES, false);
            $connect->setAttribute(\PDO::ATTR_ERRMODE, \PDO::ERRMODE_EXCEPTION);

            return true;
        } catch (\PDOException $e) {
            print "Error: Couldn't connect to server.";

            return false;
        }
    }




}