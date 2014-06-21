<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
interface LanguagesDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return Languages 
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
 	 * @param language primary key
 	 */
	public function delete($id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param Languages language
 	 */
	public function insert($language);
	
	/**
 	 * Update record in table
 	 *
 	 * @param Languages language
 	 */
	public function update($language);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByLanguage($value);


	public function deleteByLanguage($value);


}
?>