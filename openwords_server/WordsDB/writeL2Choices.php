<?php

//include database objects----
require_once('include_dao2.php');
$jsonresp = array();


if(isset($_POST['choices']))
{
	$l2choices = $_POST['choices'];

	$result = DAOFactory2::getLearnableLanguageOptionsDAO()->queryByL1Id($lang);


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
		echo "noooooo";
		array_push($jsonresp,array("l2id"=>-1,"l2name"=>"NA"));
		
	}
	echo json_encode($jsonresp);
}
?>
