<?php

/**
 * DAOFactory
 * @author: http://phpdao.com
 * @date: ${date}
 */
class DAOFactory2{
	
	/**
	 * @return ConnectionTagsDAO
	 */
	public static function getConnectionTagsDAO(){
		return new ConnectionTagsMySqlExtDAO();
	}

	/**
	 * @return LanguagesDAO
	 */
	public static function getLanguagesDAO(){
		return new LanguagesMySqlExtDAO();
	}

	/**
	 * @return LearnableLanguageOptionsDAO
	 */
	public static function getLearnableLanguageOptionsDAO(){
		return new LearnableLanguageOptionsMySqlExtDAO();
	}

	/**
	 * @return RankTypesDAO
	 */
	public static function getRankTypesDAO(){
		return new RankTypesMySqlExtDAO();
	}

	/**
	 * @return WordAudiocallDAO
	 */
	public static function getWordAudiocallDAO(){
		return new WordAudiocallMySqlExtDAO();
	}

	/**
	 * @return WordConnectionsDAO
	 */
	public static function getWordConnectionsDAO(){
		return new WordConnectionsMySqlExtDAO();
	}

	/**
	 * @return WordMeaningDAO
	 */
	public static function getWordMeaningDAO(){
		return new WordMeaningMySqlExtDAO();
	}

	/**
	 * @return WordRankDAO
	 */
	public static function getWordRankDAO(){
		return new WordRankMySqlExtDAO();
	}

	/**
	 * @return WordTagsDAO
	 */
	public static function getWordTagsDAO(){
		return new WordTagsMySqlExtDAO();
	}

	/**
	 * @return WordTranscriptionDAO
	 */
	public static function getWordTranscriptionDAO(){
		return new WordTranscriptionMySqlExtDAO();
	}

	/**
	 * @return WordsDAO
	 */
	public static function getWordsDAO(){
		return new WordsMySqlExtDAO();
	}


}
?>