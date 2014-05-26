<?php
/**
 * Database transaction
 *
 * @author: http://phpdao.com
 * @date: 27.11.2007
 */
class Transaction2{
	private static $transactions;

	private $connection;

	public function Transaction2(){
		$this->connection = new Connection2();
		if(!Transaction2::$transactions){
			Transaction2::$transactions = new ArrayList2();
		}
		Transaction2::$transactions->add($this);
		$this->connection->executeQuery('BEGIN');
	}

	/**
	 * Zakonczenie transakcji i zapisanie zmian
	 */
	public function commit(){
		$this->connection->executeQuery('COMMIT');
		$this->connection->close();
		Transaction2::$transactions->removeLast();
	}

	/**
	 * Zakonczenie transakcji i wycofanie zmian
	 */
	public function rollback(){
		$this->connection->executeQuery('ROLLBACK');
		$this->connection->close();
		Transaction2::$transactions->removeLast();
	}

	/**
	 * Pobranie polaczenia dla obencej transakcji
	 *
	 * @return polazenie do bazy
	 */
	public function getConnection(){
		return $this->connection;
	}

	/**
	 * Zwraca obecna transakcje
	 *
	 * @return transkacja
	 */
	public static function getCurrentTransaction(){
		if(Transaction2::$transactions){
			$tran = Transaction2::$transactions->getLast();
			return $tran;
		}
		return;
	}
}
?>