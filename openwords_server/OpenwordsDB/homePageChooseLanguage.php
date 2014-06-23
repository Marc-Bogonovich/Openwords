<?php
//include DAO files
require_once('include_dao.php');

$resp = array();
$jsonresp = array();
if(isset($_GET['userid']))
{
	//echo "hello";
	$user = $_GET['userid'];
	//-------getting language preference of user-------
	$result = DAOFactory::getPersonalDbLanguagePrefDAO()->queryByUser($user);

	if(isset($result))
	{
		
		foreach($result as $row)
		{
			$resp_row = array();
			//-----getting each chosen language---
			$resp_row["l2id"] = $row->l2PrefId;
			$resp_row["l2name"] = $row->l2PrefName;
			array_push($jsonresp,$resp_row);
		}
	}
	else
	{
		array_push($jsonresp,array("l2id"=>-1,"l2name"=>"NA"));
	}


	//--------building final response------
	$resp["language"] = $jsonresp;
	echo json_encode($resp);
}
?>