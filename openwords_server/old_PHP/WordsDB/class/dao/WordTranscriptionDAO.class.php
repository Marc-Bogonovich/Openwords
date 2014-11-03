<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
interface WordTranscriptionDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return WordTranscription 
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
 	 * @param wordTranscription primary key
 	 */
	public function delete($id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param WordTranscription wordTranscription
 	 */
	public function insert($wordTranscription);
	
	/**
 	 * Update record in table
 	 *
 	 * @param WordTranscription wordTranscription
 	 */
	public function update($wordTranscription);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByWordId($value);

	public function queryByTranscription($value);

	public function queryByTranscriptionType($value);


	public function deleteByWordId($value);

	public function deleteByTranscription($value);

	public function deleteByTranscriptionType($value);


}
?>