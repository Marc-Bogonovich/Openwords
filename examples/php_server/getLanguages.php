<?php

require_once "db.words.medoo.min.php";

if (isset($_POST["user_id"]) && isset($_POST["lang_id"])) {
    $user_id = $_POST["user_id"];
    $lang_id = $_POST["lang_id"];
} else {
    header("HTTP/1.1 400"); //For PHP 5.3
    echo "Invalid Parameters";
    return;
}

$database = new medoo();

$where = array("l1_id" => $lang_id);

$langs = $database->select("learnable_language_options", "*", $where);

header("Content-Type: application/json");
echo json_encode(array("languages" => $langs));


