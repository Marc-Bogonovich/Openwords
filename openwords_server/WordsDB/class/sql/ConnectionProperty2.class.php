<?php
/**
 * Connection properties
 *
 * @author: http://phpdao.com
 * @date: 27.11.2007
 */
class ConnectionProperty2{
	private static $host = 'localhost';
	private static $user = 'oworg';
	private static $password = '$Neander456';
	private static $database = 'oworg_owr_1_0';

	public static function getHost(){
		return ConnectionProperty2::$host;
	}

	public static function getUser(){
		return ConnectionProperty2::$user;
	}

	public static function getPassword(){
		return ConnectionProperty2::$password;
	}

	public static function getDatabase(){
		return ConnectionProperty2::$database;
	}
}
?>