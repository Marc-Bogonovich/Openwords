<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
interface WordConnectionsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return WordConnections 
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
 	 * @param wordConnection primary key
 	 */
	public function delete($id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param WordConnections wordConnection
 	 */
	public function insert($wordConnection);
	
	/**
 	 * Update record in table
 	 *
 	 * @param WordConnections wordConnection
 	 */
	public function update($wordConnection);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByWord1Id($value);

	public function queryByWord2Id($value);

	public function queryByConnectionType($value);


	public function deleteByWord1Id($value);

	public function deleteByWord2Id($value);

	public function deleteByConnectionType($value);


}
?>