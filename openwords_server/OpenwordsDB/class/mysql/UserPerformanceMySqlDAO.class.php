<?php
/**
 * Class that operate on table 'user_performance'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-07-03 10:57
 */
class UserPerformanceMySqlDAO implements UserPerformanceDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return UserPerformanceMySql 
	 */
	public function load($id){
		$sql = 'SELECT * FROM user_performance WHERE id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($id);
		return $this->getRow($sqlQuery);
	}

	/**
	 * Get all records from table
	 */
	public function queryAll(){
		$sql = 'SELECT * FROM user_performance';
		$sqlQuery = new SqlQuery($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
	 * Get all records from table ordered by field
	 *
	 * @param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn){
		$sql = 'SELECT * FROM user_performance ORDER BY '.$orderColumn;
		$sqlQuery = new SqlQuery($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
 	 * Delete record from table
 	 * @param userPerformance primary key
 	 */
	public function delete($id){
		$sql = 'DELETE FROM user_performance WHERE id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($id);
		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param UserPerformanceMySql userPerformance
 	 */
	public function insert($userPerformance){
		$sql = 'INSERT INTO user_performance (user_id, connection_id, type, performance, time, user_exclude) VALUES (?, ?, ?, ?, ?, ?)';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->setNumber($userPerformance->userId);
		$sqlQuery->setNumber($userPerformance->connectionId);
		$sqlQuery->setNumber($userPerformance->type);
		$sqlQuery->setNumber($userPerformance->performance);
		$sqlQuery->setNumber($userPerformance->time);
		$sqlQuery->setNumber($userPerformance->userExclude);

		$id = $this->executeInsert($sqlQuery);	
		//$userPerformance->id = $id;
		return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param UserPerformanceMySql userPerformance
 	 */
	public function update($userPerformance){
		$sql = 'UPDATE user_performance SET user_id = ?, connection_id = ?, type = ?, performance = ?, time = ?, user_exclude = ? WHERE id = ?';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->setNumber($userPerformance->userId);
		$sqlQuery->setNumber($userPerformance->connectionId);
		$sqlQuery->setNumber($userPerformance->type);
		$sqlQuery->setNumber($userPerformance->performance);
		$sqlQuery->set($userPerformance->time);
		$sqlQuery->setNumber($userPerformance->userExclude);

		$sqlQuery->setNumber($userPerformance->id);
		return $this->executeUpdate($sqlQuery);
	}

	/**
 	 * Delete all rows
 	 */
	public function clean(){
		$sql = 'DELETE FROM user_performance';
		$sqlQuery = new SqlQuery($sql);
		return $this->executeUpdate($sqlQuery);
	}

	public function queryByUserId($value){
		$sql = 'SELECT * FROM user_performance WHERE user_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByConnectionId($value){
		$sql = 'SELECT * FROM user_performance WHERE connection_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByType($value){
		$sql = 'SELECT * FROM user_performance WHERE type = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByPerformance($value){
		$sql = 'SELECT * FROM user_performance WHERE performance = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByTime($value){
		$sql = 'SELECT * FROM user_performance WHERE time = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}

	public function queryByUserExclude($value){
		$sql = 'SELECT * FROM user_performance WHERE user_exclude = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}


	public function deleteByUserId($value){
		$sql = 'DELETE FROM user_performance WHERE user_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByConnectionId($value){
		$sql = 'DELETE FROM user_performance WHERE connection_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByType($value){
		$sql = 'DELETE FROM user_performance WHERE type = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByPerformance($value){
		$sql = 'DELETE FROM user_performance WHERE performance = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByTime($value){
		$sql = 'DELETE FROM user_performance WHERE time = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByUserExclude($value){
		$sql = 'DELETE FROM user_performance WHERE user_exclude = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}


	
	/**
	 * Read row
	 *
	 * @return UserPerformanceMySql 
	 */
	protected function readRow($row){
		$userPerformance = new UserPerformance();
		
		$userPerformance->id = $row['id'];
		$userPerformance->userId = $row['user_id'];
		$userPerformance->connectionId = $row['connection_id'];
		$userPerformance->type = $row['type'];
		$userPerformance->performance = $row['performance'];
		$userPerformance->time = $row['time'];
		$userPerformance->userExclude = $row['user_exclude'];

		return $userPerformance;
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
	 * @return UserPerformanceMySql 
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