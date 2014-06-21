<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
interface WordTagsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return WordTags 
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
 	 * @param wordTag primary key
 	 */
	public function delete($id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param WordTags wordTag
 	 */
	public function insert($wordTag);
	
	/**
 	 * Update record in table
 	 *
 	 * @param WordTags wordTag
 	 */
	public function update($wordTag);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByWordId($value);

	public function queryByTag($value);


	public function deleteByWordId($value);

	public function deleteByTag($value);


}
?>