<?php
/**
 * Class that operate on table 'user_words'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-06-13 16:33
 */
class UserWordsMySqlExtDAO extends UserWordsMySqlDAO{


//custom methods

	public function queryByUserAndLanguage($user,$language){
		$sql = 'SELECT * FROM user_words WHERE user_id=?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($user);
		return $this->getList($sqlQuery);
	}

	public function queryByUserAndConnection($user,$connection){
		$sql = 'SELECT * FROM user_words WHERE connection_id = ? AND user_id=?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($connection);
		$sqlQuery->setNumber($user);
		return $this->getList($sqlQuery);
	}
	

	//custom update
	public function updateFresh($user,$con,$fresh){
		$sql = 'UPDATE user_words SET fresh = ? WHERE user_id = ?  AND connection_id = ? ';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->setNumber($fresh);
		$sqlQuery->setNumber($user);
		$sqlQuery->setNumber($con);

		return $this->executeUpdate($sqlQuery);
	}
}
?>