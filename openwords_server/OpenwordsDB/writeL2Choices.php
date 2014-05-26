<?php
/**
 * @author: Mayukh Das
 * @date: May 22, 2014
 */


//include database objects----
require_once('include_dao.php');
require_once('../WordsDB/include_dao2.php');
$jsonresp = array();


if(isset($_GET['choices']))
{

	$l2choices = $_GET['choices'];
	$user = $_GET['id'];
	echo $user;

	$ch = explode(":",$l2choices);

	for($i = 0; $i < count($ch); $i=$i+1)
	{
		echo $ch[$i];
		$lang2 = DAOFactory2::getLanguagesDAO()->load($ch[$i]);
		$resp = DAOFactory::getPersonalDbLanguagePrefDAO()->insertMod($user,$lang2->id,$lang2->language);
	}

	
/*
	
	if(isset($result))
	{

		foreach($result as $row)
		{
			$record = array();
			$record["l2id"] = $row->optionL2Id;
			$record["l2name"] = $row->optionL2Name;
			
			//pushing to main json array
			array_push($jsonresp,$record);
		}
	}	
	else
	{

		array_push($jsonresp,array("l2id"=>-1,"l2name"=>"NA"));
		
	}
	echo json_encode($jsonresp);
*/
}
?>
