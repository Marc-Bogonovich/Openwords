<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
interface PersonalDbUserDetailsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return PersonalDbUserDetails 
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
 	 * @param personalDbUserDetail primary key
 	 */
	public function delete($user_id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param PersonalDbUserDetails personalDbUserDetail
 	 */
	public function insert($personalDbUserDetail);
	
	/**
 	 * Update record in table
 	 *
 	 * @param PersonalDbUserDetails personalDbUserDetail
 	 */
	public function update($personalDbUserDetail);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByAboutU1($value);

	public function queryByAboutU2($value);

	public function queryByL1PrefId($value);


	public function deleteByAboutU1($value);

	public function deleteByAboutU2($value);

	public function deleteByL1PrefId($value);


}
?>