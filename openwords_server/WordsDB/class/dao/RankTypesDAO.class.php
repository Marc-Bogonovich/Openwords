<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
interface RankTypesDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return RankTypes 
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
 	 * @param rankType primary key
 	 */
	public function delete($id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param RankTypes rankType
 	 */
	public function insert($rankType);
	
	/**
 	 * Update record in table
 	 *
 	 * @param RankTypes rankType
 	 */
	public function update($rankType);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByRankTypeName($value);


	public function deleteByRankTypeName($value);


}
?>