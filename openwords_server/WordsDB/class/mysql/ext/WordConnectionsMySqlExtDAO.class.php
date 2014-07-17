<?php
/**
 * Class that operate on table 'word_connections'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:49
 */
class WordConnectionsMySqlExtDAO extends WordConnectionsMySqlDAO{

	//Custom methods

	public function queryByLangOneLangTwo($user,$langOne,$langTwo){
		$sql = 'SELECT id, word1_id, word2_id, connection_type
			FROM (

				SELECT wc. * , wr.rank
				FROM oworg_owr_1_0.word_connections wc, oworg_owr_1_0.words wl1, oworg_owr_1_0.words wl2 left join oworg_owr_1_0.word_rank wr on wl2.id = wr.word_id
				WHERE wc.word1_id = wl1.id
				AND wc.word2_id = wl2.id
				AND wl1.language_id =?
				AND wl2.language_id =?
				AND NOT EXISTS (

						SELECT 1 
						FROM oworg_openwords.user_words uw
						WHERE uw.user_id =?
						AND uw.connection_id = wc.id)
				ORDER BY wr.rank DESC)aggr';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($langOne);
		$sqlQuery->setNumber($langTwo);
		$sqlQuery->setNumber($user);
		return $this->getList($sqlQuery);
	}


	public function queryByLangOneLangTwoText($user,$langOne,$langTwo,$searchText){
		$sql = 'SELECT id, word1_id, word2_id, connection_type
			FROM (

				SELECT wc. * , wr.rank
				FROM oworg_owr_1_0.word_connections wc, oworg_owr_1_0.words wl1, oworg_owr_1_0.words wl2, oworg_owr_1_0.word_rank wr
				WHERE wc.word1_id = wl1.id
				AND wc.word2_id = wl2.id
				AND wl1.language_id =?
				AND wl2.language_id =?
				AND wl1.word LIKE ?
				AND wl2.id = wr.word_id
				AND NOT EXISTS (

						SELECT 1 
						FROM oworg_openwords.user_words uw
						WHERE uw.user_id =?
						AND uw.connection_id = wc.id)
				ORDER BY wr.rank DESC)aggr';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($langOne);
		$sqlQuery->setNumber($langTwo);
		$sqlQuery->set($searchText.'%');
		$sqlQuery->setNumber($user);

		return $this->getList($sqlQuery);
	}

}
?>