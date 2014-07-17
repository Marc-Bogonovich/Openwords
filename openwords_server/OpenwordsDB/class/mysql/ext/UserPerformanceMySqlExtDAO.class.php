<?php
/**
 * Class that operate on table 'user_performance'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-19 16:06
 */
class UserPerformanceMySqlExtDAO extends UserPerformanceMySqlDAO{

	//custom
	public function queryByUserConTime($user,$con,$t){
		$sql = 'SELECT * FROM user_performance WHERE user_id = ? and connection_id = ? and time = ?';
		$sqlQuery = new SqlQuery($sql);
		$sqlQuery->setNumber($user);
		$sqlQuery->setNumber($con);
		$sqlQuery->setNumber($t);
		return $this->getList($sqlQuery);
	}
}
?>