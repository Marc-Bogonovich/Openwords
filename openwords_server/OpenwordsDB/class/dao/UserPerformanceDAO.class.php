<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
interface UserPerformanceDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return UserPerformance 
	 */
	public function load($userId, $connectionId);

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
	public function delete($userId, $connectionId);
	
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

	public function queryByTotalCorrect($value);

	public function queryByTotalSkipped($value);

	public function queryByTotalExposure($value);

	public function queryByLastTime($value);

	public function queryByLastPerformance($value);

	public function queryByUserExclude($value);


	public function deleteByTotalCorrect($value);

	public function deleteByTotalSkipped($value);

	public function deleteByTotalExposure($value);

	public function deleteByLastTime($value);

	public function deleteByLastPerformance($value);

	public function deleteByUserExclude($value);


}
?>