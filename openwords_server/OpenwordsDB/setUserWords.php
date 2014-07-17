<?php
//include DAOs
require_once('include_dao.php');
require_once('../WordsDB/include_dao2.php');

$response = array();
if(isset($_POST['conid']) && isset($_POST['dtime']) && isset($_POST['user']) && isset($_POST['lTwo']))
{

	//getting the parameters
	$conid = $_POST['conid'];
	$dTime = $_POST['dtime'];
	$user = $_POST['user'];
	$l2 = $_POST['lTwo'];
	
	//exploding con ids
	$conIdArr = explode("|",$conid);
	
	//reading and inserting each connection into database
	for($i = 0; $i <count($conIdArr); $i++)
	{
		
		$connectionInfo = DAOFactory2::getWordConnectionsDAO()->load($conIdArr[$i]);
		
		$userWordsRec = new UserWord();
		//setting values
		$userWordsRec->userId = $user;
		$userWordsRec->connectionId = $connectionInfo->id;
		$userWordsRec->wordl2Id = $connectionInfo->word2Id;
		$userWordsRec->wordl1Id = $connectionInfo->word1Id;
		$userWordsRec->l2Id = $l2;
		$userWordsRec->downloadTime = $dTime;
		$userWordsRec->fresh=1;
		//insert into DB
		$flag = DAOFactory::getUserWordsDAO()->insert($userWordsRec);
		
	}
	$response["success"] = 1;
	$response["message"] = "Inserted successfully";
	
}
else
{
	$response["success"] = 0;
	$response["message"] = "Insertion failed";
}

echo json_encode($response);

?>