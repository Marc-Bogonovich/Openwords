<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
interface ConnectionTagsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return ConnectionTags 
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
 	 * @param connectionTag primary key
 	 */
	public function delete($id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param ConnectionTags connectionTag
 	 */
	public function insert($connectionTag);
	
	/**
 	 * Update record in table
 	 *
 	 * @param ConnectionTags connectionTag
 	 */
	public function update($connectionTag);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByTieId($value);

	public function queryByTag($value);


	public function deleteByTieId($value);

	public function deleteByTag($value);


}
?>