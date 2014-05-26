<?php
/**
 * Class that operate on table 'personal_db_language_pref'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
class PersonalDbLanguagePrefMySqlExtDAO extends PersonalDbLanguagePrefMySqlDAO{

	public function insertMod($user,$l2id,$l2name){
		$sql = 'INSERT INTO personal_db_language_pref (l2_pref_name, user_id, l2_pref_id) VALUES (?, ?, ?)';
		
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->set($l2name);
	
		$sqlQuery->setNumber($user);

		$sqlQuery->setNumber($l2id);

		$this->executeInsert($sqlQuery);	
		//$personalDbLanguagePref->id = $id;
		//return $id;
	}

	public function queryByUser($value){
		$sql = 'SELECT * FROM personal_db_language_pref WHERE user_id = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}
}
?>