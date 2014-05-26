<?php

/**
 * @author: Mayukh Das
 * @date: May 22, 2014
 */

//include database objects----
require_once('include_dao2.php');
require_once('../OpenwordsDB/include_dao.php');

$jsonresp = array();
$response = array();

if(isset($_GET['langone']))
{
	$lang = $_GET['langone'];
	$user = $_GET['userid'];

	$result = DAOFactory2::getLearnableLanguageOptionsDAO()->queryByL1Id($lang);
	$userDet = DAOFActory::getPersonalDbLanguagePrefDAO()->queryByUser($user);
	$userDetArr = array();
	for($i=0 ; $i<count($userDet) ; $i=$i+1)
	{
		array_push($userDetArr,$userDet[$i]->l2PrefId);
	}

	if(isset($result))
	{

		foreach($result as $row)
		{
			$record = array();
			$record["l2id"] = $row->optionL2Id;
			$record["l2name"] = $row->optionL2Name;
			if(in_array($row->optionL2Id,$userDetArr))
			{
				$record["chosen"] = 1;
			}
			else
			{
				$record["chosen"] = 0;
			}
			
			//pushing to main json array
			array_push($jsonresp,$record);
		}
	}	
	else
	{
		echo "noooooo";
		array_push($jsonresp,array("l2id"=>-1,"l2name"=>"NA","chosen"=>-1));
		
	}
	$response["langdata"] = $jsonresp;
	echo json_encode($response);
}
?>
