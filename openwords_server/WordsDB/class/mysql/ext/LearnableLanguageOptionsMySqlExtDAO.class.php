<?php
/**
 * Class that operate on table 'learnable_language_options'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:43
 */
class LearnableLanguageOptionsMySqlExtDAO extends LearnableLanguageOptionsMySqlDAO{

	public function queryByL1Id($value){
		$sql = 'SELECT * FROM learnable_language_options WHERE l1_id = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($value);
		return $this->getList($sqlQuery);
	}
}
?>