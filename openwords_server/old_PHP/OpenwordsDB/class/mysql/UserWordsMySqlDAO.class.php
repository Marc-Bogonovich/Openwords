<?php
/**
 * Class that operate on table 'user_words'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-07-03 10:57
 */
class UserWordsMySqlDAO implements UserWordsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return UserWordsMySql 
	 */
	public function load($userId, $connectionId){
		$sql = 'SELECT * FROM user_words WHERE user_id = ?  AND connection_id = ? ';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($userId);
		$sqlQuery->setNumber($connectionId);

		return $this->getRow($sqlQuery);
	}

	/**
	 * Get all records from table
	 */
	public function queryAll(){
		$sql = 'SELECT * FROM user_words';
		$sqlQuery = new SqlQuery($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
	 * Get all records from table ordered by field
	 *
	 * @param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn){
		$sql = 'SELECT * FROM user_words ORDER BY '.$orderColumn;
		$sqlQuery = new SqlQuery($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
 	 * Delete record from table
 	 * @param userWord primary key
 	 */
	public function delete($userId, $connectionId){
		$sql = 'DELETE FROM user_words WHERE user_id = ?  AND connection_id = ? ';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($userId);
		$sqlQuery->setNumber($connectionId);

		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param UserWordsMySql userWord
 	 */
	public function insert($userWord){
		$sql = 'INSERT INTO user_words (wordl2_id, wordl1_id, l2_id, download_time, fresh, user_id, connection_id) VALUES (?, ?, ?, ?, ?, ?, ?)';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->setNumber($userWord->wordl2Id);
		$sqlQuery->setNumber($userWord->wordl1Id);
		$sqlQuery->setNumber($userWord->l2Id);
		$sqlQuery->setNumber($userWord->downloadTime);
		$sqlQuery->setNumber($userWord->fresh);
		
		$sqlQuery->setNumber($userWord->userId);

		$sqlQuery->setNumber($userWord->connectionId);

		$this->executeInsert($sqlQuery);	
		//$userWord->id = $id;
		//return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param UserWordsMySql userWord
 	 */
	public function update($userWord){
		$sql = 'UPDATE user_words SET wordl2_id = ?, wordl1_id = ?, l2_id = ?, download_time = ?, fresh = ? WHERE user_id = ?  AND connection_id = ? ';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->setNumber($userWord->wordl2Id);
		$sqlQuery->setNumber($userWord->wordl1Id);
		$sqlQuery->setNumber($userWord->l2Id);
		$sqlQuery->setNumber($userWord->downloadTime);
		$sqlQuery->setNumber($userWord->fresh);

		
		$sqlQuery->setNumber($userWord->userId);

		$sqlQuery->setNumber($userWord->connectionId);

		return $this->executeUpdate($sqlQuery);
	}

	/**
 	 * Delete all rows
 	 */
	public function clean(){
		$sql = 'DELETE FROM user_words';
		$sqlQuery = new SqlQuery($sql);
		return $this->executeUpdate($sqlQuery);
	}

	public function queryByWordl2Id($value){
		$sql = 'SELECT * FROM user_words WHERE wordl2_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByWordl1Id($value){
		$sql = 'SELECT * FROM user_words WHERE wordl1_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByL2Id($value){
		$sql = 'SELECT * FROM user_words WHERE l2_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByDownloadTime($value){
		$sql = 'SELECT * FROM user_words WHERE download_time = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByFresh($value){
		$sql = 'SELECT * FROM user_words WHERE fresh = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}


	public function deleteByWordl2Id($value){
		$sql = 'DELETE FROM user_words WHERE wordl2_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByWordl1Id($value){
		$sql = 'DELETE FROM user_words WHERE wordl1_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByL2Id($value){
		$sql = 'DELETE FROM user_words WHERE l2_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByDownloadTime($value){
		$sql = 'DELETE FROM user_words WHERE download_time = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByFresh($value){
		$sql = 'DELETE FROM user_words WHERE fresh = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}


	
	/**
	 * Read row
	 *
	 * @return UserWordsMySql 
	 */
	protected function readRow($row){
		$userWord = new UserWord();
		
		$userWord->userId = $row['user_id'];
		$userWord->connectionId = $row['connection_id'];
		$userWord->wordl2Id = $row['wordl2_id'];
		$userWord->wordl1Id = $row['wordl1_id'];
		$userWord->l2Id = $row['l2_id'];
		$userWord->downloadTime = $row['download_time'];
		$userWord->fresh = $row['fresh'];

		return $userWord;
	}
	
	protected function getList($sqlQuery){
		$tab = QueryExecutor::execute($sqlQuery);
		$ret = array();
		for($i=0;$i<count($tab);$i++){
			$ret[$i] = $this->readRow($tab[$i]);
		}
		return $ret;
	}
	
	/**
	 * Get row
	 *
	 * @return UserWordsMySql 
	 */
	protected function getRow($sqlQuery){
		$tab = QueryExecutor::execute($sqlQuery);
		if(count($tab)==0){
			return null;
		}
		return $this->readRow($tab[0]);		
	}
	
	/**
	 * Execute sql query
	 */
	protected function execute($sqlQuery){
		return QueryExecutor::execute($sqlQuery);
	}
	
		
	/**
	 * Execute sql query
	 */
	protected function executeUpdate($sqlQuery){
		return QueryExecutor::executeUpdate($sqlQuery);
	}

	/**
	 * Query for one row and one column
	 */
	protected function querySingleResult($sqlQuery){
		return QueryExecutor::queryForString($sqlQuery);
	}

	/**
	 * Insert row to table
	 */
	protected function executeInsert($sqlQuery){
		return QueryExecutor::executeInsert($sqlQuery);
	}
}
?>