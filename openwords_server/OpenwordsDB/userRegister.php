<?php
//http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/
// include db connect class
require_once('include_dao.php');

$response = array();
if(isset($_POST['email']) && isset($_POST['password'])) {
	//----------not needed--------
	//require_once __DIR__ . '/db_connect.php';
	//$db = new DB_CONNECT();
	//-----------------------------

	$email = $_POST['email'];
 	$password = $_POST['password'];
	
	//$query = "insert into user_db (email, password) values ('".$email. "', '".$password."')";

	//-----new code with DAO----
	$newuser = new UserDb();
	$newuser->email = $email;
	$newuser->password = $password;

	$flag = DAOFactory::getUserDbDAO()->insertMod($newuser);
	//echo $flag;
	
	//----populating personal details-----
	$newUserPref = new PersonalDbUserDetail();
	$newUserPref->userId = $flag;
	$newUserPref->aboutU1 = '';
	$newUserPref->aboutU2 = '';
	$newUserPref->l1PrefId = 1;
	$flag2 = DAOFactory::getPersonalDbUserDetailsDAO()->insertMod($newUserPref);
	//--------------------
	echo $flag2;

	if($flag) { 
 		$response["success"] = 1;
        $response["message"] = "User create successfully";
	}
 	else {
		$response["success"] = 0;
        $response["message"] = "User create fail";
	}
} else {
	$response["success"] = 0;
        $response["message"] = "No data";
	//$response["message"] = "email: " . $_POST['email'] . "password" . $_POST['password'];
}
echo json_encode($response);
?>