<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
interface WordAudiocallDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return WordAudiocall 
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
 	 * @param wordAudiocall primary key
 	 */
	public function delete($id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param WordAudiocall wordAudiocall
 	 */
	public function insert($wordAudiocall);
	
	/**
 	 * Update record in table
 	 *
 	 * @param WordAudiocall wordAudiocall
 	 */
	public function update($wordAudiocall);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByWordId($value);

	public function queryByUrl($value);


	public function deleteByWordId($value);

	public function deleteByUrl($value);


}
?>