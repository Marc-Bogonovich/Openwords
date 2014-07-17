<?php

//including necessary DAO files
require_once('include_dao.php');
require_once('../WordsDB/include_dao2.php');
$jsonResp = array();
if(isset($_POST['language']) && isset($_POST['user']))
{

	$lang = $_POST['language'];
	$user = $_POST['user'];
	$TransType = 2;
	
	//get user word records
	$userWords = DAOFActory::getUserWordsDAO()->queryByUserAndLanguage($user,$language);
	
	$resultArr = array();
	if(count($userWords)>0)
	{
		foreach($userWords as $rec)
		{
			$record = array();
			$wordl2 = DAOFActory2::getWordsDAO()->load($rec->wordl2Id);
			$wordl1 = DAOFactory2::getWordsDAO()->load($rec->wordl1Id);
			$langData = DAOFactory2::getLanguagesDAO()->load($rec->l2Id);
			$audio = DAOFactory2::getWordAudiocallDAO()->queryByWordId($rec->wordl2Id);
			$trans = DAOFactory2::getWordTranscriptionDAO()->queryByWordIdTranscriptionType($wordl2->id,$TransType);
			
			$record["user_id"] =  $rec->userId;
			$record["connection_id"] = $rec->connectionId;
			$record["wordl2id"] = $rec->wordl2Id;
			$record["wordl2name"] = $wordl2->word;
			$record["wordl1id"] = $rec->wordl1Id;
			$record["wordl1name"] = $wordl1->word;
			$record["l2id"] = $rec->l2Id;
			$record["l2name"] = $langData->language;
			$record["audiocall"] = $audio->url;
			$record["fresh"] = $rec->fresh;
			


			if(count($trans)>0)
			{
				$record["trans"] = $trans[0]->transcription;
			}
			else
			{
				$record["trans"] = '';
			}


			//pushing to array
			array_push($resultArr,$record);
		}
		$jsonResp["success"] = 1;
		$jsonResp["data"] = $resultArr;
	}
	else
	{
		$jsonResp["success"] = 0;
		$jsonResp["data"] = "No Data";
	}
}
else
{
	$jsonResp["success"] = 0;
	$jsonResp["data"] = "No Data";
}

echo json_encode($jsonResp);
?>