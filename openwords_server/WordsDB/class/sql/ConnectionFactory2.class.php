<?php
/*
 * Class return connection to database
 *
 * @author: http://phpdao.com
 * @date: 27.11.2007
 */
class ConnectionFactory2{
	
	/**
	 * Zwrocenie polaczenia
	 *
	 * @return polaczenie
	 */
	static public function getConnection(){
		$conn = mysql_connect(ConnectionProperty2::getHost(), ConnectionProperty2::getUser(), ConnectionProperty2::getPassword());
		mysql_select_db(ConnectionProperty2::getDatabase());
		if(!$conn){
			throw new Exception('could not connect to database');
		}
		return $conn;
	}

	/**
	 * Zamkniecie polaczenia
	 *
	 * @param connection polaczenie do bazy
	 */
	static public function close($connection){
		mysql_close($connection);
	}
}
?>