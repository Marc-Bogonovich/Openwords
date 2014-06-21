<?php

/**
 * @author: Mayukh Das
 * @date: May 22, 2014
 */


//include database objects----
require_once('include_dao.php');
require_once('../WordsDB/include_dao2.php');
$response = array();


if(isset($_POST['choices']))
{

	$l2choices = $_POST['choices'];
	$user = $_POST['id'];
	//echo $user;

	$ch = explode("|",$l2choices);
	
	$respdel = DAOFactory::getPersonalDbLanguagePrefDAO()->deleteByUser($user);
	
	$alltheresp = 0;
	for($i = 0; $i < count($ch); $i=$i+1)
	{
		//echo $ch[$i];
		//$lang2 = DAOFactory2::getLanguagesDAO()->load($ch[$i]);
		//echo $lang2->language;
				
		$resp = DAOFactory::getPersonalDbLanguagePrefDAO()->insertMod($user,$ch[$i]);
		if(isset($resp))
		{
			$alltheresp = $alltheresp + 1;
		}
		
	}

	//----building response----
	
	//echo $alltheresp;

	if($alltheresp == count($ch)) { 
 		$response["success"] = 1;
        $response["message"] = "Preference created";
	}
 	else {
		$response["success"] = 0;
        $response["message"] = "Could not write to database";
	}	

	echo json_encode($response);
}
?>
