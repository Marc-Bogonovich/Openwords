<?php
/**
 * Class that operate on table 'word_meaning'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
class WordMeaningMySqlDAO implements WordMeaningDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return WordMeaningMySql 
	 */
	public function load($id){
		$sql = 'SELECT * FROM word_meaning WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($id);
		return $this->getRow($sqlQuery);
	}

	/**
	 * Get all records from table
	 */
	public function queryAll(){
		$sql = 'SELECT * FROM word_meaning';
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
	 * Get all records from table ordered by field
	 *
	 * @param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn){
		$sql = 'SELECT * FROM word_meaning ORDER BY '.$orderColumn;
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
 	 * Delete record from table
 	 * @param wordMeaning primary key
 	 */
	public function delete($id){
		$sql = 'DELETE FROM word_meaning WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($id);
		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param WordMeaningMySql wordMeaning
 	 */
	public function insert($wordMeaning){
		$sql = 'INSERT INTO word_meaning (word_id, type, meaning_text) VALUES (?, ?, ?)';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->setNumber($wordMeaning->wordId);
		$sqlQuery->setNumber($wordMeaning->type);
		$sqlQuery->set($wordMeaning->meaningText);

		$id = $this->executeInsert($sqlQuery);	
		$wordMeaning->id = $id;
		return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param WordMeaningMySql wordMeaning
 	 */
	public function update($wordMeaning){
		$sql = 'UPDATE word_meaning SET word_id = ?, type = ?, meaning_text = ? WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->setNumber($wordMeaning->wordId);
		$sqlQuery->setNumber($wordMeaning->type);
		$sqlQuery->set($wordMeaning->meaningText);

		$sqlQuery->setNumber($wordMeaning->id);
		return $this->executeUpdate($sqlQuery);
	}

	/**
 	 * Delete all rows
 	 */
	public function clean(){
		$sql = 'DELETE FROM word_meaning';
		$sqlQuery = new SqlQuery2($sql);
		return $this->executeUpdate($sqlQuery);
	}

	public function queryByWordId($value){
		$sql = 'SELECT * FROM word_meaning WHERE word_id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByType($value){
		$sql = 'SELECT * FROM word_meaning WHERE type = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByMeaningText($value){
		$sql = 'SELECT * FROM word_meaning WHERE meaning_text = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}


	public function deleteByWordId($value){
		$sql = 'DELETE FROM word_meaning WHERE word_id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByType($value){
		$sql = 'DELETE FROM word_meaning WHERE type = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByMeaningText($value){
		$sql = 'DELETE FROM word_meaning WHERE meaning_text = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}


	
	/**
	 * Read row
	 *
	 * @return WordMeaningMySql 
	 */
	protected function readRow($row){
		$wordMeaning = new WordMeaning();
		
		$wordMeaning->id = $row['id'];
		$wordMeaning->wordId = $row['word_id'];
		$wordMeaning->type = $row['type'];
		$wordMeaning->meaningText = $row['meaning_text'];

		return $wordMeaning;
	}
	
	protected function getList($sqlQuery){
		$tab = QueryExecutor2::execute($sqlQuery);
		$ret = array();
		for($i=0;$i<count($tab);$i++){
			$ret[$i] = $this->readRow($tab[$i]);
		}
		return $ret;
	}
	
	/**
	 * Get row
	 *
	 * @return WordMeaningMySql 
	 */
	protected function getRow($sqlQuery){
		$tab = QueryExecutor2::execute($sqlQuery);
		if(count($tab)==0){
			return null;
		}
		return $this->readRow($tab[0]);		
	}
	
	/**
	 * Execute sql query
	 */
	protected function execute($sqlQuery){
		return QueryExecutor2::execute($sqlQuery);
	}
	
		
	/**
	 * Execute sql query
	 */
	protected function executeUpdate($sqlQuery){
		return QueryExecutor2::executeUpdate($sqlQuery);
	}

	/**
	 * Query for one row and one column
	 */
	protected function querySingleResult($sqlQuery){
		return QueryExecutor2::queryForString($sqlQuery);
	}

	/**
	 * Insert row to table
	 */
	protected function executeInsert($sqlQuery){
		return QueryExecutor2::executeInsert($sqlQuery);
	}
}
?>