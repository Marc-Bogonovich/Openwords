<?php

class Result {

    public $words;

}

require_once 'medoo.min.php';

if (isset($_POST["languageId"])) {
    $lid = $_POST["languageId"];
} else {
    $lid = $_GET["languageId"];
}

$database = new medoo();

$data = $database->select("words", "*", ["language_id" => $lid]);

$r = new Result();
$r->words = $data;

header('Content-Type: application/json');
echo json_encode($r);
