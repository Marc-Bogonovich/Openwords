<?php
/**
 * Object represents connection to database
 *
 * @author: http://phpdao.com
 * @date: 27.11.2007
 */
class Connection2{
	private $connection;

	public function Connection2(){
		$this->connection = ConnectionFactory2::getConnection();
	}

	public function close(){
		ConnectionFactory2::close($this->connection);
	}

	/**
	 * Wykonanie zapytania sql na biezacym polaczeniu
	 *
	 * @param sql zapytanie sql
	 * @return wynik zapytania
	 */
	public function executeQuery($sql){
		return mysql_query($sql, $this->connection);
	}
}
?>