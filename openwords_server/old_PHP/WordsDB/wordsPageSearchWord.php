<?php

//include DAO
require_once('../OpenwordsDB/include_dao.php');
require_once('include_dao2.php');

$response = array();
$done=array();

if(isset($_POST['user']) && isset($_POST['langTwo']) && isset($_POST['langOne']) && isset($_POST['searchText']))
{
	
	$user = $_POST['user'];
	$langTwo = $_POST['langTwo'];
	$langOne = $_POST['langOne'];
	$searchText = $_POST['searchText'];
	$TransType = 2;
	$dataSet = array();
	// Querying words Database
	$conns = DAOFactory2::getWordConnectionsDAO()->queryByLangOneLangTwoText($user,$langOne,$langTwo,$searchText);
	$l2 = DAOFactory2::getLanguagesDAO()->load($langTwo);
	$cnt = 0;
	foreach($conns as $con)
	{
		if ($cnt >= 10)
			break;
		//get word information
		$wordl1 = DAOFactory2::getWordsDAO()->load($con->word1Id);
		$wordl2 = DAOFactory2::getWordsDAO()->load($con->word2Id);
		$audio = DAOFactory2::getWordAudiocallDAO()->queryByWordId($con->word2Id);
		$conns = DAOFactory2::getWordConnectionsDAO()->queryByLangOneLangTwo($user,$langOne,$langTwo);
		$rec = array();
		//packing a record
		$rec["connection_id"] = intval($con->id);
		$rec["wordl1"] = intval($con->word1Id);
		$rec["wordl1_text"] = $wordl1->word;
		$rec["wordl2"] = intval($con->word2Id);
		$rec["wordl2_text"] = $wordl2->word;
		$rec["l2id"] = intval($l2->id);
		$rec["l2name"] = $l2->language;
		$rec["audio"] = $audio[0]->url;
		
		if(count($trans)>0)
		{
			$rec["trans"] = $trans[0]->transcription;
		}
		else
		{
			$rec["trans"] = '';
		}
		
		//pushing into array of records
		array_push($dataSet,$rec);
		$cnt++;
	}
	$response["success"]=1;
	$response["data"]=$dataSet;
	
}
else
{
	$response["success"]=0;
	$response["message"]='Not inserted';
}

//packing into json object
echo json_encode($response);
?>