<?php
/**
 * Class that operate on table 'personal_db_user_details'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
class PersonalDbUserDetailsMySqlExtDAO extends PersonalDbUserDetailsMySqlDAO{

	public function insertMod($personalDbUserDetail){
		$sql = 'INSERT INTO personal_db_user_details (user_id, about_u1, about_u2, l1_pref_id) VALUES (?, ?, ?, ?)';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($personalDbUserDetail->userId);
		$sqlQuery->set($personalDbUserDetail->aboutU1);
		$sqlQuery->set($personalDbUserDetail->aboutU2);
		$sqlQuery->setNumber($personalDbUserDetail->l1PrefId);

		$id = $this->executeInsert($sqlQuery);	
		$personalDbUserDetail->userId = $id;
		return $id;
	}

}
?>