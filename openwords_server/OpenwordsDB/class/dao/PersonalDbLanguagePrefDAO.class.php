<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
interface PersonalDbLanguagePrefDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return PersonalDbLanguagePref 
	 */
	public function load($userId, $l2PrefId);

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
 	 * @param personalDbLanguagePref primary key
 	 */
	public function delete($userId, $l2PrefId);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param PersonalDbLanguagePref personalDbLanguagePref
 	 */
	public function insert($personalDbLanguagePref);
	
	/**
 	 * Update record in table
 	 *
 	 * @param PersonalDbLanguagePref personalDbLanguagePref
 	 */
	public function update($personalDbLanguagePref);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByL2PrefName($value);


	public function deleteByL2PrefName($value);


}
?>