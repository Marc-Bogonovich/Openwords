<?php
/**
 * Class that operate on table 'word_transcription'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
class WordTranscriptionMySqlDAO implements WordTranscriptionDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return WordTranscriptionMySql 
	 */
	public function load($id){
		$sql = 'SELECT * FROM word_transcription WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($id);
		return $this->getRow($sqlQuery);
	}

	/**
	 * Get all records from table
	 */
	public function queryAll(){
		$sql = 'SELECT * FROM word_transcription';
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
	 * Get all records from table ordered by field
	 *
	 * @param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn){
		$sql = 'SELECT * FROM word_transcription ORDER BY '.$orderColumn;
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
 	 * Delete record from table
 	 * @param wordTranscription primary key
 	 */
	public function delete($id){
		$sql = 'DELETE FROM word_transcription WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($id);
		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param WordTranscriptionMySql wordTranscription
 	 */
	public function insert($wordTranscription){
		$sql = 'INSERT INTO word_transcription (word_id, transcription, transcription_type) VALUES (?, ?, ?)';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->setNumber($wordTranscription->wordId);
		$sqlQuery->set($wordTranscription->transcription);
		$sqlQuery->setNumber($wordTranscription->transcriptionType);

		$id = $this->executeInsert($sqlQuery);	
		$wordTranscription->id = $id;
		return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param WordTranscriptionMySql wordTranscription
 	 */
	public function update($wordTranscription){
		$sql = 'UPDATE word_transcription SET word_id = ?, transcription = ?, transcription_type = ? WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->setNumber($wordTranscription->wordId);
		$sqlQuery->set($wordTranscription->transcription);
		$sqlQuery->setNumber($wordTranscription->transcriptionType);

		$sqlQuery->setNumber($wordTranscription->id);
		return $this->executeUpdate($sqlQuery);
	}

	/**
 	 * Delete all rows
 	 */
	public function clean(){
		$sql = 'DELETE FROM word_transcription';
		$sqlQuery = new SqlQuery2($sql);
		return $this->executeUpdate($sqlQuery);
	}

	public function queryByWordId($value){
		$sql = 'SELECT * FROM word_transcription WHERE word_id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByTranscription($value){
		$sql = 'SELECT * FROM word_transcription WHERE transcription = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}

	public function queryByTranscriptionType($value){
		$sql = 'SELECT * FROM word_transcription WHERE transcription_type = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}


	public function deleteByWordId($value){
		$sql = 'DELETE FROM word_transcription WHERE word_id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByTranscription($value){
		$sql = 'DELETE FROM word_transcription WHERE transcription = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByTranscriptionType($value){
		$sql = 'DELETE FROM word_transcription WHERE transcription_type = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}


	
	/**
	 * Read row
	 *
	 * @return WordTranscriptionMySql 
	 */
	protected function readRow($row){
		$wordTranscription = new WordTranscription();
		
		$wordTranscription->id = $row['id'];
		$wordTranscription->wordId = $row['word_id'];
		$wordTranscription->transcription = $row['transcription'];
		$wordTranscription->transcriptionType = $row['transcription_type'];

		return $wordTranscription;
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
	 * @return WordTranscriptionMySql 
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