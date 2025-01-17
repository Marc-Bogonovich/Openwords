<?php
/**
 * Object executes sql queries
 *
 * @author: http://phpdao.com
 * @date: 27.11.2007
 */
class QueryExecutor2{

	/**
	 * Wykonaniew zapytania do bazy
	 *
	 * @param sqlQuery obiekt typu SqlQuery
	 * @return wynik zapytania 
	 */
	public static function execute($sqlQuery){
		$transaction = Transaction2::getCurrentTransaction();
		if(!$transaction){
			$connection = new Connection2();
		}else{
			$connection = $transaction->getConnection();
		}		
		$query = $sqlQuery->getQuery();
//                $pos = strpos($query, "null");
//                if ($pos != false) {
//                    if(true) {
//                      writeToFile($query);
//                    }
//                }
		$result = $connection->executeQuery($query);
		if(!$result){
			throw new Exception("SQL Error: -->".$query."<--" . mysql_error());
		}
		$i=0;
		$tab = array();
		while ($row = mysql_fetch_array($result)){
			$tab[$i++] = $row;
		}
		mysql_free_result($result);
		if(!$transaction){
			$connection->close();
		}
		return $tab;
	}
	
	
	public static function executeUpdate($sqlQuery){
		$transaction = Transaction2::getCurrentTransaction();
		if(!$transaction){
			$connection = new Connection2();
		}else{
			$connection = $transaction->getConnection();
		}		
		$query = $sqlQuery->getQuery();
		$result = $connection->executeQuery($query);
		if(!$result){
			throw new Exception("SQL Error: -->".$query."<--" . mysql_error());
		}
		return mysql_affected_rows();		
	}

	public static function executeInsert($sqlQuery){
		QueryExecutor2::executeUpdate($sqlQuery);
		return mysql_insert_id();
	}
	
	/**
	 * Wykonaniew zapytania do bazy
	 *
	 * @param sqlQuery obiekt typu SqlQuery
	 * @return wynik zapytania 
	 */
	public static function queryForString($sqlQuery){
		$transaction = Transaction2::getCurrentTransaction();
		if(!$transaction){
			$connection = new Connection2();
		}else{
			$connection = $transaction->getConnection();
		}
		$result = $connection->executeQuery($sqlQuery->getQuery());
		if(!$result){
			throw new Exception("SQL Error: -->".$sqlQuery->getQuery()."<--" . mysql_error());
		}
		$row = mysql_fetch_array($result);		
		return $row[0];
	}

}
?>