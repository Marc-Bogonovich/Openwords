<?php

require_once "db.user.medoo.min.php";

if (isset($_POST["user_name"]) && isset($_POST["password"])) {
    $user_name = $_POST["user_name"];
    $password = $_POST["password"];
} else {
    header("HTTP/1.1 400"); //For PHP 5.3
    echo "Invalid Parameters";
    return;
}

$database = new medoo();

$where = array("AND" => array("email" => $user_name, "password" => $password));

header("Content-Type: application/json");
if ($database->has("user_db", $where)) {
    session_start();
    $current_time = time();

    $user_id = $database->select("user_db", array("id"), $where);

    echo json_encode(array("valid" => true,
        "user_id" => $user_id[0]["id"],
        "session_id" => session_id(),
        "current_time" => $current_time,
        "last_login_time" => $current_time,
        "error_message" => ""));
} else {
    echo json_encode(array("valid" => false));
}


