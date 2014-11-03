<?php
// include db connect class
require_once('OpenwordsDB/include_dao.php');

$response = array();

if(isset($_POST['user']) && isset($_POST['connectionid'])) {
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	$user = $_POST['user'];
 	$connectionid = $_POST['connectionid'];
	//echo $user;
	$query = "SELECT user_id,connection_id, type, case when type>0 then sum(skipped) else 0 end as totalSkipped, 
				case when type>0 then sum(wrong) else 0 end as total_wrong, case when type>0 then sum(closeEnuf) else 0 end as total_close, 
				case when type>0 then sum(correct) else 0 end as total_correct, count(1) total_expose, max(time) last_time	
				FROM (
					SELECT user_id, connection_id, type,
					case when performance = 0 then 1 else 0 end as skipped,
					case when performance = 1 then 1 else 0 end as wrong,
					case when performance = 2 then 1 else 0 end as closeEnuf,
					case when performance = 3 then 1 else 0 end as correct,
					time
					FROM  `user_performance` 
					WHERE user_id =".$user." AND connection_id = ".$connectionid.") det
				group by user_id, connection_id, type";
	//echo $query;
	$query_exec = mysql_query($query);
	$rows = mysql_num_rows($query_exec);

	//echo $rows;
	if($rows > 0) { 
		$dataSet = array();
		while ($row = mysql_fetch_assoc($query_exec))
		{
			
			$rec = array();
			$rec["userid"] = intval($row['user_id']);
			
			$rec["connectionid"] = intval($row['connection_id']);
			$rec["module"] = intval($row['type']);
			$rec["totalSkipped"] = intval($row['totalSkipped']);
			$rec["total_wrong"] = intval($row['total_wrong']);
			$rec["total_close"] = intval($row['total_close']);
			$rec["total_correct"] = intval($row['total_correct']);
			$rec["total_expose"] = intval($row['total_expose']);
			$rec["last_time"] = intval($row['last_time']);
			
			$grainRec=DAOFactory::getUserPerformanceDAO()->queryByUserConTime($user,$connectionid,intval($row['last_time']));
			$rec["last_performance"] = 	intval($grainRec[0]->performance);		
			
			array_push($dataSet,$rec);
		}
		//echo $dataSet[0]["last_time"];
		$response["success"] = 1;
        	$response["data"] = $dataSet;
	}
 	else {
		$response["success"] = 0;
        $response["message"] = "No data--";
	}

	
} else {
	$response["success"] = 0;
        $response["message"] = "No data";
	//$response["message"] = "email: " . $_POST['email'] . "password" . $_POST['password'];
}

echo json_encode($response);

?>