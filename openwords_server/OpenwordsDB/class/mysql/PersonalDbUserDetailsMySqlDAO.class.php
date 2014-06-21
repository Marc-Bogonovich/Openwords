<?php
/**
 * Class that operate on table 'personal_db_user_details'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
class PersonalDbUserDetailsMySqlDAO implements PersonalDbUserDetailsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return PersonalDbUserDetailsMySql 
	 */
	public function load($id){
		$sql = 'SELECT * FROM personal_db_user_details WHERE user_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($id);
		return $this->getRow($sqlQuery);
	}

	/**
	 * Get all records from table
	 */
	public function queryAll(){
		$sql = 'SELECT * FROM personal_db_user_details';
		$sqlQuery = new SqlQuery($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
	 * Get all records from table ordered by field
	 *
	 * @param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn){
		$sql = 'SELECT * FROM personal_db_user_details ORDER BY '.$orderColumn;
		$sqlQuery = new SqlQuery($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
 	 * Delete record from table
 	 * @param personalDbUserDetail primary key
 	 */
	public function delete($user_id){
		$sql = 'DELETE FROM personal_db_user_details WHERE user_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($user_id);
		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param PersonalDbUserDetailsMySql personalDbUserDetail
 	 */
	public function insert($personalDbUserDetail){
		$sql = 'INSERT INTO personal_db_user_details (about_u1, about_u2, l1_pref_id) VALUES (?, ?, ?)';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->set($personalDbUserDetail->aboutU1);
		$sqlQuery->set($personalDbUserDetail->aboutU2);
		$sqlQuery->setNumber($personalDbUserDetail->l1PrefId);

		$id = $this->executeInsert($sqlQuery);	
		$personalDbUserDetail->userId = $id;
		return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param PersonalDbUserDetailsMySql personalDbUserDetail
 	 */
	public function update($personalDbUserDetail){
		$sql = 'UPDATE personal_db_user_details SET about_u1 = ?, about_u2 = ?, l1_pref_id = ? WHERE user_id = ?';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->set($personalDbUserDetail->aboutU1);
		$sqlQuery->set($personalDbUserDetail->aboutU2);
		$sqlQuery->setNumber($personalDbUserDetail->l1PrefId);

		$sqlQuery->setNumber($personalDbUserDetail->userId);
		return $this->executeUpdate($sqlQuery);
	}

	/**
 	 * Delete all rows
 	 */
	public function clean(){
		$sql = 'DELETE FROM personal_db_user_details';
		$sqlQuery = new SqlQuery($sql);
		return $this->executeUpdate($sqlQuery);
	}

	public function queryByAboutU1($value){
		$sql = 'SELECT * FROM personal_db_user_details WHERE about_u1 = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}

	public function queryByAboutU2($value){
		$sql = 'SELECT * FROM personal_db_user_details WHERE about_u2 = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}

	public function queryByL1PrefId($value){
		$sql = 'SELECT * FROM personal_db_user_details WHERE l1_pref_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}


	public function deleteByAboutU1($value){
		$sql = 'DELETE FROM personal_db_user_details WHERE about_u1 = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByAboutU2($value){
		$sql = 'DELETE FROM personal_db_user_details WHERE about_u2 = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByL1PrefId($value){
		$sql = 'DELETE FROM personal_db_user_details WHERE l1_pref_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}


	
	/**
	 * Read row
	 *
	 * @return PersonalDbUserDetailsMySql 
	 */
	protected function readRow($row){
		$personalDbUserDetail = new PersonalDbUserDetail();
		
		$personalDbUserDetail->userId = $row['user_id'];
		$personalDbUserDetail->aboutU1 = $row['about_u1'];
		$personalDbUserDetail->aboutU2 = $row['about_u2'];
		$personalDbUserDetail->l1PrefId = $row['l1_pref_id'];

		return $personalDbUserDetail;
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
	 * @return PersonalDbUserDetailsMySql 
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