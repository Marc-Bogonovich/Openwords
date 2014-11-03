<?php
/**
 * Class that operate on table 'user_db'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-07-03 10:57
 */
class UserDbMySqlDAO implements UserDbDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return UserDbMySql 
	 */
	public function load($id){
		$sql = 'SELECT * FROM user_db WHERE email = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($id);
		return $this->getRow($sqlQuery);
	}

	/**
	 * Get all records from table
	 */
	public function queryAll(){
		$sql = 'SELECT * FROM user_db';
		$sqlQuery = new SqlQuery($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
	 * Get all records from table ordered by field
	 *
	 * @param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn){
		$sql = 'SELECT * FROM user_db ORDER BY '.$orderColumn;
		$sqlQuery = new SqlQuery($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
 	 * Delete record from table
 	 * @param userDb primary key
 	 */
	public function delete($email){
		$sql = 'DELETE FROM user_db WHERE email = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($email);
		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param UserDbMySql userDb
 	 */
	public function insert($userDb){
		$sql = 'INSERT INTO user_db (id, password) VALUES (?, ?)';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->setNumber($userDb->id);
		$sqlQuery->set($userDb->password);

		$id = $this->executeInsert($sqlQuery);	
		$userDb->email = $id;
		return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param UserDbMySql userDb
 	 */
	public function update($userDb){
		$sql = 'UPDATE user_db SET id = ?, password = ? WHERE email = ?';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->setNumber($userDb->id);
		$sqlQuery->set($userDb->password);

		$sqlQuery->set($userDb->email);
		return $this->executeUpdate($sqlQuery);
	}

	/**
 	 * Delete all rows
 	 */
	public function clean(){
		$sql = 'DELETE FROM user_db';
		$sqlQuery = new SqlQuery($sql);
		return $this->executeUpdate($sqlQuery);
	}

	public function queryById($value){
		$sql = 'SELECT * FROM user_db WHERE id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByPassword($value){
		$sql = 'SELECT * FROM user_db WHERE password = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}


	public function deleteById($value){
		$sql = 'DELETE FROM user_db WHERE id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByPassword($value){
		$sql = 'DELETE FROM user_db WHERE password = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}


	
	/**
	 * Read row
	 *
	 * @return UserDbMySql 
	 */
	protected function readRow($row){
		$userDb = new UserDb();
		
		$userDb->id = $row['id'];
		$userDb->email = $row['email'];
		$userDb->password = $row['password'];

		return $userDb;
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
	 * @return UserDbMySql 
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