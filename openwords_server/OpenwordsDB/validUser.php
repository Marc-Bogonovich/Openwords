<?php
//http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/
// include db connect class
require_once('include_dao.php');


$response = array();

if(isset($_POST['email']) && isset($_POST['password']) ) {
	//require_once __DIR__ . '/db_connect.php';
	//$db = new DB_CONNECT();
	$email = $_POST['email'];
 	$password = $_POST['password'];
	//$query = "select * from user_db where email = '".$email."' AND password = '".$password. "'";
	//$query_exec = mysql_query($query);
	//$rows = mysql_num_rows($query_exec);
	
	//----------new code with dao---------
	//echo $email;
	$result = DAOFactory::getUserDbDAO()->queryByIdAndPassword($email,$password);
	
	//echo $result[0]->email;

	if(count($result) == 1) { 
 		$response["success"] = 1;
        	$response["message"] = "User exists";
		$response["userid"] = intval($result[0]->id);
	}
 	else {
		$response["success"] = 0;
        $response["message"] = "Invalid email/password";
	}
} else {
	$response["success"] = 0;
        $response["message"] = "No data";
	//$response["message"] = "email: " . $_POST['email'] . "password" . $_POST['password'];
}
echo json_encode($response);
?>