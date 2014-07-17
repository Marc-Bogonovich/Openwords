<?php

//include DAO
require_once('include_dao.php');

$response = array();
$done=array();

if(isset($_POST['user']) && isset($_POST['con']) && isset($_POST['fresh']))
{
	
	$user = $_POST['user'];
	$con = $_POST['con'];
	$fresh = $_POST['fresh'];


	$recExist = DAOFactory::getUserWordsDAO()->queryByUserAndConnection($user,$con);
	
	if(count($recExist)>0)
	{
		
			DAOFactory::getUserWordsDAO()->updateFresh($user,$con,$fresh);
	}
	$response["success"]=1;
	$response["message"]='Updated successfully';

}
else
{
	$response["success"]=0;
	$response["message"]='Not updated';
}

echo json_encode($response);

?>