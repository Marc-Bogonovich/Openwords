<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
interface WordMeaningDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return WordMeaning 
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
 	 * @param wordMeaning primary key
 	 */
	public function delete($id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param WordMeaning wordMeaning
 	 */
	public function insert($wordMeaning);
	
	/**
 	 * Update record in table
 	 *
 	 * @param WordMeaning wordMeaning
 	 */
	public function update($wordMeaning);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByWordId($value);

	public function queryByType($value);

	public function queryByMeaningText($value);


	public function deleteByWordId($value);

	public function deleteByType($value);

	public function deleteByMeaningText($value);


}
?>