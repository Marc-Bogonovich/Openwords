<?php
/**
 * Class that operate on table 'word_transcription'. Database Mysql.
 *
 * @author: http://phpdao.com
 * @date: 2014-05-20 23:43
 */
class WordTranscriptionMySqlExtDAO extends WordTranscriptionMySqlDAO{

	//Custom Methods
	
	public function queryByWordIdTranscriptionType($word, $type){
		$sql = 'SELECT * FROM word_transcription WHERE word_id = ? AND transcription_type = ?';
		$sqlQuery = new SqlQuery2($sql);
		$sqlQuery->setNumber($word);
		$sqlQuery->setNumber($type);
		return $this->getList($sqlQuery);
	}
}
?>