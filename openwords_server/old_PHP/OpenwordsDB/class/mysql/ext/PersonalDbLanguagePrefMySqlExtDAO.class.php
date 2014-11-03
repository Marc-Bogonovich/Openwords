<?php
/**
 * Class that operate on table 'personal_db_language_pref'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
class PersonalDbLanguagePrefMySqlExtDAO extends PersonalDbLanguagePrefMySqlDAO{

	public function insertMod($user,$l2id){
		$sql = 'INSERT INTO personal_db_language_pref (user_id, l2_pref_id, l2_pref_name) SELECT ?,id,language FROM oworg_owr_1_0.languages where id = ?';
		
		$sqlQuery = new SqlQuery($sql);
		
		//$sqlQuery->set($l2name);
	
		$sqlQuery->setNumber($user);

		$sqlQuery->setNumber($l2id);

		return $this->executeInsert($sqlQuery);	
		//$personalDbLanguagePref->id = $id;
		//return $id;
	}

	public function queryByUser($value){
		$sql = 'SELECT * FROM personal_db_language_pref WHERE user_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}


	/**
 	 * Delete record from table by user ID
 	 * @param personalDbLanguagePref primary key
 	 */
	public function deleteByUser($userId){
		$sql = 'DELETE FROM personal_db_language_pref WHERE user_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($userId);

		return $this->executeUpdate($sqlQuery);
	}
}
?>