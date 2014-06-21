<?php
/**
 * Class that operate on table 'rank_types'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
class RankTypesMySqlDAO implements RankTypesDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return RankTypesMySql 
	 */
	public function load($id){
		$sql = 'SELECT * FROM rank_types WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($id);
		return $this->getRow($sqlQuery);
	}

	/**
	 * Get all records from table
	 */
	public function queryAll(){
		$sql = 'SELECT * FROM rank_types';
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
	 * Get all records from table ordered by field
	 *
	 * @param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn){
		$sql = 'SELECT * FROM rank_types ORDER BY '.$orderColumn;
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
 	 * Delete record from table
 	 * @param rankType primary key
 	 */
	public function delete($id){
		$sql = 'DELETE FROM rank_types WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($id);
		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param RankTypesMySql rankType
 	 */
	public function insert($rankType){
		$sql = 'INSERT INTO rank_types (rank_type_name) VALUES (?)';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->set($rankType->rankTypeName);

		$id = $this->executeInsert($sqlQuery);	
		$rankType->id = $id;
		return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param RankTypesMySql rankType
 	 */
	public function update($rankType){
		$sql = 'UPDATE rank_types SET rank_type_name = ? WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->set($rankType->rankTypeName);

		$sqlQuery->setNumber($rankType->id);
		return $this->executeUpdate($sqlQuery);
	}

	/**
 	 * Delete all rows
 	 */
	public function clean(){
		$sql = 'DELETE FROM rank_types';
		$sqlQuery = new SqlQuery2($sql);
		return $this->executeUpdate($sqlQuery);
	}

	public function queryByRankTypeName($value){
		$sql = 'SELECT * FROM rank_types WHERE rank_type_name = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}


	public function deleteByRankTypeName($value){
		$sql = 'DELETE FROM rank_types WHERE rank_type_name = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}


	
	/**
	 * Read row
	 *
	 * @return RankTypesMySql 
	 */
	protected function readRow($row){
		$rankType = new RankType();
		
		$rankType->id = $row['id'];
		$rankType->rankTypeName = $row['rank_type_name'];

		return $rankType;
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
	 * @return RankTypesMySql 
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