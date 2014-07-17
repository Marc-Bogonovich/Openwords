<?php

//include DAO
require_once('include_dao.php');

$response = array();
$done=array();

$c=1;
if(isset($_POST['params']))
{
	
	$param = $_POST['params'];
	
	//unpacking json string-----
	$jsonarr = json_decode($param);
	$childarr = $jsonarr->data;
	$userPerfRec = new UserPerformance();
	//scanning through JSON array of data
	$l=count($childarr);
	foreach($childarr as $obj)
	{
		//print $obj->name;
		if($index > $l-1)
		{ 	echo "breaking";
			break;
		}
		$userPerfRec->connectionId = $obj->connection_id;
		$userPerfRec->userId = $obj->user_id;
		$userPerfRec->type = $obj->type;
		$userPerfRec->performance = $obj->perf;
		$userPerfRec->time = $obj->time;
		$userPerfRec->userExclude = $obj->user_ex;
		
		//echo $userPerfRec->connectionId."--".$userPerfRec->userId."--".$userPerfRec->time." : ";

		//insert data....
		$id=DAOFactory::getUserPerformanceDAO()->insert($userPerfRec);
		
		if(isset($id))
		{array_push($done,$id);}
		
 		$idx++;
	}
	if(count($done)==$l)
		{
			$response["success"]=1;
			$response["message"]='done';
		}
		else
		{
			$response["success"]=0;
			$response["message"]='Not inserted';
			//if insert failure in one delete all others
			for($i=0;i<count($done);$i++)
			{
				//$resp = DAOFactory::getUserPerformanceDAO()->delete($done[$i]);
			}
		}

}
else
{
	$response["success"]=0;
	$response["message"]='Not inserted';
}

echo json_encode($response);
?>