<?php
/**
 * Class that operate on table 'user_performance'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
class UserPerformanceMySqlDAO implements UserPerformanceDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return UserPerformanceMySql 
	 */
	public function load($userId, $connectionId){
		$sql = 'SELECT * FROM user_performance WHERE user_id = ?  AND connection_id = ? ';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($userId);
		$sqlQuery->setNumber($connectionId);

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
	public function delete($userId, $connectionId){
		$sql = 'DELETE FROM user_performance WHERE user_id = ?  AND connection_id = ? ';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($userId);
		$sqlQuery->setNumber($connectionId);

		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param UserPerformanceMySql userPerformance
 	 */
	public function insert($userPerformance){
		$sql = 'INSERT INTO user_performance (total_correct, total_skipped, total_exposure, last_time, last_performance, user_exclude, user_id, connection_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->setNumber($userPerformance->totalCorrect);
		$sqlQuery->setNumber($userPerformance->totalSkipped);
		$sqlQuery->setNumber($userPerformance->totalExposure);
		$sqlQuery->set($userPerformance->lastTime);
		$sqlQuery->setNumber($userPerformance->lastPerformance);
		$sqlQuery->setNumber($userPerformance->userExclude);

		
		$sqlQuery->setNumber($userPerformance->userId);

		$sqlQuery->setNumber($userPerformance->connectionId);

		$this->executeInsert($sqlQuery);	
		//$userPerformance->id = $id;
		//return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param UserPerformanceMySql userPerformance
 	 */
	public function update($userPerformance){
		$sql = 'UPDATE user_performance SET total_correct = ?, total_skipped = ?, total_exposure = ?, last_time = ?, last_performance = ?, user_exclude = ? WHERE user_id = ?  AND connection_id = ? ';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->setNumber($userPerformance->totalCorrect);
		$sqlQuery->setNumber($userPerformance->totalSkipped);
		$sqlQuery->setNumber($userPerformance->totalExposure);
		$sqlQuery->set($userPerformance->lastTime);
		$sqlQuery->setNumber($userPerformance->lastPerformance);
		$sqlQuery->setNumber($userPerformance->userExclude);

		
		$sqlQuery->setNumber($userPerformance->userId);

		$sqlQuery->setNumber($userPerformance->connectionId);

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

	public function queryByTotalCorrect($value){
		$sql = 'SELECT * FROM user_performance WHERE total_correct = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByTotalSkipped($value){
		$sql = 'SELECT * FROM user_performance WHERE total_skipped = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByTotalExposure($value){
		$sql = 'SELECT * FROM user_performance WHERE total_exposure = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByLastTime($value){
		$sql = 'SELECT * FROM user_performance WHERE last_time = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}

	public function queryByLastPerformance($value){
		$sql = 'SELECT * FROM user_performance WHERE last_performance = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByUserExclude($value){
		$sql = 'SELECT * FROM user_performance WHERE user_exclude = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}


	public function deleteByTotalCorrect($value){
		$sql = 'DELETE FROM user_performance WHERE total_correct = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByTotalSkipped($value){
		$sql = 'DELETE FROM user_performance WHERE total_skipped = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByTotalExposure($value){
		$sql = 'DELETE FROM user_performance WHERE total_exposure = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByLastTime($value){
		$sql = 'DELETE FROM user_performance WHERE last_time = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByLastPerformance($value){
		$sql = 'DELETE FROM user_performance WHERE last_performance = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
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
		
		$userPerformance->userId = $row['user_id'];
		$userPerformance->connectionId = $row['connection_id'];
		$userPerformance->totalCorrect = $row['total_correct'];
		$userPerformance->totalSkipped = $row['total_skipped'];
		$userPerformance->totalExposure = $row['total_exposure'];
		$userPerformance->lastTime = $row['last_time'];
		$userPerformance->lastPerformance = $row['last_performance'];
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