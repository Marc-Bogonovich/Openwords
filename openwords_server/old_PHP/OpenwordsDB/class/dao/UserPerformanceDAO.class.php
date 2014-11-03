<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-07-03 10:57
 */
interface UserPerformanceDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return UserPerformance 
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
 	 * @param userPerformance primary key
 	 */
	public function delete($id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param UserPerformance userPerformance
 	 */
	public function insert($userPerformance);
	
	/**
 	 * Update record in table
 	 *
 	 * @param UserPerformance userPerformance
 	 */
	public function update($userPerformance);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByUserId($value);

	public function queryByConnectionId($value);

	public function queryByType($value);

	public function queryByPerformance($value);

	public function queryByTime($value);

	public function queryByUserExclude($value);


	public function deleteByUserId($value);

	public function deleteByConnectionId($value);

	public function deleteByType($value);

	public function deleteByPerformance($value);

	public function deleteByTime($value);

	public function deleteByUserExclude($value);


}
?>