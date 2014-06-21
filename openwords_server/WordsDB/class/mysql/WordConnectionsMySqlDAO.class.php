<?php
/**
 * Class that operate on table 'word_connections'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
class WordConnectionsMySqlDAO implements WordConnectionsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return WordConnectionsMySql 
	 */
	public function load($id){
		$sql = 'SELECT * FROM word_connections WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($id);
		return $this->getRow($sqlQuery);
	}

	/**
	 * Get all records from table
	 */
	public function queryAll(){
		$sql = 'SELECT * FROM word_connections';
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
	 * Get all records from table ordered by field
	 *
	 * @param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn){
		$sql = 'SELECT * FROM word_connections ORDER BY '.$orderColumn;
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
 	 * Delete record from table
 	 * @param wordConnection primary key
 	 */
	public function delete($id){
		$sql = 'DELETE FROM word_connections WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($id);
		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param WordConnectionsMySql wordConnection
 	 */
	public function insert($wordConnection){
		$sql = 'INSERT INTO word_connections (word1_id, word2_id, connection_type) VALUES (?, ?, ?)';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->setNumber($wordConnection->word1Id);
		$sqlQuery->setNumber($wordConnection->word2Id);
		$sqlQuery->set($wordConnection->connectionType);

		$id = $this->executeInsert($sqlQuery);	
		$wordConnection->id = $id;
		return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param WordConnectionsMySql wordConnection
 	 */
	public function update($wordConnection){
		$sql = 'UPDATE word_connections SET word1_id = ?, word2_id = ?, connection_type = ? WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->setNumber($wordConnection->word1Id);
		$sqlQuery->setNumber($wordConnection->word2Id);
		$sqlQuery->set($wordConnection->connectionType);

		$sqlQuery->setNumber($wordConnection->id);
		return $this->executeUpdate($sqlQuery);
	}

	/**
 	 * Delete all rows
 	 */
	public function clean(){
		$sql = 'DELETE FROM word_connections';
		$sqlQuery = new SqlQuery2($sql);
		return $this->executeUpdate($sqlQuery);
	}

	public function queryByWord1Id($value){
		$sql = 'SELECT * FROM word_connections WHERE word1_id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByWord2Id($value){
		$sql = 'SELECT * FROM word_connections WHERE word2_id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByConnectionType($value){
		$sql = 'SELECT * FROM word_connections WHERE connection_type = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}


	public function deleteByWord1Id($value){
		$sql = 'DELETE FROM word_connections WHERE word1_id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByWord2Id($value){
		$sql = 'DELETE FROM word_connections WHERE word2_id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByConnectionType($value){
		$sql = 'DELETE FROM word_connections WHERE connection_type = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}


	
	/**
	 * Read row
	 *
	 * @return WordConnectionsMySql 
	 */
	protected function readRow($row){
		$wordConnection = new WordConnection();
		
		$wordConnection->id = $row['id'];
		$wordConnection->word1Id = $row['word1_id'];
		$wordConnection->word2Id = $row['word2_id'];
		$wordConnection->connectionType = $row['connection_type'];

		return $wordConnection;
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
	 * @return WordConnectionsMySql 
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