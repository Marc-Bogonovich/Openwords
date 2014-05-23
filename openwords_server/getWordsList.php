<?php

require_once 'medoo.min.php';

if (isset($_POST["name"])) {
    $lid = $_POST["languageId"];
} else {
    $lid = $_GET["languageId"];
}

$database = new medoo();

$data = $database->select("words", "*", ["language_id" => $lid]);

echo json_encode($data);
