<?php
/**
 * Class that operate on table 'learnable_language_options'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
class LearnableLanguageOptionsMySqlDAO implements LearnableLanguageOptionsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return LearnableLanguageOptionsMySql 
	 */
	public function load($l1Id, $optionL2Id){
		$sql = 'SELECT * FROM learnable_language_options WHERE l1_id = ?  AND option_l2_id = ? ';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($l1Id);
		$sqlQuery->setNumber($optionL2Id);

		return $this->getRow($sqlQuery);
	}

	/**
	 * Get all records from table
	 */
	public function queryAll(){
		$sql = 'SELECT * FROM learnable_language_options';
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
	 * Get all records from table ordered by field
	 *
	 * @param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn){
		$sql = 'SELECT * FROM learnable_language_options ORDER BY '.$orderColumn;
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
 	 * Delete record from table
 	 * @param learnableLanguageOption primary key
 	 */
	public function delete($l1Id, $optionL2Id){
		$sql = 'DELETE FROM learnable_language_options WHERE l1_id = ?  AND option_l2_id = ? ';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($l1Id);
		$sqlQuery->setNumber($optionL2Id);

		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param LearnableLanguageOptionsMySql learnableLanguageOption
 	 */
	public function insert($learnableLanguageOption){
		$sql = 'INSERT INTO learnable_language_options (option_l2_name, l1_id, option_l2_id) VALUES (?, ?, ?)';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->set($learnableLanguageOption->optionL2Name);

		
		$sqlQuery->setNumber($learnableLanguageOption->l1Id);

		$sqlQuery->setNumber($learnableLanguageOption->optionL2Id);

		$this->executeInsert($sqlQuery);	
		//$learnableLanguageOption->id = $id;
		//return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param LearnableLanguageOptionsMySql learnableLanguageOption
 	 */
	public function update($learnableLanguageOption){
		$sql = 'UPDATE learnable_language_options SET option_l2_name = ? WHERE l1_id = ?  AND option_l2_id = ? ';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->set($learnableLanguageOption->optionL2Name);

		
		$sqlQuery->setNumber($learnableLanguageOption->l1Id);

		$sqlQuery->setNumber($learnableLanguageOption->optionL2Id);

		return $this->executeUpdate($sqlQuery);
	}

	/**
 	 * Delete all rows
 	 */
	public function clean(){
		$sql = 'DELETE FROM learnable_language_options';
		$sqlQuery = new SqlQuery2($sql);
		return $this->executeUpdate($sqlQuery);
	}

	public function queryByOptionL2Name($value){
		$sql = 'SELECT * FROM learnable_language_options WHERE option_l2_name = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}


	public function deleteByOptionL2Name($value){
		$sql = 'DELETE FROM learnable_language_options WHERE option_l2_name = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}


	
	/**
	 * Read row
	 *
	 * @return LearnableLanguageOptionsMySql 
	 */
	protected function readRow($row){
		$learnableLanguageOption = new LearnableLanguageOption();
		
		$learnableLanguageOption->l1Id = $row['l1_id'];
		$learnableLanguageOption->optionL2Id = $row['option_l2_id'];
		$learnableLanguageOption->optionL2Name = $row['option_l2_name'];

		return $learnableLanguageOption;
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
	 * @return LearnableLanguageOptionsMySql 
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