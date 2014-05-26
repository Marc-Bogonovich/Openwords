<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
interface RoleUserDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return RoleUser 
	 */
	public function load($userId, $roleId);

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
 	 * @param roleUser primary key
 	 */
	public function delete($userId, $roleId);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param RoleUser roleUser
 	 */
	public function insert($roleUser);
	
	/**
 	 * Update record in table
 	 *
 	 * @param RoleUser roleUser
 	 */
	public function update($roleUser);	

	/**
	 * Delete all rows
	 */
	public function clean();



}
?>