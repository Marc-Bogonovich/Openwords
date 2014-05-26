<?php
/**
 * Class that operate on table 'connection_tags'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
class ConnectionTagsMySqlDAO implements ConnectionTagsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @return ConnectionTagsMySql 
	 */
	public function load($id){
		$sql = 'SELECT * FROM connection_tags WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($id);
		return $this->getRow($sqlQuery);
	}

	/**
	 * Get all records from table
	 */
	public function queryAll(){
		$sql = 'SELECT * FROM connection_tags';
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
	 * Get all records from table ordered by field
	 *
	 * @param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn){
		$sql = 'SELECT * FROM connection_tags ORDER BY '.$orderColumn;
		$sqlQuery = new SqlQuery2($sql);
		return $this->getList($sqlQuery);
	}
	
	/**
 	 * Delete record from table
 	 * @param connectionTag primary key
 	 */
	public function delete($id){
		$sql = 'DELETE FROM connection_tags WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($id);
		return $this->executeUpdate($sqlQuery);
	}
	
	/**
 	 * Insert record to table
 	 *
 	 * @param ConnectionTagsMySql connectionTag
 	 */
	public function insert($connectionTag){
		$sql = 'INSERT INTO connection_tags (tie_id, tag) VALUES (?, ?)';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->setNumber($connectionTag->tieId);
		$sqlQuery->set($connectionTag->tag);

		$id = $this->executeInsert($sqlQuery);	
		$connectionTag->id = $id;
		return $id;
	}
	
	/**
 	 * Update record in table
 	 *
 	 * @param ConnectionTagsMySql connectionTag
 	 */
	public function update($connectionTag){
		$sql = 'UPDATE connection_tags SET tie_id = ?, tag = ? WHERE id = ?';
		$sqlQuery = new SqlQuery2($sql);
		
		$sqlQuery->setNumber($connectionTag->tieId);
		$sqlQuery->set($connectionTag->tag);

		$sqlQuery->setNumber($connectionTag->id);
		return $this->executeUpdate($sqlQuery);
	}

	/**
 	 * Delete all rows
 	 */
	public function clean(){
		$sql = 'DELETE FROM connection_tags';
		$sqlQuery = new SqlQuery2($sql);
		return $this->executeUpdate($sqlQuery);
	}

	public function queryByTieId($value){
		$sql = 'SELECT * FROM connection_tags WHERE tie_id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}

	public function queryByTag($value){
		$sql = 'SELECT * FROM connection_tags WHERE tag = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->getList($sqlQuery);
	}


	public function deleteByTieId($value){
		$sql = 'DELETE FROM connection_tags WHERE tie_id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->executeUpdate($sqlQuery);
	}

	public function deleteByTag($value){
		$sql = 'DELETE FROM connection_tags WHERE tag = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->set($value);
		return $this->executeUpdate($sqlQuery);
	}


	
	/**
	 * Read row
	 *
	 * @return ConnectionTagsMySql 
	 */
	protected function readRow($row){
		$connectionTag = new ConnectionTag();
		
		$connectionTag->id = $row['id'];
		$connectionTag->tieId = $row['tie_id'];
		$connectionTag->tag = $row['tag'];

		return $connectionTag;
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
	 * @return ConnectionTagsMySql 
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