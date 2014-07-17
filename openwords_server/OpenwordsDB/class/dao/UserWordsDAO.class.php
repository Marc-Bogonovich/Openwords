<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-07-03 10:57
 */
interface UserWordsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return UserWords 
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
 	 * @param userWord primary key
 	 */
	public function delete($userId, $connectionId);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param UserWords userWord
 	 */
	public function insert($userWord);
	
	/**
 	 * Update record in table
 	 *
 	 * @param UserWords userWord
 	 */
	public function update($userWord);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByWordl2Id($value);

	public function queryByWordl1Id($value);

	public function queryByL2Id($value);

	public function queryByDownloadTime($value);

	public function queryByFresh($value);


	public function deleteByWordl2Id($value);

	public function deleteByWordl1Id($value);

	public function deleteByL2Id($value);

	public function deleteByDownloadTime($value);

	public function deleteByFresh($value);


}
?>