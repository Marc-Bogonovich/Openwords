<?php
//http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/
// include db connect class
require_once('include_dao.php');

$response = array();
if(isset($_POST['email'])) {
	//require_once __DIR__ . '/db_connect.php';
	//$db = new DB_CONNECT();
	$email = $_POST['email'];
	//$query = "select * from user_db where email = '".$email. "'";
	//$query_exec = mysql_query($query);
	//$rows = mysql_num_rows($query_exec);
	
	//----new code with DAO-----
	$rows = DAOFactory::getUserDbDAO()->load($email);
	
	if($rows->email == "") { 
 		$response["success"] = 1;
        $response["message"] = "User doesn't exist";
	}
 	else {
		$response["success"] = 0;
        $response["message"] = "User exists";
	}
} else {
	$response["success"] = 0;
        $response["message"] = "No data";
	//$response["message"] = "email: " . $_POST['email'] . "password" . $_POST['password'];
}
echo json_encode($response);
?>