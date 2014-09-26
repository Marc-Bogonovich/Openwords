<?php

require_once "db.words.medoo.min.php";

if (isset($_POST["words"])) {
    $words_json = $_POST["words"];
} else {
    header("HTTP/1.1 400"); //For PHP 5.3
    echo "Invalid Parameters";
    return;
}

$ids = json_decode($words_json);
$database = new medoo();

$data = $database->select("word_audiocall", ["word_id(wordId)", "url(fileName)"], ["AND" => ["word_id" => $ids, "type" => 0]]);

header("Content-Type: application/json");
echo json_encode($data);
