<?php
/**
 * Intreface DAO
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
interface LearnableLanguageOptionsDAO{

	/**
	 * Get Domain object by primry key
	 *
	 * @param String $id primary key
	 * @Return LearnableLanguageOptions 
	 */
	public function load($l1Id, $optionL2Id);

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
 	 * @param learnableLanguageOption primary key
 	 */
	public function delete($l1Id, $optionL2Id);
	
	/**
 	 * Insert record to table
 	 *
 	 * @param LearnableLanguageOptions learnableLanguageOption
 	 */
	public function insert($learnableLanguageOption);
	
	/**
 	 * Update record in table
 	 *
 	 * @param LearnableLanguageOptions learnableLanguageOption
 	 */
	public function update($learnableLanguageOption);	

	/**
	 * Delete all rows
	 */
	public function clean();

	public function queryByOptionL2Name($value);


	public function deleteByOptionL2Name($value);


}
?>