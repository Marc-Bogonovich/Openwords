<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
interface UserDbDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return UserDb 
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
 	 * @param userDb primary key
 	 */
	public function delete($email);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param UserDb userDb
 	 */
	public function insert($userDb);
	
	/**
 	 * Update record in table
 	 *
 	 * @param UserDb userDb
 	 */
	public function update($userDb);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryById($value);

	public function queryByPassword($value);


	public function deleteById($value);

	public function deleteByPassword($value);


}
?>