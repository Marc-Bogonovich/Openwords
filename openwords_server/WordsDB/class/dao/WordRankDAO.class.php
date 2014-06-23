<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
interface WordRankDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return WordRank 
	 */
	public function load($id);

	/**
	 * Get all records from table
	 */
	public function queryAll();
	
	/**
	 * Get all records from table ordered by field
	 * @Param $orderColumn column name
	 */
	public function queryAllOrderBy($orderColumn);
	
	/**
 	 * Delete record from table
 	 * @param wordRank primary key
 	 */
	public function delete($id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param WordRank wordRank
 	 */
	public function insert($wordRank);
	
	/**
 	 * Update record in table
 	 *
 	 * @param WordRank wordRank
 	 */
	public function update($wordRank);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByWordId($value);

	public function queryByRank($value);

	public function queryByRankType($value);


	public function deleteByWordId($value);

	public function deleteByRank($value);

	public function deleteByRankType($value);


}
?>