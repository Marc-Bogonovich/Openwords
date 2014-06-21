<?php
/**
 * Class that operate on table 'user_db'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-16 17:15
 */
class UserDbMySqlExtDAO extends UserDbMySqlDAO{

	public function queryByIdAndPassword($em,$pwd){
		$sql = 'SELECT * FROM user_db where email = ? AND password = ?';
		
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setString($em);
		$sqlQuery->setString($pwd);
		return $this->getList($sqlQuery);
	}


	public function insertMod($userDb){
		$sql = 'INSERT INTO user_db (email, password) VALUES (?, ?)';
		$sqlQuery = new SqlQuery($sql);
		
		$sqlQuery->set($userDb->email);
		$sqlQuery->set($userDb->password);

		$id = $this->executeInsert($sqlQuery);	
		$userDb->email = $id;
		return $id;
	}
	
}
?>