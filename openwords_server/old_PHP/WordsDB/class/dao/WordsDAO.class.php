<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
interface WordsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return Words 
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
 	 * @param word primary key
 	 */
	public function delete($id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param Words word
 	 */
	public function insert($word);
	
	/**
 	 * Update record in table
 	 *
 	 * @param Words word
 	 */
	public function update($word);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByLanguageId($value);

	public function queryByWord($value);


	public function deleteByLanguageId($value);

	public function deleteByWord($value);


}
?>