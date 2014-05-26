<?php
/**
 * Class that operate on table 'personal_db_language_pref'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
class PersonalDbLanguagePrefMySqlDAO implements PersonalDbLanguagePrefDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return PersonalDbLanguagePrefMySql 
	 */
	public function load($userId, $l2PrefId){
		$sql = 'SELECT * FROM personal_db_language_pref WHERE user_id = ?  AND l2_pref_id = ? ';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($userId);
		$sqlQuery->setNumber($l2PrefId);

		return $this->getRow($sqlQuery);
	}

	/**
	 * Get all records from table
	 */
	public function queryAll(){
		$sql = 'SELECT * FROM personal_db_language_pref';
		$sqlQuery = new SqlQuery($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
	 * Get all records from table ordered by field
	 *
	 * @param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn){
		$sql = 'SELECT * FROM personal_db_language_pref ORDER BY '.$orderColumn;
		$sqlQuery = new SqlQuery($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
 	 * Delete record from table
 	 * @param personalDbLanguagePref primary key
 	 */
	public function delete($userId, $l2PrefId){
		$sql = 'DELETE FROM personal_db_language_pref WHERE user_id = ?  AND l2_pref_id = ? ';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($userId);
		$sqlQuery->setNumber($l2PrefId);

		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param PersonalDbLanguagePrefMySql personalDbLanguagePref
 	 */
	public function insert($personalDbLanguagePref){
		$sql = 'INSERT INTO personal_db_language_pref (l2_pref_name, user_id, l2_pref_id) VALUES (?, ?, ?)';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->set($personalDbLanguagePref->l2PrefName);

		
		$sqlQuery->setNumber($personalDbLanguagePref->userId);

		$sqlQuery->setNumber($personalDbLanguagePref->l2PrefId);

		$this->executeInsert($sqlQuery);	
		//$personalDbLanguagePref->id = $id;
		//return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param PersonalDbLanguagePrefMySql personalDbLanguagePref
 	 */
	public function update($personalDbLanguagePref){
		$sql = 'UPDATE personal_db_language_pref SET l2_pref_name = ? WHERE user_id = ?  AND l2_pref_id = ? ';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->set($personalDbLanguagePref->l2PrefName);

		
		$sqlQuery->setNumber($personalDbLanguagePref->userId);

		$sqlQuery->setNumber($personalDbLanguagePref->l2PrefId);

		return $this->executeUpdate($sqlQuery);
	}

	/**
 	 * Delete all rows
 	 */
	public function clean(){
		$sql = 'DELETE FROM personal_db_language_pref';
		$sqlQuery = new SqlQuery($sql);
		return $this->executeUpdate($sqlQuery);
	}

	public function queryByL2PrefName($value){
		$sql = 'SELECT * FROM personal_db_language_pref WHERE l2_pref_name = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}


	public function deleteByL2PrefName($value){
		$sql = 'DELETE FROM personal_db_language_pref WHERE l2_pref_name = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}


	
	/**
	 * Read row
	 *
	 * @return PersonalDbLanguagePrefMySql 
	 */
	protected function readRow($row){
		$personalDbLanguagePref = new PersonalDbLanguagePref();
		
		$personalDbLanguagePref->userId = $row['user_id'];
		$personalDbLanguagePref->l2PrefId = $row['l2_pref_id'];
		$personalDbLanguagePref->l2PrefName = $row['l2_pref_name'];

		return $personalDbLanguagePref;
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
	 * @return PersonalDbLanguagePrefMySql 
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